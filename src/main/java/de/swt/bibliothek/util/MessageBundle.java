package de.swt.bibliothek.util;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public class MessageBundle {

    private ResourceBundle messages;

    public MessageBundle() {
        this.messages = ResourceBundle.getBundle("localization/messages", Locale.GERMAN);
    }

    public String get(String message) {
        return messages.getString(message);
    }

    public final String get(final String key, final Object... args) {
        return MessageFormat.format(get(key), args);
    }
}
