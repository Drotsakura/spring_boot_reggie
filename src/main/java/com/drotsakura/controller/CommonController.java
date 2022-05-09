package com.drotsakura.controller;

import com.drotsakura.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String pathImages;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        File pathFile = new File(pathImages);
        if (!pathFile.exists()){
            pathFile.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
        try {
            file.transferTo(new File(pathImages+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws Exception {
        File file = new File(pathImages + name);
        FileInputStream  inputStream = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;

        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("image/jpeg");

        while ((len = inputStream.read(bytes))!=-1){
            outputStream.write(bytes,0,len);
        }

        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }
}
