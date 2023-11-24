package com.HTPT.FileServer.Model;

import com.HTPT.FileServer.Util.Constant;
import lombok.Data;


@Data

public class APISucResponse extends  APIResponse{

    public APISucResponse(){
        super();
        setStatus(Constant.APP_STATUS.SUCCESS_STATUS);
        setErrorCode(Constant.APP_STATUS.SUCCESS_STATUS);
        setMessage("Success");
    }
    public APISucResponse(Object data){
        super();
        setStatus(Constant.APP_STATUS.SUCCESS_STATUS);
        setErrorCode(Constant.APP_STATUS.SUCCESS_STATUS);
        setMessage("Success");
        setData(data);
    }
}
