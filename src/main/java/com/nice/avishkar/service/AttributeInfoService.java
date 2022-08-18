package com.nice.avishkar.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public interface AttributeInfoService {

    Map<String, Integer> getAllAttributes(Path path) throws IOException;

}
