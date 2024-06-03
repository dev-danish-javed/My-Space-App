package com.mySpace.ui.controller;


import com.mySpace.service.IOHandlerService;
import com.mySpace.shared.constants.urlConstants.ControllerPaths;
import com.mySpace.shared.constants.urlConstants.actionPaths.IO_HandlerPaths;
import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping(ControllerPaths.IO_HANDLER_PATH)
public class IOHandlerController {
    @Autowired
    private IOHandlerService ioService;

    @GetMapping(IO_HandlerPaths.DOWNLOAD)
    public ResponseEntity<Resource> download(@PathVariable String fileName) throws IOException {
        File file = ioService.getFile(fileName);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping(value = IO_HandlerPaths.DOWNLOAD_MULTIPLE, produces="application/zip")
    public void zipDownload(@PathVariable String archiveName,@PathVariable String[] fileNames, HttpServletResponse response) throws IOException {
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archiveName + "\"");
        ioService.getZip(fileNames,response.getOutputStream());
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping(IO_HandlerPaths.UPLOAD)
    public String receiveData(@RequestBody MultipartFile file){
        ioService.saveFile(file,null);
        return "working";
    }

    @PostMapping(IO_HandlerPaths.UPLOAD_MULTIPLE)
    public String receiveData(@RequestBody MultipartFile[] files){
        ioService.saveFiles(files,null);
        return "working";
    }

    @GetMapping(IO_HandlerPaths.DOWNLOAD_DETAILS)
    public DownloadLinkDataViewModel getDownloadDetails(@PathVariable String[] fileNames){
        return ioService.generateDownloadDetails(fileNames, "");
    }

    @PostMapping(IO_HandlerPaths.GENERATE_LINK)
    public DownloadLinkDataViewModel prepareDownloadDetails(@RequestBody MultipartFile[] files){
        System.out.println("Oh");
        return ioService.generateDownloadDetail(files, "");
    }

    @PostMapping(IO_HandlerPaths.GENERATE_LINK_V2)
    public DownloadLinkDataViewModel prepareDownloadDetailsV2(@RequestBody MultipartFile[] files, @PathVariable String archiveName){
        System.out.println("Yes");
        return ioService.generateDownloadDetail(files, archiveName);
    }

    @GetMapping("/closeApp")
    public void closeApp(){
        System.exit(1);
    }
}


