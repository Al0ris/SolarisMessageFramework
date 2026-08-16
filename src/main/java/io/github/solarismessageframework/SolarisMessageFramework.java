package io.github.solarismessageframework;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.plugin.java.JavaPlugin;

public final class SolarisMessageFramework {

    public static void init(JavaPlugin plugin) {
        MessageConfig.init(plugin);
    }

    public static void registerMessage(MessageKey key) {
        MessageConfig.registerMessage(key);
    }

    public static Component render(MessageKey key, TagResolver... placeholders) {
        return MessageConfig.render(key, placeholders);
    }

    public static void send(Audience audience, MessageKey key, TagResolver... placeholders) {
        MessageConfig.send(audience, key, placeholders);
    }

    public static void reload() {
        MessageConfig.reload();
    }
}
