package com.nice.avishkar.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ExistingConnectionsService {

//    void getAllExistingConnectionsForUser(Path path,
//                                          final String id,
//                                          final int maxConnectionDegree) throws IOException;
    Map<String, Set<String>> getConnectionsMap(final List<String[]> allExistingConnections);
    Set<String> getPossibleFriendsSuggestion(final String id,
                                             final int maxConnectionDegree,
                                             final Map<String, Set<String>> connectionsMap);


}
