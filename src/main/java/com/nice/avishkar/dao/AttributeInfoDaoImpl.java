package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class AttributeInfoDaoImpl implements AttributeInfoDao {

    @Override
    public List<String[]> getAllAttributeInfo(final Path path) throws IOException {
        return DaoUtils.getData(path);
    }
}
