package com.mySpace.ui.model.response;

public class DownloadLinkDataViewModel {
    private String downloadUrl;
    private String downloadSize;
    private String downloadFileName;

    public DownloadLinkDataViewModel() {
    }

    public DownloadLinkDataViewModel(String downloadUrl, String downloadSize, String downloadFileName) {
        this.downloadUrl = downloadUrl;
        this.downloadSize = downloadSize;
        this.downloadFileName = downloadFileName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(String downloadSize) {
        this.downloadSize = downloadSize;
    }

    public String getDownloadFileName() {
        return downloadFileName;
    }

    public void setDownloadFileName(String downloadFileName) {
        this.downloadFileName = downloadFileName;
    }
}
