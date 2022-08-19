package com.nice.avishkar;

import com.nice.avishkar.dao.AttributeInfoDao;
import com.nice.avishkar.dao.AttributeInfoDaoImpl;
import com.nice.avishkar.dao.ExistingConnectionsDao;
import com.nice.avishkar.dao.ExistingConnectionsDaoImpl;
import com.nice.avishkar.dao.MasterDataFeedDao;
import com.nice.avishkar.dao.MasterDataFeedDaoImpl;
import com.nice.avishkar.entities.MasterDataFeed;
import com.nice.avishkar.service.AttributeInfoService;
import com.nice.avishkar.service.AttributeInfoServiceImpl;
import com.nice.avishkar.service.ExistingConnectionsService;
import com.nice.avishkar.service.ExistingConnectionsServiceImpl;
import com.nice.avishkar.service.MasterDataFeedService;
import com.nice.avishkar.service.MasterDataFeedServiceImpl;
import com.nice.avishkar.util.ScoringUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CareConnectImpl implements CareConnect {

    @Override
    public List<Suggestion> getSuggestions(String id,
                                           int maxConnectionDegree,
                                           int maxSuggestions,
                                           Path attributeInfoFilePath,
                                           Path existingConnectionsFilePath,
                                           Path masterDataFeedFilePath) {

        List<Suggestion> suggestions = new ArrayList<>();

        //Creating Dao's
        AttributeInfoDao attributeInfoDao = new AttributeInfoDaoImpl();
        ExistingConnectionsDao existingConnectionsDao = new ExistingConnectionsDaoImpl();
        MasterDataFeedDao masterDataFeedDao = new MasterDataFeedDaoImpl();

        //Creating all required Services
        AttributeInfoService attributeInfoService = new AttributeInfoServiceImpl(attributeInfoDao);
        ExistingConnectionsService existingConnectionsService = new ExistingConnectionsServiceImpl(existingConnectionsDao);
        MasterDataFeedService masterDataFeedService = new MasterDataFeedServiceImpl(masterDataFeedDao);

        // impl
        populateSuggestions(id,
                            maxConnectionDegree,
                            maxSuggestions,
                            attributeInfoFilePath,
                            existingConnectionsFilePath,
                            masterDataFeedFilePath,
                            suggestions,
                            existingConnectionsDao,
                            attributeInfoService,
                            existingConnectionsService,
                            masterDataFeedService);

        return suggestions;
    }

    private void populateSuggestions(final String id,
                                     int maxConnectionDegree,
                                     int maxSuggestions,
                                     Path attributeInfoFilePath,
                                     Path existingConnectionsFilePath,
                                     Path masterDataFeedFilePath,
                                     List<Suggestion> suggestions,
                                     ExistingConnectionsDao existingConnectionsDao,
                                     AttributeInfoService attributeInfoService,
                                     ExistingConnectionsService existingConnectionsService,
                                     MasterDataFeedService masterDataFeedService) {
        try {
            //Getting Attributes
            Map<String, Integer> allAttributes = attributeInfoService.getAllAttributes(attributeInfoFilePath);

            // Getting possible friend suggestions
            Set<String> possibleFriends = existingConnectionsService.getPossibleFriendsSuggestion(id,
                                                                                                  maxConnectionDegree,
                                                                                                  existingConnectionsFilePath);
            //System.out.println(possibleFriends);

            //Get master data
            List<String> userDataToGet = new ArrayList<>(possibleFriends);
            userDataToGet.add(id);
            Map<String, MasterDataFeed> allMasterDataFeed = masterDataFeedService.getAllMasterDataFeed(userDataToGet,
                                                                                                       masterDataFeedFilePath);
            //System.out.println("All MAster Data" + allMasterDataFeed);
            // calculate scores
            populateWeightedSuggestions(id, suggestions, allAttributes, possibleFriends, allMasterDataFeed);

            getSortedMaxSuggestions(suggestions, maxSuggestions);
        } catch(IOException e) {
            String errorMessage = MessageFormat.format("Exception Occurred during Processing input file. Message: {0}",
                                                       e.getMessage());
            System.err.println(errorMessage);
            e.printStackTrace();
        }
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
