package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class ExistingConnectionsDaoImpl implements ExistingConnectionsDao {

    @Override
    public List<String[]> getAllExistingConnections(final Path path) throws IOException {
        return DaoUtils.getData(path);
    }
}
