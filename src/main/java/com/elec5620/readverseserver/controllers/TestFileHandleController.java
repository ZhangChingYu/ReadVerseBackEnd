package com.elec5620.readverseserver.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class TestFileHandleController {
    @GetMapping("root_path")
    public String rootPath(){
        return System.getProperty("user.dir");
    }

    @PostMapping("test_upload_ebook")
    public String uploadEBook(@RequestParam("id") Long id, @RequestParam("data")MultipartFile file){
        if (file.isEmpty()) {
            return "Please select a file to upload.";
        } else {
            return file.getOriginalFilename()+":"+id+":"+file.getSize();
        }
    }

}
