package com.nice.avishkar.service;

import com.nice.avishkar.Suggestion;
import com.nice.avishkar.entities.MasterDataFeed;
import com.nice.avishkar.util.ScoringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FriendsSuggestionServiceImpl implements FriendsSuggestionService {

    @Override
    public List<Suggestion> getFriendsSuggestions(final String userId,
                                                  final int maxSuggestions,
                                                  final Set<String> possibleFriends,
                                                  final Map<String, Integer> allAttributes,
                                                  final Map<String, MasterDataFeed> allMasterDataFeed) {
        List<Suggestion> suggestions = new ArrayList<>();
        populateWeightedSuggestions(userId, suggestions, allAttributes, possibleFriends, allMasterDataFeed);
        getSortedMaxSuggestions(suggestions, maxSuggestions);
        return suggestions;
    }

    private void getSortedMaxSuggestions(List<Suggestion> suggestions,
                                         int maxSuggestions) {
        //Comparator to sort candidates as per criteria
        Comparator<Suggestion> comparator = (suggestion1, suggestion2) -> {
            if( suggestion2.getScore() > suggestion1.getScore() ) {
                return 1;
            } else if( suggestion2.getScore() < suggestion1.getScore() ) {
                return -1;
            } else {
                return suggestion1.getName().compareTo(suggestion2.getName());
            }
        };

        //sorting suggestions
        suggestions.sort(comparator);
        //extract required suggestions count
        suggestions = suggestions.stream().limit(maxSuggestions).collect(Collectors.toList());
    }

    private void populateWeightedSuggestions(final String id,
                                             final List<Suggestion> suggestions,
                                             final Map<String, Integer> allAttributes,
                                             final Set<String> possibleFriends,
                                             final Map<String, MasterDataFeed> allMasterDataFeed) {
        possibleFriends.forEach(friend -> {
            MasterDataFeed friendData = allMasterDataFeed.get(friend);
            //System.out.println("Calling Score for:" + id + " friend:" + friend);
            int score = ScoringUtils.getWeightedScore(allMasterDataFeed.get(id), friendData, allAttributes);
            if( score > 0 ) {
                Suggestion suggestion = new Suggestion(friend, score, friendData.getFullName());
                suggestions.add(suggestion);
            }
        });
    }
}
