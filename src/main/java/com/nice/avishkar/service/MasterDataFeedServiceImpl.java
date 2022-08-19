package com.nice.avishkar.service;

import com.nice.avishkar.dao.MasterDataFeedDao;
import com.nice.avishkar.entities.MasterDataFeed;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterDataFeedServiceImpl implements MasterDataFeedService {

    public static final String VALUE_SEPARATOR = "\\|";
    private MasterDataFeedDao masterDataFeedDao;

    public MasterDataFeedServiceImpl(final MasterDataFeedDao masterDataFeedDao) {
        this.masterDataFeedDao = masterDataFeedDao;
    }

    @Override
    public Map<String, MasterDataFeed> getMasterDataFeedForUsers(List<String> userIds, final Path masterDataFilePath) throws IOException {
        List<String[]> allMasterDataFeed = masterDataFeedDao.getMasterDataFeed(masterDataFilePath);
        Map<String, MasterDataFeed> masterData = new HashMap<>();
        if( null != allMasterDataFeed ) {
            allMasterDataFeed.stream().forEach(p -> {
                if(userIds.contains(p[0])) {
                    MasterDataFeed masterDataFeed = getMasterDataFeed(p);
                    masterData.put(masterDataFeed.getId(), masterDataFeed);
                }
            });
        } else {
            System.err.println("No Master data found");
        }

        return masterData;
    }

    private MasterDataFeed getMasterDataFeed(final String[] p) {
        MasterDataFeed masterDataFeed = new MasterDataFeed();
        masterDataFeed.setId(p[0]);
        masterDataFeed.setFullName(p[1]);
        masterDataFeed.setCities(getEntities(p[4]));
        masterDataFeed.setSchools(getEntities(p[5]));
        masterDataFeed.setColleges(getEntities(p[6]));
        masterDataFeed.setCurrentOrgs(p[7]);
        masterDataFeed.setPastOrgs(getEntities(p[8]));
        masterDataFeed.setInterests(getEntities(p[9]));
        return masterDataFeed;
    }

    List<String> getEntities(String value) {
        if( null != value ) {
            String[] values = value.split(VALUE_SEPARATOR);
            return Arrays.asList(values);
        }

        return null;
    }
}
