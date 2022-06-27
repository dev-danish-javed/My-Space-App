package com.mySpace.shared.constants.urlConstants;

public enum OperationStatusCode {
    SUCESS(1),
    FAILURE(0);
    private int code;
    OperationStatusCode(int code){
        this.code=code;
    }
}
