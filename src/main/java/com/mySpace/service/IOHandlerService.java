package com.mySpace.service;

import com.mySpace.shared.constants.urlConstants.OperationStatusCode;
import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import jakarta.servlet.ServletOutputStream;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.util.zip.ZipOutputStream;

public interface IOHandlerService {
    DownloadLinkDataViewModel generateDownloadDetails(String[] fileNames);
    File getFile(String fileName);
    ZipOutputStream getZip(String[] fileNames, ServletOutputStream outputStream);
    OperationStatusCode saveFiles(MultipartFile[] files, String location);
    OperationStatusCode saveFile(MultipartFile file,String location);
    DownloadLinkDataViewModel generateDownloadDetail(MultipartFile[] files);
}
