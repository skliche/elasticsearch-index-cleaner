package com.kliche.elasticsearch.cleaner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Cleaner {
	public static final Logger LOG = LoggerFactory.getLogger(Cleaner.class.getName());
	
	private ElasticsearchClient client;
	private CleanerConfig config;
	private DateTimeFormatter fmt;
	
	public Cleaner(CleanerConfig config) {
		this.config = config;
		client = new ElasticsearchClientImpl(config);
		fmt = DateTimeFormat.forPattern(config.getDateFormat());
	}
	
	public void clean() throws IOException, ParseException {
		LOG.info("Starting cleanup job...");
		
        List<String> indices = client.findAllIndices();
        LOG.info("Found the following indices: " + indices);
        
        for (String index : indices) {
        	// we don't want an index to be handled twice
        	// therefore we track if it was already handled
        	boolean wasHandled = false;
        	
        	// handle deletions
			if(indexMatchesPatternAndInterval(index, config.getMaxAgeToDelete())) {
				LOG.info("About to delete " + index);
				client.deleteInex(index);
				wasHandled = true;
			}
			
			// handle archiving
			if(!wasHandled && indexMatchesPatternAndInterval(index, config.getMaxAgeToArchive())) {
				LOG.info("About to archive " + index);
				client.archiveIndex(index);
				wasHandled = true;
			}
			
			// handle optimization
			if(!wasHandled && indexMatchesPatternAndInterval(index, config.getMaxAgeToOptimize())) {
				LOG.info("About to optimize " + index);
				client.optimize(index);
				wasHandled = true;
			}
		}
        
        LOG.info("Finished cleanup job...");
	}
	
	private boolean indexMatchesPatternAndInterval(String candidateIndex, int days) {
		List<String> indicesInConfig = config.getIndexpattern();
		for (String configIndex : indicesInConfig) {
			if(candidateIndex.startsWith(configIndex)) {
				// we have a candidate now check the date
				LOG.debug(candidateIndex + " is a index pattern registered in the config.");
				// build the rest of the string
				String datePart = candidateIndex.substring(configIndex.length());
				
				// parse the date
				DateTime indexDate;
				try {
					indexDate = fmt.parseDateTime(datePart);
				} catch (IllegalArgumentException e) {
					LOG.error("Could not parse date part of index " + candidateIndex);
					return false;
				}
				
				// check if it is in the right interval
				if(isOlderThan(days, indexDate)) {
					LOG.info(candidateIndex + " is older than " + days + " days. It will be handled.");
					return true;
				}
			}
		}
		return false;
	}

	private boolean isOlderThan(int days, DateTime dateToValidate) {
		return dateToValidate.isBefore(new DateTime().minusDays(days));
	}
}
