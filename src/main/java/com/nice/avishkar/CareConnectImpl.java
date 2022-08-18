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
import java.util.List;
import java.util.Map;

public class CareConnectImpl implements CareConnect {

    @Override
    public List<Suggestion> getSuggestions(String id,
                                           int maxConnectionDegree,
                                           int maxSuggestions,
                                           Path attributeInfoFilePath,
                                           Path existingConnectionsFilePath,
                                           Path masterDataFeedFilePath) {

        List<Suggestion> suggestions = null;

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
            Map<String, MasterDataFeed> allMasterDataFeed = masterDataFeedService.getAllMasterDataFeed(masterDataFeedFilePath);
            existingConnectionsService.getAllExistingConnectionsForUser(existingConnectionsFilePath, id, maxConnectionDegree);


        } catch(IOException e) {
            String errorMessage = MessageFormat.format("Exception Occurred during Processing input file. Message: {0}",
                                                       e.getMessage());
            System.err.println(errorMessage);
            e.printStackTrace();
        }

        return suggestions;
    }
}
