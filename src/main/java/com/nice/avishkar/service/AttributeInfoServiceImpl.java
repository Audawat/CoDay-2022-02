package com.nice.avishkar.service;

import com.nice.avishkar.dao.AttributeInfoDao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttributeInfoServiceImpl implements AttributeInfoService {

    private AttributeInfoDao attributeInfoDao;

    public AttributeInfoServiceImpl(AttributeInfoDao attributeInfoDao) {
        this.attributeInfoDao = attributeInfoDao;
    }

    @Override
    public Map<String, Integer> getAllAttributes(final Path path) throws IOException {
        Map<String, Integer> attributes = new HashMap<>();
        List<String[]> allAttributeInfo = attributeInfoDao.getAllAttributeInfo(path);

        if( null != allAttributeInfo ) {
            allAttributeInfo.forEach(attribute -> attributes.put(attribute[0], Integer.parseInt(attribute[1])));
        } else {
            System.out.println("No Attributes data found");
        }

        return attributes;
    }
}
