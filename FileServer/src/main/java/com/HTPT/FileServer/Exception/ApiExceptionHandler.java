package com.HTPT.FileServer.Exception;

import com.HTPT.FileServer.Model.APIErrorResponse;
import com.HTPT.FileServer.Model.APIResponse;
import com.HTPT.FileServer.Util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {
    /**
     * Tất cả các Exception không được khai báo sẽ được xử lý tại đây
     */
    @ExceptionHandler({Exception.class})
    @ResponseStatus(value = HttpStatus.OK)
    public final APIResponse handleAllException(Exception ex, WebRequest request) {
        return new APIErrorResponse(Constant.ERROR_CODE.FAIL_CODE).forException(ex);
    }

    @ExceptionHandler({ NoHandlerFoundException.class })
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse notFound(final NoHandlerFoundException ex) {
        return new APIErrorResponse(Constant.ERROR_CODE.FAIL_CODE).forException(ex);
    }

    @ExceptionHandler({AppException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public final APIResponse handleAppException(AppException ex) {
        return new APIErrorResponse(Constant.ERROR_CODE.FAIL_CODE).forAppException(ex);
    }


    @ExceptionHandler({JpaSystemException.class})
    @ResponseStatus(value = HttpStatus.OK)
    public final APIResponse handleJpaSystemException(JpaSystemException ex) {
        return new APIErrorResponse(Constant.ERROR_CODE.SQL_FAIL).forSqlException(ex);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final APIResponse handleValidException(MethodArgumentNotValidException ex, WebRequest request) {
        return new APIErrorResponse(Constant.ERROR_CODE.FAIL_CODE).forNotValidException(ex);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public final APIResponse handleMissParamException(MissingServletRequestParameterException ex){
        return new APIErrorResponse(Constant.ERROR_CODE.FAIL_CODE).forMissParamException(ex);
    }
}
