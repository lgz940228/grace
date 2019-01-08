package com.lgz.grace.api.utils.pojo;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lgz on 2019/1/8.
 */
public class FileMap extends HashMap<String, MultipartFile> {
    public FileMap(Map<? extends String, ? extends MultipartFile> m) {
        super(m);
    }

    public FileMap(int initialCapacity) {
        super(initialCapacity);
    }

    public FileMap() {

    }
}
