package com.nice.avishkar.util;

import com.nice.avishkar.entities.MasterDataFeed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoringUtils {

    public static int getWeightedScore(MasterDataFeed primaryUser,
                                 MasterDataFeed possibleFriend,
                                 Map<String, Integer> attributes) {
        //System.out.println(primaryUser);
        //System.out.println(possibleFriend);
        int score = 0;

        for( Map.Entry<String, Integer> e : attributes.entrySet() ) {
            int count = -1;
            switch(e.getKey()) {
                case "Current organization":
                    if( primaryUser.getCurrentOrgs().equalsIgnoreCase(possibleFriend.getCurrentOrgs()) ) {
                        score = score + e.getValue();
                    }
                    break;
                case "School":
                    count = getCommonCount(primaryUser.getSchools(), possibleFriend.getSchools());
                    for( int i = 0; i < count; i++ ) {
                        score = score + e.getValue();
                    }
                    break;

                case "Interests":
                    count = getCommonCount(primaryUser.getInterests(), possibleFriend.getInterests());
                    for( int i = 0; i < count; i++ ) {
                        score = score + e.getValue();
                    }
                    break;

                case "City":
                    count = getCommonCount(primaryUser.getCities(), possibleFriend.getCities());
                    for( int i = 0; i < count; i++ ) {
                        score = score + e.getValue();
                    }
                    break;

                case "College":
                    count = getCommonCount(primaryUser.getColleges(), possibleFriend.getColleges());
                    for( int i = 0; i < count; i++ ) {
                        score = score + e.getValue();
                    }
                    break;

                case "Past organization":
                    count = getCommonCount(primaryUser.getPastOrgs(), possibleFriend.getPastOrgs());
                    for( int i = 0; i < count; i++ ) {
                        score = score + e.getValue();
                    }
                    break;
            }
        }

        return score;
    }

    private static int getCommonCount(List<String> list1,
                               List<String> list2) {
        if( null != list1 && null != list2 ) {
            ArrayList l1 = new ArrayList(list1);
            ArrayList l2 = new ArrayList(list2);
            l1.retainAll(l2);
            return l1.size();
        }
        return 0;
    }

}
