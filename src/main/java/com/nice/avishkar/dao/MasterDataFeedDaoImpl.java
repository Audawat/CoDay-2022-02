package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class MasterDataFeedDaoImpl implements MasterDataFeedDao {

    @Override
    public List<String[]> getMasterDataFeed(final Path path) throws IOException {
        return DaoUtils.getData(path);
    }
}
