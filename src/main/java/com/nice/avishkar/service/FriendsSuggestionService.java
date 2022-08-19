package com.nice.avishkar.service;

import com.nice.avishkar.Suggestion;
import com.nice.avishkar.entities.MasterDataFeed;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FriendsSuggestionService {

    List<Suggestion> getFriendsSuggestions(String userId,
                                           int maxSuggestions,
                                           Set<String> possibleFriends,
                                           Map<String, Integer> allAttributes,
                                           Map<String, MasterDataFeed> allMasterDataFeed);
}
