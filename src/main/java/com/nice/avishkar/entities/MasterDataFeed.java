package com.nice.avishkar.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
public class MasterDataFeed {

    String id;
    String fullName;
    List<String> cities;
    List<String> schools;
    List<String> colleges;
    List<String> pastOrgs;
    List<String> interests;
    String currentOrgs;
}
