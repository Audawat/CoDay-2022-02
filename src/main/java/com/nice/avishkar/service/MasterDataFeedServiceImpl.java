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

    private MasterDataFeedDao masterDataFeedDao;

    public MasterDataFeedServiceImpl(final MasterDataFeedDao masterDataFeedDao) {
        this.masterDataFeedDao = masterDataFeedDao;
    }

    @Override
    public Map<String, MasterDataFeed> getAllMasterDataFeed(List<String> userIds, final Path path) throws IOException {
        List<String[]> allMasterDataFeed = masterDataFeedDao.getAllMasterDataFeed(path);
        Map<String, MasterDataFeed> masterData = new HashMap<>();
        if( null != allMasterDataFeed ) {
            allMasterDataFeed.stream().forEach(p -> {
                //System.out.println("Getting Data for: " + p[0]);
                if(userIds.contains(p[0])) {
                    MasterDataFeed masterDataFeed = getMasterDataFeed(p);
                    //System.out.println("Got master data:" + masterDataFeed);
                    masterData.put(masterDataFeed.getId(), masterDataFeed);
                }
            });
        } else {
            System.out.println("No Master data found");
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
            String[] values = value.split("\\|");
            return Arrays.asList(values);
        }

        return null;
    }
}
