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

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
        try {
            //Getting Attributes
            Map<String, Integer> allAttributes = attributeInfoService.getAllAttributes(attributeInfoFilePath);

            List<String[]> allExistingConnections = existingConnectionsDao.getAllExistingConnections(existingConnectionsFilePath);
            Map<String, Set<String>> connectionsMap = existingConnectionsService.getConnectionsMap(allExistingConnections);

            Set<String> possibleFriends = existingConnectionsService.getPossibleFriendsSuggestion(id, maxConnectionDegree, connectionsMap);
            //System.out.println(possibleFriends);

            //Get master data
            List<String> userDataToGet = new ArrayList<>(possibleFriends);
            userDataToGet.add(id);
            Map<String, MasterDataFeed> allMasterDataFeed = masterDataFeedService.getAllMasterDataFeed(userDataToGet, masterDataFeedFilePath);
            //System.out.println("All MAster Data" + allMasterDataFeed);
            // calculate scores
            possibleFriends.forEach( friend -> {
                MasterDataFeed friendData = allMasterDataFeed.get(friend);
                //System.out.println("Calling Score for:" + id + " friend:" + friend);
                int score = getWeightedScore(allMasterDataFeed.get(id), friendData, allAttributes);
                if(score > 0) {
                    Suggestion suggestion = new Suggestion(friend, score, friendData.getFullName());
                    suggestions.add(suggestion);
                }
            });

            //Comparator to sort candidates as per criteria
            Comparator<Suggestion> comparator = (suggestion1, suggestion2) -> {
                if (suggestion2.getScore() > suggestion1.getScore()) {
                    return 1;
                } else if (suggestion2.getScore() < suggestion1.getScore()) {
                    return -1;
                } else {
                    return suggestion1.getName().compareTo(suggestion2.getName());
                }
            };

            suggestions.sort(comparator);




        } catch(IOException e) {
            String errorMessage = MessageFormat.format("Exception Occurred during Processing input file. Message: {0}",
                                                       e.getMessage());
            System.err.println(errorMessage);
            e.printStackTrace();
        }

        return suggestions;
    }

    private int getWeightedScore(MasterDataFeed primaryUser,
                                 MasterDataFeed possibleFriend,
                                 Map<String, Integer> attributes) {
        //System.out.println(primaryUser);
        //System.out.println(possibleFriend);
        int score = 0;

        for( Map.Entry<String, Integer> e : attributes.entrySet()) {
            int count = -1;
            switch (e.getKey()) {
                case "Current organization" :
                    if(primaryUser.getCurrentOrgs().equalsIgnoreCase(possibleFriend.getCurrentOrgs())) {
                        score = score + e.getValue();
                    }
                    break;
                case "School" :
                    count = getCommonCount(primaryUser.getSchools(), possibleFriend.getSchools());
                    for(int i = 0; i < count; i++) {
                        score = score + e.getValue();
                    }
                    break;

                case "Interests" :
                    count = getCommonCount(primaryUser.getInterests(), possibleFriend.getInterests());
                    for(int i = 0; i < count; i++) {
                        score = score + e.getValue();
                    }
                    break;

                case "City" :
                    count = getCommonCount(primaryUser.getCities(), possibleFriend.getCities());
                    for(int i = 0; i < count; i++) {
                        score = score + e.getValue();
                    }
                    break;

                case "College" :
                    count = getCommonCount(primaryUser.getColleges(), possibleFriend.getColleges());
                    for(int i = 0; i < count; i++) {
                        score = score + e.getValue();
                    }
                    break;

                case "Past organization" :
                    count = getCommonCount(primaryUser.getPastOrgs(), possibleFriend.getPastOrgs());
                    for(int i = 0; i < count; i++) {
                        score = score + e.getValue();
                    }
                    break;
            }
        }

        return score;
    }

    private int getCommonCount(List<String> list1,
                               List<String> list2) {
        if(null != list1 && null != list2) {
            ArrayList l1 = new ArrayList(list1);
            ArrayList l2 = new ArrayList(list2);
            l1.retainAll(l2);
            return l1.size();
        }
        return 0;

    }


}
