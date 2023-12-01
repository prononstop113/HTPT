package com.HTPT.FileServer.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {
    private String status;
    private Object data;
    private String message;
    private String errorCode;

    public APIResponse emptyBody() {
        return this;
    }

    public APIResponse withData(Object data){
        setData(data);
        return this;
    }
}
