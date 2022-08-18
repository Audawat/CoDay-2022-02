package com.nice.avishkar.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface AttributeInfoDao {
    List<String[]> getAllAttributeInfo(Path path) throws IOException;
}
