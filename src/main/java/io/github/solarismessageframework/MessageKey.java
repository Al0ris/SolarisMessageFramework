package io.github.solarismessageframework;

public record MessageKey(
        String id,
        String defaultTemplate
) {

    public static MessageKey of(String id, String defaultTemplate) {
        return new MessageKey(id, defaultTemplate);
    }
}
