package com.nice.avishkar.service;

import com.nice.avishkar.entities.MasterDataFeed;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface MasterDataFeedService {
    Map<String, MasterDataFeed> getAllMasterDataFeed(Path path) throws IOException;
}
