package com.nice.avishkar;

import java.nio.file.Path;
import java.util.List;

public interface CareConnect {
	
	public List<Suggestion> getSuggestions(String id, int maxConnectionDegree, int maxSuggestions, Path attributeInfoFilePath, Path existingConnectionsFilePath, Path masterDataFeedFilePath);
	
}
