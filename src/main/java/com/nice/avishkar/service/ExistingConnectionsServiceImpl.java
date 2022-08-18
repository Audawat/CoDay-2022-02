package com.nice.avishkar.service;

import com.nice.avishkar.dao.ExistingConnectionsDao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExistingConnectionsServiceImpl implements ExistingConnectionsService {

    Map<String, Set<String>> relations = new HashMap<>();
    Set<String> possibleFriends = new HashSet<>();

    public ExistingConnectionsServiceImpl(final ExistingConnectionsDao existingConnectionsDao) {
        this.existingConnectionsDao = existingConnectionsDao;
    }

    private ExistingConnectionsDao existingConnectionsDao;

    @Override
    public void getAllExistingConnectionsForUser(final Path path,
                                                 final String userId,
                                                 final int maxConnectionDegree) throws IOException {
        List<String[]> allExistingConnections = existingConnectionsDao.getAllExistingConnections(path);

        if( null != allExistingConnections ) {
            allExistingConnections.stream().forEach(p -> {
                String node1 = p[0];
                String node2 = p[1];
                populateRelations(relations, node1, node2);
                populateRelations(relations, node2, node1);
            });

            Set<String> friends = relations.get(userId);
            Set<String> parentIds = new HashSet<>();
            parentIds.add(userId);

            getSuggestions(userId, friends, parentIds, maxConnectionDegree, 1);
            possibleFriends.removeAll(friends);
            possibleFriends.remove(userId);
            System.out.println(possibleFriends);
        } else {
            System.out.println("No Existing Connections data found");
        }
    }

    private void getSuggestions(String currentUser,
                                Set<String> friends,
                                Set<String> parentIds,
                                int maxConnectionDegree,//2
                                int counter) {//0
        if( counter >= maxConnectionDegree ) {
            return;
        }
        Iterator<String> iterator = friends.iterator();
        int cnt = 1;
        while( iterator.hasNext() ) {
            String nextFriend = iterator.next();

            if( cnt == 1 ) {
                counter = counter + 1;
            }
            if( !parentIds.contains(nextFriend) ) {
                parentIds.add(nextFriend);
                possibleFriends.addAll(relations.get(nextFriend));
                getSuggestions(nextFriend, relations.get(nextFriend), parentIds, maxConnectionDegree, counter);
            }
            cnt++;
        }
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
}
