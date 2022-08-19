package com.nice.avishkar.service;

import com.nice.avishkar.Suggestion;
import com.nice.avishkar.entities.MasterDataFeed;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendsSuggestionService {

    List<Suggestion> getFriendsSuggestions(String userId,
                                           int maxSuggestions,
                                           Set<String> possibleFriends,
                                           Path attributeInfoFilePath,
                                           Map<String, MasterDataFeed> allMasterDataFeed) throws IOException;
}
