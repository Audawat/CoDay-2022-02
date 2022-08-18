package com.nice.avishkar.service;

import com.nice.avishkar.entities.MasterDataFeed;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public interface MasterDataFeedService {
    Map<String, MasterDataFeed> getAllMasterDataFeed(List<String> userIds, Path path) throws IOException;
}
