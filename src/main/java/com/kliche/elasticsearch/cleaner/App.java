package com.kliche.elasticsearch.cleaner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class App {
	public static final Logger LOG = LoggerFactory.getLogger(App.class);
	
    public static void main( String[] args ) throws IOException, ParseException {
    	CleanerConfig config = getConfig();
        
        Cleaner cleaner = new Cleaner(config);
        cleaner.clean();
    }

	private static CleanerConfig getConfig() throws FileNotFoundException {
    	Yaml yamlConfig = new Yaml(new Constructor(CleanerConfig.class));
    	
    	CleanerConfig config = (CleanerConfig) yamlConfig.load(new FileInputStream(new File("config.yaml")));
		return config;
	}

	
}
