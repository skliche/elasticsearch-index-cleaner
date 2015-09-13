package com.kliche.elasticsearch.cleaer;

import java.io.IOException;
import java.util.List;

public interface ElasticsearchClient {
	/**
	 * Returns a list of all indices present in the elasticsearch cluster.
	 * @return a list of all indices
	 * @throws IOException
	 */
	public List<String> findAllIndices() throws IOException;
	
	/**
	 * Uses the REST API to delete the specified index.
	 * @param index to be deleted
	 * @throws IOException
	 */
	public void deleteInex(String index) throws IOException;
	/**
	 * Uses the REST API to flush and close an index.
	 * @param index the index to be archived
	 * @throws IOException
	 */
	public void archiveIndex(String index) throws IOException;
	
	/** 
	 * Uses the REST API to optimize the index.
	 * @param index the index to be optimized
	 * @throws IOException
	 */
	public void optimize(String index) throws IOException;
}
