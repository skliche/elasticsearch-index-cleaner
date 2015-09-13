package com.kliche.elasticsearch.cleaer;

import java.util.List;

public class CleanerConfig {
	String hostname;
	String port;
	String protocol;
	String dateFormat;
	
	int maxAgeToDelete;
	int maxAgeToArchive;
	int maxAgeToOptimize;
	
	List<String> indexpattern;
	
	private CleanerConfig(){
	}

	public int getMaxAgeToOptimize() {
		return maxAgeToOptimize;
	}

	public void setMaxAgeToOptimize(int maxAgeToOptimize) {
		this.maxAgeToOptimize = maxAgeToOptimize;
	}
	
	public int getMaxAgeToArchive() {
		return maxAgeToArchive;
	}

	public void setMaxAgeToArchive(int maxAgeToArchive) {
		this.maxAgeToArchive = maxAgeToArchive;
	}

	public int getMaxAgeToDelete() {
		return maxAgeToDelete;
	}

	public void setMaxAgeToDelete(int maxAgeToDelete) {
		this.maxAgeToDelete = maxAgeToDelete;
	}

	public List<String> getIndexpattern() {
		return indexpattern;
	}

	public void setIndexpattern(List<String> indexpattern) {
		this.indexpattern = indexpattern;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
}
