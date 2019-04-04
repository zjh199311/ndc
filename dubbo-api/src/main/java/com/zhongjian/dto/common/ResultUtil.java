package com.zhongjian.dto.common;

import java.util.List;

public class ResultUtil {

	private static ResultDTO<Object> successResult = new ResultDTO<Object>(true, null,
			CommonMessageEnum.SUCCESS.getMsg(), CommonMessageEnum.SUCCESS.getCode(), null);

	private static ResultDTO<Object> failResult = new ResultDTO<Object>(false, null, CommonMessageEnum.FAIL.getMsg(),
			CommonMessageEnum.FAIL.getCode(), null);

	static ResultDTO<Object> defaultSuccess() {
		return successResult;
	}

	static ResultDTO<Object> defaultFail() {
		return failResult;
	}

	public static ResultDTO<Object> getSuccess(Object object) {
		if (object == null) {
			return defaultSuccess();
		}
		return success(object);
	}

	public static ResultDTO<Object> getFail(CommonMessageEnum commonMessageEnum) {
		if (commonMessageEnum == null) {
			return defaultFail();
		}
		return fail(commonMessageEnum);
	}

	static ResultDTO<Object> success(Object object) {
		ResultDTO<Object> successResult = new ResultDTO<Object>();
		successResult.setFlag(true);
		successResult.setData(object);
		if (successResult instanceof List) {
			List<?> theList = (List<?>) successResult;
			successResult.setCount(theList.size());
		}
		successResult.setErrorMessage(CommonMessageEnum.SUCCESS.getMsg());
		successResult.setStatusCode(CommonMessageEnum.SUCCESS.getCode());
		return successResult;
	}

	static ResultDTO<Object> fail(CommonMessageEnum commonMessageEnum) {
		ResultDTO<Object> failResult = new ResultDTO<Object>();
		failResult.setFlag(false);
		failResult.setErrorMessage(commonMessageEnum.getMsg());
		failResult.setStatusCode(commonMessageEnum.getCode());
		return failResult;
	}
}
