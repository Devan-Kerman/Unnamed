package net.devtech.unnamed;


import static net.devtech.unnamed.Unnamed.id;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.devtech.arrp.api.RuntimeResourcePack;
import net.devtech.arrp.json.lang.JLang;

import net.minecraft.util.Identifier;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings ("VariableUseSideOnly")
public class ResourceGen {
	@Environment (EnvType.CLIENT) private static final Multimap<String, Consumer<JLang>> LANGUAGES = HashMultimap.create();
	private static Consumer<RuntimeResourcePack> server, client;

	public static void registerServer(Consumer<RuntimeResourcePack> consumer) {
		if (server == null) {
			server = consumer;
		} else {
			server = server.andThen(consumer);
		}
	}

	public static void registerClient(Consumer<RuntimeResourcePack> consumer) {
		if (Unnamed.CLIENT) {
			if (client == null) {
				client = consumer;
			} else {
				client = client.andThen(consumer);
			}
		}
	}

	public static void registerLang(String language, Consumer<JLang> lang) {
		if (Unnamed.CLIENT) {
			LANGUAGES.put(language, lang);
		}
	}

	public static Identifier prefixPath(Identifier identifier, String prefix) {
		return new Identifier(identifier.getNamespace(), prefix + '/' + identifier.getPath());
	}


	public static void init(RuntimeResourcePack pack) {
		if (server != null) {
			server.accept(pack);
		}
		if (Unnamed.CLIENT) {
			if (client != null) {
				client.accept(pack);
			}
			LANGUAGES.asMap().forEach((s, c) -> {
				JLang lang = JLang.lang();
				for (Consumer<JLang> consumer : c) {
					consumer.accept(lang);
				}
				pack.addLang(id(s), lang);
			});
		}
	}
}
