package com.album_de_mama.back_end.storage.service;

import org.springframework.core.io.Resource;

public interface MediaStorageService {

    Resource load(String storageKey);
}
