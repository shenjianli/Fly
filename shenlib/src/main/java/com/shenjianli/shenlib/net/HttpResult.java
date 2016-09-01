package com.shenjianli.shenlib.net;

public class HttpResult<T> {
    //0表示失败，非0  其他表示成功
	private int errNo;

    //表示错误信息
    private String errMsg;

    //表示数据
    private T data;

    public int getErrNo() {
        return errNo;
    }

    public void setErrNo(int errNo) {
        this.errNo = errNo;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
