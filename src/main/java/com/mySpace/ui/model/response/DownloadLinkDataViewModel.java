package com.mySpace.ui.model.response;

public class DownloadLinkDataViewModel {
    private String downloadUrl;
    private String downloadSize;
    private String downloadFileName;
    private String qrUrl;

    public DownloadLinkDataViewModel() {
    }

    public DownloadLinkDataViewModel(String downloadUrl, String downloadSize, String downloadFileName, String downloadId) {
        this.downloadUrl = downloadUrl;
        this.downloadSize = downloadSize;
        this.downloadFileName = downloadFileName;
        this.qrUrl = downloadId;
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

    public String getQrUrl() {
        return qrUrl;
    }

    public void setQrUrl(String qrUrl) {
        this.qrUrl = qrUrl;
    }
}
