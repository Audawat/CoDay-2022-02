package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface MasterDataFeedDao {
    List<String[]> getMasterDataFeed(Path path) throws IOException;
}
