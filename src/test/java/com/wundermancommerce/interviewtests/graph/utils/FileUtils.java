package com.wundermancommerce.interviewtests.graph.utils;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

public class FileUtils {

    public static File getFile(String filename) throws URISyntaxException {
        URI resource = FileUtils.class.getClassLoader().getResource(filename).toURI();
        return new File(resource);
    }
}
