package com.example.praca.service;

import com.example.praca.model.RefreshToken;
import lombok.Data;
import java.util.Collections;
import java.util.Map;

/**
 * @author Daniel Lezniak
 */
@Data
public class ReturnService<T> {
    private String message;
    private int status;
    private Map<String, String> errList;
    private T value;
    private RefreshToken refreshToken;

    public static <T> ReturnService returnError(String errMsg, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setErrList(Collections.singletonMap("user", errMsg));
        ret.setValue(obj);
        ret.setStatus(0);

        return ret;
    }

    public static <T> ReturnService returnError(String errMsg, Map<String, String> errList, T obj, int status) {
        ReturnService ret = new ReturnService();
        ret.setErrList(errList);
        ret.setValue(obj);
        ret.setStatus(status);

        return ret;
    }

    public static ReturnService returnError(String errMsg, int status) {
        ReturnService ret = new ReturnService();
        ret.setMessage(errMsg);
        ret.setStatus(status);

        return ret;
    }

    public static <T> ReturnService returnInformation(String msg, T obj, int status) {
        ReturnService ret = new ReturnService();

        ret.setMessage(msg);
        ret.setStatus(status);
        ret.setValue(obj);

        return ret;
    }

    public static <T> ReturnService returnInformation(String msg, int status) {
        ReturnService ret = new ReturnService();

        ret.setMessage(msg);
        ret.setStatus(status);

        return ret;
    }

    public static <T> ReturnService returnRefreshTokenInformation(RefreshToken refreshToken, int status) {
        ReturnService ret = new ReturnService();

        ret.setRefreshToken(refreshToken);
        ret.setStatus(1);

        return ret;
    }


}
