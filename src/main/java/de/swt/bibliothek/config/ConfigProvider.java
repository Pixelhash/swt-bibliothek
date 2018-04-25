package de.swt.bibliothek.config;

import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.ConfigurationSource;
import org.cfg4j.source.context.filesprovider.ConfigFilesProvider;
import org.cfg4j.source.files.FilesConfigurationSource;

import java.nio.file.Paths;
import java.util.Arrays;

public class ConfigProvider {

    public static ConfigurationProvider configurationProvider() {
        // Specify which files to load. Configuration from both files will be merged.
        ConfigFilesProvider configFilesProvider = () -> Arrays.asList(
            Paths.get(Paths.get(".").toAbsolutePath().normalize().toString(), "application.yaml"));

        // Use local files as configuration store
        ConfigurationSource source = new FilesConfigurationSource(configFilesProvider);

        // Create provider
        return new ConfigurationProviderBuilder()
            .withConfigurationSource(source)
            .build();
    }

}
