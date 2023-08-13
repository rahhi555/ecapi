package com.example.ecapi.controller.storage;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public class StorageController {
    @PostMapping("/products/storage")
    public void addStorage(@RequestParam MultipartFile file) {

    }
}
