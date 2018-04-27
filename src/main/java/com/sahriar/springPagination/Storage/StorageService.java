package com.sahriar.springPagination.Storage;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file);

    void init();
}
