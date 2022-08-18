package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface ExistingConnectionsDao {
    List<String[]> getAllExistingConnections(Path path) throws IOException;
}
