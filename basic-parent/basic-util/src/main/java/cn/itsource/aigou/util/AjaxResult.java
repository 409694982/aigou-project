package cn.itsource.aigou.util;

/**
 * @author zt
 * @version V1.0
 * @className AjaxResult
 * @description 封装的ajax请求的结果的工具类，改进了以前的，set操作都返回AjaxResult对象，形成链式操作
 * @date 2019/5/11 21:36
 */
public class AjaxResult {

    //默认是成功的
    private Boolean success = true;
    private String message;
    //错误码
    private String errorCode;
    //封装的数据
    private Object data;

    public Boolean getSuccess() {
        return success;
    }

    public AjaxResult setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public AjaxResult setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public AjaxResult setErrorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Object getData() {
        return data;
    }

    public AjaxResult setData(Object data) {
        this.data = data;
        return this;
    }

    public static AjaxResult me(){
        return new AjaxResult();
    }
}
