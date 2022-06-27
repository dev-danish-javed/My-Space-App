package com.mySpace.service.implementation;

import com.mySpace.service.IOHandlerService;
import com.mySpace.shared.constants.urlConstants.ControllerPaths;
import com.mySpace.shared.constants.urlConstants.OperationStatusCode;
import com.mySpace.shared.constants.urlConstants.actionPaths.IO_HandlerPaths;
import com.mySpace.shared.utils.FileUtils;
import com.mySpace.ui.model.response.DownloadLinkDataViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class IOHandlerServiceImplementation implements IOHandlerService {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FileUtils fileUtils;

    @Value("${upload-location}")
    String uploadLocation;

    @Value("${download-source-location}")
    String downloadSourceLocation;
    @Value("${base-location}")
    String baseLocation;
    @Override
    public DownloadLinkDataViewModel generateDownloadDetails(String[] fileNames) {
        String hostName = "";
        double fileLength = 0;
        String downloadUrl = "";
        String unit = "b";
        String downloadFileSize = "";
        String ip = (String) applicationContext.getBean("indexUrl");
        String fileNameParameterForRequest = "";


        for (String fileName : fileNames) {
            FileSystemResource resource = new FileSystemResource(baseLocation+downloadSourceLocation + fileName);
            try {
                fileLength += resource.contentLength();
                String path=resource.getPath();
                fileNameParameterForRequest += fileNameParameterForRequest == "" ? fileName : "," + fileName;
            } catch (IOException e) {

            }
        }
        if (fileLength / 1024 > 1) {
            fileLength = fileLength / 1024;
            unit = "kb";
        }
        if (fileLength / 1024 > 1) {
            fileLength = fileLength / 1024;
            unit = "mb";
        }
        if (fileLength / 1024 > 1) {
            fileLength = fileLength / 1024;
            unit = "GB";
        }
        DecimalFormat decimalFormat = new DecimalFormat("#0.00");
        downloadFileSize = decimalFormat.format(fileLength) + " " + unit;
        String downloadFileName = "";
        if (fileNames.length > 1) {
            downloadUrl = ip + ControllerPaths.IO_HANDLER_PATH + IO_HandlerPaths.DOWNLOAD_MULTIPLE.replaceAll("\\{fileNames\\}", fileNameParameterForRequest);

            downloadFileName = "My Space Download.zip";
        } else {
            downloadUrl = ip + ControllerPaths.IO_HANDLER_PATH + IO_HandlerPaths.DOWNLOAD.replaceAll("\\{fileName\\}", fileNames[0]);
            downloadFileName = fileNames[0];
        }
        return new DownloadLinkDataViewModel(downloadUrl, downloadFileSize, downloadFileName);
    }

    @Override
    public File getFile(String fileName) {
        return new File(uploadLocation + fileName);
    }

    @Override
    public ZipOutputStream getZip(String[] fileNames, ServletOutputStream outputStream) {
        fileUtils.createDirectoryIfDoesNotExist(uploadLocation);
        ZipOutputStream zipOut = new ZipOutputStream(outputStream);
        try {
            for (String fileName : fileNames) {
                FileSystemResource resource = new FileSystemResource(uploadLocation + fileName);
                ZipEntry zipEntry = new ZipEntry(resource.getFilename());
                zipEntry.setSize(resource.contentLength());
                zipOut.putNextEntry(zipEntry);
                StreamUtils.copy(resource.getInputStream(), zipOut);
                zipOut.closeEntry();
            }
            zipOut.finish();
            zipOut.close();
            return zipOut;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public OperationStatusCode saveFiles(MultipartFile[] files, String location) {
        for (MultipartFile file : files) {
            if (saveFile(file, location) == OperationStatusCode.FAILURE)
                return OperationStatusCode.FAILURE;
        }
        return OperationStatusCode.SUCESS;
    }

    @Override
    public OperationStatusCode saveFile(MultipartFile file, String location) {
        try {
            String uploadPath = location == null ? uploadLocation: location;
            Path path = Paths.get(baseLocation+uploadPath + "\\" + file.getOriginalFilename());
            OperationStatusCode st=fileUtils.createDirectoryIfDoesNotExist(uploadPath);

            FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
            InputStream inputStream = file.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            byte[] buffer = new byte[1024];
            int num;
            while ((num = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, num);
            }
            fileOutputStream.close();
            inputStream.close();
            return OperationStatusCode.SUCESS;
        } catch (IOException ioException) {
            return OperationStatusCode.FAILURE;
        }
    }

    @Override
    public DownloadLinkDataViewModel generateDownloadDetail(MultipartFile[] files) {

        OperationStatusCode status = saveFiles(files, downloadSourceLocation);
        String[] fileNames = new String[files.length];
        if (status == OperationStatusCode.SUCESS) {
            for (int i = 0; i < files.length; i++)
                fileNames[i] = files[i].getOriginalFilename();
            return generateDownloadDetails(fileNames);
        } else
            return null;
    }


}