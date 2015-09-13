package com.kliche.elasticsearch.cleaer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class ElasticsearchClientImpl implements ElasticsearchClient {
	public static final Logger LOG = LoggerFactory.getLogger(ElasticsearchClient.class.getName());

	private static final String GET_ALL_INDICES_URL = "/_aliases?ignore_missing=true";
	
	private static final String OPTIMIZE_URL = "/_optimize?max_num_segments=1";
	private static final String SETTINGS_URL = "/_settings?number_of_replicas=0";
	private static final String FLUSH_URL = "/_flush";
	private static final String CLOSE_URL = "/_close";
	
	private final CloseableHttpClient httpclient = HttpClients.createDefault();
	private CleanerConfig config;
	
	public ElasticsearchClientImpl(CleanerConfig config) {
		this.config = config;
	}

	public List<String> findAllIndices() throws IOException {
		List<String> result = new ArrayList<String>();
		
		HttpGet httpget = new HttpGet(buildConnectionUrl() + GET_ALL_INDICES_URL);
		CloseableHttpResponse response = httpclient.execute(httpget);
		try {
			if(response.getStatusLine().getStatusCode() == 200) { 
				
				HttpEntity entity = response.getEntity();
				
				String json = EntityUtils.toString(entity);
				Map<String, Object> jsonObject = new Gson().fromJson(json, Map.class);
				Set<String> indices = jsonObject.keySet();
				
				result.addAll(indices);
			} else {
				LOG.error("Could not retrieve the indices from elasticsearch. "
						+ "Please verify your config and elasticseach server.");
			}
		} finally {
		    response.close();
		}
		
		return result;
	}

	public void deleteInex(String index) throws ClientProtocolException, IOException {
		HttpDelete deleteOperation = new HttpDelete(buildConnectionUrl() + index);
		CloseableHttpResponse resp = httpclient.execute(deleteOperation);
		LOG.info("Delete operation status: " + resp.getStatusLine());
		resp.close();
	}

	public void archiveIndex(String index) throws IOException {
		HttpPost flushOperation = new HttpPost(buildConnectionUrl() + index + FLUSH_URL);
		CloseableHttpResponse resp = httpclient.execute(flushOperation);
		LOG.info("Flush operation status: " + resp.getStatusLine());
		resp.close();
		
		HttpPost closeOperation = new HttpPost(buildConnectionUrl() + index + CLOSE_URL);
		resp = httpclient.execute(closeOperation);
		LOG.info("Close operation status: " + resp.getStatusLine());
		resp.close();
	}
	
	@Override
	public void optimize(String index) throws IOException {
		HttpPut settingsOperation = new HttpPut(buildConnectionUrl() + index + SETTINGS_URL);
		CloseableHttpResponse resp = httpclient.execute(settingsOperation);
		LOG.info("Settings operation status: " + resp.getStatusLine());
		resp.close();
		
		HttpPost optimizeOperation = new HttpPost(buildConnectionUrl() + index + OPTIMIZE_URL);
		resp = httpclient.execute(optimizeOperation);
		LOG.info("Optimize operation status: " + resp.getStatusLine());
		resp.close();
	}
	
	private String buildConnectionUrl() {
		return new StringBuilder()
			.append(config.protocol).append("://")
			.append(config.hostname).append(":")
			.append(config.port)
			.append("/")
			.toString();
	}
}
