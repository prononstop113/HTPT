package com.HTPT.FileServer.Util;

public class Constant {
    public interface APP_STATUS {
        String SUCCESS_STATUS = "0000";
        String FAIL_STATUS = "0001";
    }
    public interface FILE_STATE{
        String DELETED= "DELETED";
        String UPLOADED= "UPLOADED";
    }
    public interface ACTION_TYPE{
        String UPLOAD = "UPLOAD";
        String DELETE = "DELETE";
        String DOWNLOAD ="DOWNLOAD";
    }
    public interface ACTION_STATE{
        String COMPLETE="COMPLETE";
        String FAILED ="FAILED";
    }
    public final static String DATE_FORMAT1 = "dd/MM/yyyy";
    public final static String DATETIME_FORMAT1 = "dd/MM/yyyy HH:mm:ss";
    public interface ERROR_CODE {
        String FAIL_CODE = "error.P999999";
        String NOTFOUND_CODE = "error.P999998";
        String INVALID_PARAMS_CODE = "error.P999997";
        String SQL_FAIL = "error.P999994";
    }
}
