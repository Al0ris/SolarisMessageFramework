package io.github.solarismessageframework;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

final class MessageConfig {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();
    private static final Map<String, String> overrides = new HashMap<>();
    private static final Map<String, MessageKey> registered = new HashMap<>();

    private static JavaPlugin plugin;
    private static File configFile;
    private static YamlConfiguration config;

    public static void init(JavaPlugin plugin) {
        MessageConfig.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public static void registerMessage(MessageKey key) {
        if (plugin == null) {
            throw new IllegalStateException(
                    "SolarisMessageFramework.init() must be called before registerMessage()");
        }

        registered.put(key.id(), key);

        if (!config.contains(key.id())) {
            config.set(key.id(), key.defaultTemplate());
            saveConfig();
        }

        overrides.put(key.id(), config.getString(key.id()));
    }

    public static Component render(MessageKey key, TagResolver... placeholders) {
        String template = overrides.getOrDefault(key.id(), key.defaultTemplate());
        return MINI_MESSAGE.deserialize(template, placeholders);
    }

    public static void send(Audience audience, MessageKey key, TagResolver... placeholders) {
        audience.sendMessage(render(key, placeholders));
    }

    public static void reload() {
        if (plugin == null) {
            throw new IllegalStateException(
                    "SolarisMessageFramework.init() must be called before reload()");
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        for (MessageKey key : registered.values()) {
            if (!config.contains(key.id())) {
                config.set(key.id(), key.defaultTemplate());
            }
            overrides.put(key.id(), config.getString(key.id()));
        }

        saveConfig();
    }

    private static void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save messages.yml: " + e.getMessage());
        }
    }

}
