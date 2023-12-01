package com.HTPT.FileServer.Model;

import com.HTPT.FileServer.Exception.AppException;
import com.HTPT.FileServer.Util.AppMessageUtil;
import com.HTPT.FileServer.Util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class APIErrorResponse extends APIResponse {

    public APIErrorResponse(String errorCode) {
        super();
        setErrorCode(errorCode);
        setStatus(Constant.APP_STATUS.FAIL_STATUS);
    }

    public APIErrorResponse forException(Exception ex) {
        if(ex != null && ex instanceof AppException){
            AppException appException = ((AppException)ex);
            setErrorCode(appException.getKey());
            setMessage(appException.getMessage());

        } else if (ex != null){
            setErrorCode("NONE");
            setMessage(ex.getMessage());
        }

        log.error(ex.getMessage(), ex);

        return this;
    }

    public APIResponse forAppException(AppException ex) {
        setErrorCode(ex.getKey());
        setMessage(ex.getMessage());

        log.error(ex.getMessage(), ex);

        return this;
    }

    public APIResponse forNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getFieldErrors();
        String key = Constant.ERROR_CODE.INVALID_PARAMS_CODE;
        String message = fieldErrors.stream().map(x -> x.getDefaultMessage()).collect(Collectors.joining(", "));
        if (StringUtils.isEmpty(message))
            message =  StringUtils.clean(AppMessageUtil.getInstance().getMessage(key, null));
        setErrorCode(key);
        setMessage(message);
        return this;
    }

    public APIResponse forMissParamException(MissingServletRequestParameterException ex) {
        String key = Constant.ERROR_CODE.INVALID_PARAMS_CODE;
        String parameter = ex.getParameterName();
        String message = AppMessageUtil.getInstance().getMessage(key, new Object[]{parameter});
        if (StringUtils.isEmpty(message))
            message = ex.getMessage();
        setErrorCode(key);
        setMessage(message);
        return this;
    }


    public APIResponse forSqlException(JpaSystemException ex) {
        setErrorCode(Constant.ERROR_CODE.SQL_FAIL);
        setMessage(ex.getMessage());

        return this;
    }
}
