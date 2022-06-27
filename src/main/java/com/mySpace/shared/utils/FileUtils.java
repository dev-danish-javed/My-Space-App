package com.mySpace.shared.utils;

import com.mySpace.shared.constants.urlConstants.OperationStatusCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;



@Component
public class FileUtils {
    @Value("${base-location}")
    String baseLocation;

    public OperationStatusCode createDirectoryIfDoesNotExist(String path) {

        File baseLocationFile=new File(baseLocation);
        if( ! baseLocationFile.exists()){
            baseLocationFile.mkdir();
        }
        File directory = new File(baseLocation+path);
        if (!directory.exists())
             directory.mkdir();

        return directory.exists()?OperationStatusCode.SUCESS:OperationStatusCode.FAILURE;
    }

    public OperationStatusCode deleteDirectory(String path) {

        File baseLocationFile=new File(baseLocation);
        String ac=baseLocationFile.getAbsolutePath();
        if( ! baseLocationFile.exists()){
           return OperationStatusCode.SUCESS;
        }
        File directory = new File(baseLocation+path);
        String dfsf= directory.getAbsolutePath();
        if (directory.exists()) {
            try {
                org.apache.tomcat.util.http.fileupload.FileUtils.deleteDirectory(directory);

            } catch (IOException e) {
                return OperationStatusCode.FAILURE;
            }
        }

        return OperationStatusCode.SUCESS;
    }
}
