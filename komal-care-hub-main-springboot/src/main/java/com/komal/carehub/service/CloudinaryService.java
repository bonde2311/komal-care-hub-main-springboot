package com.komal.carehub.service;

import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

public interface CloudinaryService {
    Map<String, Object> upload(MultipartFile file);
    Map<String, Object> delete(String id);
}

