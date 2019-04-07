package com.stardai.manage.bean;

import org.springframework.validation.BindingResult;

/**
 *  @author jdw
 *  @date 2017/10/16
 */
@SuppressWarnings("all")
public class ResponseModel<T> {

    private int code;// 0成功 1业务失败(客户端可以处理代码) 2通用错误(客户端只能提示错误信息) 3客户端可自动跳转登录界面 4加入黑名单强制下线一次

    private String msg;

    private T data;

    public ResponseModel() {
    }

    public ResponseModel(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = (T) data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResponseModel<?> success(Object object) {
        return new ResponseModel(0, null, object);
    }
    public static ResponseModel<?> success() {
        return success(null);
    }

    public static ResponseModel<?> success(String msg, Object data) {
        return new ResponseModel(0, msg, data);
    }

    public static ResponseModel<?> error(int code, String msg) {
        return new ResponseModel(code, msg, null);
    }

    public static ResponseModel<?> error(String msg) {
        return error(2, msg);
    }

    public static ResponseModel<?> error(BindingResult bindingResult) {
        return error(2, bindingResult.getFieldError().getDefaultMessage());
    }

    public static ResponseModel<?> fail(String msg) {
        return new ResponseModel(1, msg, null);
    }

    public static ResponseModel<?> fail() {
        return new ResponseModel(1, null, null);
    }

    public static ResponseModel<?> fail(BindingResult bindingResult) {
        return new ResponseModel(1, bindingResult.getFieldError().getDefaultMessage(), null);
    }

}
