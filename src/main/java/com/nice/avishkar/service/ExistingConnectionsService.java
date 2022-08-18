package com.nice.avishkar.service;

import java.io.IOException;
import java.nio.file.Path;

public interface ExistingConnectionsService {

    void getAllExistingConnectionsForUser(Path path,
                                          final String id,
                                          final int maxConnectionDegree) throws IOException;

}
