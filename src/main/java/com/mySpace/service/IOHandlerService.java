package com.mySpace.service;

import com.mySpace.shared.constants.urlConstants.OperationStatusCode;
import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import jakarta.servlet.ServletOutputStream;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipOutputStream;

public interface IOHandlerService {
    Map<String, DownloadLinkDataViewModel> downloadDataMap = new HashMap<>();
    DownloadLinkDataViewModel getDownloadData(String id);
    DownloadLinkDataViewModel generateDownloadDetails(String[] fileNames, String archiveName);
    File getFile(String fileName);
    ZipOutputStream getZip(String[] fileNames, ServletOutputStream outputStream);
    OperationStatusCode saveFiles(MultipartFile[] files, String location);
    OperationStatusCode saveFile(MultipartFile file,String location);
    DownloadLinkDataViewModel generateDownloadDetail(MultipartFile[] files, String archiveName);
}
