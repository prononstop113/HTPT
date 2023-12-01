/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.HTPT.FileServer.Exception;

import com.HTPT.FileServer.Util.AppMessageUtil;

public class AppException extends Exception {
    private String key;
    
    public AppException(String key){
        super(key + ": " + AppMessageUtil.getInstance().getMessage(key, null));
        this.key = key;
    }
    
    public AppException(String key, Object... args){
        super(key + ": " + AppMessageUtil.getInstance().getMessage(key, args));
        this.key = key;
    }

    public AppException(String key, String message){
        super(message);
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
