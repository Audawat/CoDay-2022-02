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
import com.nice.avishkar.service.FriendsSuggestionService;
import com.nice.avishkar.service.FriendsSuggestionServiceImpl;
import com.nice.avishkar.service.MasterDataFeedService;
import com.nice.avishkar.service.MasterDataFeedServiceImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CareConnectImpl implements CareConnect {

    AttributeInfoDao attributeInfoDao;
    ExistingConnectionsDao existingConnectionsDao;
    MasterDataFeedDao masterDataFeedDao;

    AttributeInfoService attributeInfoService;
    ExistingConnectionsService existingConnectionsService;
    MasterDataFeedService masterDataFeedService;
    FriendsSuggestionService friendsSuggestionService;

    public CareConnectImpl() {
        //Creating Dao's
        attributeInfoDao = new AttributeInfoDaoImpl();
        existingConnectionsDao = new ExistingConnectionsDaoImpl();
        masterDataFeedDao = new MasterDataFeedDaoImpl();

        //Creating all required Services
        attributeInfoService = new AttributeInfoServiceImpl(attributeInfoDao);
        existingConnectionsService = new ExistingConnectionsServiceImpl(existingConnectionsDao);
        masterDataFeedService = new MasterDataFeedServiceImpl(masterDataFeedDao);
        friendsSuggestionService = new FriendsSuggestionServiceImpl();
    }

    @Override
    public List<Suggestion> getSuggestions(String id,
                                           int maxConnectionDegree,
                                           int maxSuggestions,
                                           Path attributeInfoFilePath,
                                           Path existingConnectionsFilePath,
                                           Path masterDataFeedFilePath) {

        List<Suggestion> suggestions = new ArrayList<>();

        try {
            //Getting Attributes
            Map<String, Integer> allAttributes = attributeInfoService.getAllAttributes(attributeInfoFilePath);

            //Getting possible friends set
            Set<String> possibleFriends = existingConnectionsService.getPossibleFriendsSuggestion(id,
                                                                                                  maxConnectionDegree,
                                                                                                  existingConnectionsFilePath);

            //Get master data for possible friends
            Map<String, MasterDataFeed> allMasterDataFeed = getStringMasterDataFeedMapForPossibleFriends(id,
                                                                                                         masterDataFeedFilePath,
                                                                                                         masterDataFeedService,
                                                                                                         possibleFriends);

            //populating suggested friends
            suggestions = friendsSuggestionService.getFriendsSuggestions(id,
                                                                         maxSuggestions,
                                                                         possibleFriends,
                                                                         allAttributes,
                                                                         allMasterDataFeed);
        } catch(IOException e) {
            String errorMessage = MessageFormat.format("Exception Occurred during Processing input file. Message: {0}",
                                                       e.getMessage());
            System.err.println(errorMessage);
            e.printStackTrace();
        }

        return suggestions;
    }

    private Map<String, MasterDataFeed> getStringMasterDataFeedMapForPossibleFriends(final String id,
                                                                                     final Path masterDataFeedFilePath,
                                                                                     final MasterDataFeedService masterDataFeedService,
                                                                                     final Set<String> possibleFriends)
            throws IOException {
        List<String> userDataToGet = new ArrayList<>(possibleFriends);
        userDataToGet.add(id);
        return masterDataFeedService.getAllMasterDataFeed(userDataToGet, masterDataFeedFilePath);
    }
}
