package org.seckill.dto;

/*
 * 用于封装JSON结果返回给前端, 使用泛型以便作为通用封装类
 */
public class SeckillResult<T> {
    private boolean success;
    private T data;
    private String error;

    public SeckillResult(boolean success, T data) {
        super();
        this.success = success;
        this.data = data;
    }

    public SeckillResult(boolean success, String error) {
        super();
        this.success = success;
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

}
