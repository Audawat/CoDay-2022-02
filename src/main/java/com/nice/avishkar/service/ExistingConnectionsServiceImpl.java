package com.nice.avishkar.service;

import com.nice.avishkar.dao.ExistingConnectionsDao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExistingConnectionsServiceImpl implements ExistingConnectionsService {

    private final ExistingConnectionsDao existingConnectionsDao;
    Map<String, Set<String>> relations = new HashMap<>();

    public ExistingConnectionsServiceImpl(final ExistingConnectionsDao existingConnectionsDao) {
        this.existingConnectionsDao = existingConnectionsDao;
    }

    @Override
    public Set<String> getPossibleFriends(final String id,
                                          final int maxConnectionDegree,
                                          final Path existingConnectionsFilePath) throws IOException {

        Map<String, Set<String>> existingConnections = getConnectionsMap(existingConnectionsFilePath);
        return getPossibleFriendsFromExistingConnections(id, maxConnectionDegree, existingConnections);
    }

    //Getting All Connections per User
    private Map<String, Set<String>> getConnectionsMap(final Path existingConnectionsFilePath) throws IOException {
        List<String[]> allExistingConnections = existingConnectionsDao.getAllExistingConnections(existingConnectionsFilePath);
        allExistingConnections.stream().forEach(p -> {
            String node1 = p[0];
            String node2 = p[1];
            populateRelations(relations, node1, node2);
            populateRelations(relations, node2, node1);
        });
        return relations;
    }

    private void populateRelations(final Map<String, Set<String>> relations,
                                   final String node1,
                                   final String node2) {
        if( relations.containsKey(node1) ) {
            relations.get(node1).add(node2);
        } else {
            Set<String> values = new HashSet<>();
            values.add(node2);
            relations.put(node1, values);
        }
    }

    //Get Immediate friends of provided users
    private List<String> getFriendsOfUsers(final Set<String> userIds,
                                           final Map<String, Set<String>> connectionsMap) {
        Set<String> friendsOfFriends = new HashSet<>();
        userIds.forEach(user -> {
            friendsOfFriends.addAll(connectionsMap.get(user));
        });

        return new ArrayList<>(friendsOfFriends);
    }

    private Set<String> getPossibleFriendsFromExistingConnections(final String userId,
                                                                  final int maxConnectionDegree,
                                                                  final Map<String, Set<String>> existingConnections) {
        Set<String> existingFriends = existingConnections.get(userId);
        Set<String> possibleFriends = null;
        if( null != existingFriends ) {
            possibleFriends = new HashSet<>(existingFriends);
            //Fetch friends as per maxConnectionDegree
            for( int level = 1; level < maxConnectionDegree; level++ ) {
                possibleFriends.addAll(getFriendsOfUsers(possibleFriends, existingConnections));
            }
            //Removing Direct connections and self
            possibleFriends.removeAll(existingFriends);
            possibleFriends.remove(userId);
        }
        return possibleFriends;
    }
}
