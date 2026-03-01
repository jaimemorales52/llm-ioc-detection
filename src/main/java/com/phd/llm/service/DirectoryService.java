package com.phd.llm.service;

import java.io.IOException;

public interface DirectoryService {
	
	public void explore(String directoryPath, String llm, boolean ia) throws IOException;

	void exploreJSON(String directoryPath, String llm, boolean ia) throws IOException;
	
}
