package com.zhongjian.common.util;


import com.zhongjian.common.constant.enums.response.CommonMessageEnum;
import com.zhongjian.dto.common.ResultDTO;

/**
 * 响应结果工具类
 *
 * @author: yami
 * @since: 2018/4/26
 */
public class ResultUtil {

    /**
     * 成功的响应
     *
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> success() {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(true);
        result.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
        result.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
        return result;
    }

    /**
     * 成功的响应(分页)
     *
     * @param data
     * @param count
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> success(T data, int count) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(true);
        result.setData(data);
        result.setCount(count);
        result.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
        result.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
        return result;
    }

    /**
     * 成功的响应(不分页)
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> success(T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(true);
        result.setData(data);
        result.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
        result.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
        return result;
    }

    /**
     * 失败的响应
     *
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail() {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setStatusCode(CommonMessageEnum.FAIL.getCode());
        result.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
        return result;
    }

    /**
     * 失败的响应
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail(T data) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setData(data);
        result.setStatusCode(CommonMessageEnum.FAIL.getCode());
        result.setErrorMessage(CommonMessageEnum.FAIL.getMsg());
        return result;
    }

    /**
     * 失败的响应
     *
     * @param data
     * @param code 响应码
     * @param msg  响应说明
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail(T data, String code, String msg) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setData(data);
        result.setStatusCode(code);
        result.setErrorMessage(msg);
        return result;
    }

    /**
     * 失败的响应
     *
     * @param code 响应码
     * @param msg  响应说明
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail(String code, String msg) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setData(null);
        result.setStatusCode(code);
        result.setErrorMessage(msg);
        return result;
    }

    /**
     * 失败的响应
     *
     * @param data
     * @param commonMessageEnum 响应枚举
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail(T data, CommonMessageEnum commonMessageEnum) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setData(data);
        result.setStatusCode(commonMessageEnum.getCode());
        result.setErrorMessage(commonMessageEnum.getMsg());
        return result;
    }

    /**
     * 失败的响应
     *
     * @param commonMessageEnum 响应枚举
     * @param <T>
     * @return
     */
    public static <T> ResultDTO<T> fail(CommonMessageEnum commonMessageEnum) {
        ResultDTO<T> result = new ResultDTO<>();
        result.setFlag(false);
        result.setStatusCode(commonMessageEnum.getCode());
        result.setErrorMessage(commonMessageEnum.getMsg());
        return result;
    }

}
