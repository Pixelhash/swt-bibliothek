package de.swt.bibliothek.config;

import java.io.File;

public interface DatabaseConfig {

    String type();

    String user();

    String password();

    String host();

    int port();

    String name();

    String dbPath();
}
