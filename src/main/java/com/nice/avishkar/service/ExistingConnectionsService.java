package com.nice.avishkar.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;

public interface ExistingConnectionsService {

    Set<String> getPossibleFriends(final String id,
                                   final int maxConnectionDegree,
                                   final Path existingConnectionsFilePath) throws IOException;
}
