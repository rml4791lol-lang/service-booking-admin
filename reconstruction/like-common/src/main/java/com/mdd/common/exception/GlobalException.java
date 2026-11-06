package com.mdd.common.exception;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.mdd.common.core.AjaxResult;
import com.mdd.common.enums.ErrorEnum;
import com.mdd.common.util.ErrorMessageUtils;
import com.mdd.common.util.RequestIdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 全局异常处理。
 *
 * <p>对外只返回两类文案：</p>
 * <ul>
 *     <li>明确的业务提示（BaseException / 参数校验注解 / 固定的 404、405 文案）；</li>
 *     <li>通用错误 + 请求标识（未预期异常、疑似 SQL/路径/堆栈信息）。</li>
 * </ul>
 *
 * <p>原始异常（含 SQL、文件路径、堆栈）只写入服务端日志，并按请求标识关联。</p>
 */
@ControllerAdvice
public class GlobalException {

    private static final Logger log = LoggerFactory.getLogger(GlobalException.class);

    private static final String GENERIC_MESSAGE = "系统繁忙，请稍后重试";
    private static final String INVALID_PARAM_MESSAGE = "请求参数格式错误";
    private static final String METHOD_NOT_SUPPORTED_MESSAGE = "请求方法不支持";
    private static final String PARAM_REQUIRED_MESSAGE = "缺少必填参数";
    private static final String PARAM_INVALID_MESSAGE = "参数校验错误";

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public AjaxResult<Object> handleException(Exception e) {
        return internalError(e);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {NoHandlerFoundException.class})
    @ResponseBody
    public AjaxResult<Object> handleNoHandlerFoundException(NoHandlerFoundException e) {
        // 路径只进日志，不返回客户端
        log.warn("请求接口不存在 requestId={} url={}", RequestIdUtils.current(), e.getRequestURL());
        return AjaxResult.failed(ErrorEnum.REQUEST_404_ERROR.getCode(), ErrorEnum.REQUEST_404_ERROR.getMsg());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {BaseException.class})
    @ResponseBody
    public AjaxResult<Object> handleException(BaseException e) {
        String message = ErrorMessageUtils.safeClientMessage(e.getMsg());
        if (message == null) {
            return internalError(e);
        }
        return AjaxResult.failed(e.getCode(), message);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {BindException.class})
    @ResponseBody
    public AjaxResult<Object> handleBindException(BindException e) {
        return AjaxResult.failed(ErrorEnum.PARAMS_VALID_ERROR.getCode(), bindingMessage(e.getBindingResult()));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public AjaxResult<Object> handlePathException(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        String message = parameterName == null || parameterName.trim().isEmpty()
                ? PARAM_REQUIRED_MESSAGE
                : PARAM_REQUIRED_MESSAGE + "：" + parameterName;
        return AjaxResult.failed(ErrorEnum.PARAMS_VALID_ERROR.getCode(), message);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    @ResponseBody
    public AjaxResult<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return AjaxResult.failed(ErrorEnum.PARAMS_VALID_ERROR.getCode(), bindingMessage(e.getBindingResult()));
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    public AjaxResult<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        // 反序列化异常里可能包含字段内容、类名，统一换成固定文案
        return AjaxResult.failed(ErrorEnum.PARAMS_TYPE_ERROR.getCode(), INVALID_PARAM_MESSAGE);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class})
    @ResponseBody
    public AjaxResult<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        // 非法数字/非法枚举等 @RequestParam 类型转换失败：给出明确参数名，不落通用“系统繁忙”。
        String name = e.getName();
        String message = name == null || name.trim().isEmpty()
                ? INVALID_PARAM_MESSAGE
                : "参数「" + name.trim() + "」格式错误";
        return AjaxResult.failed(ErrorEnum.PARAMS_TYPE_ERROR.getCode(), message);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public AjaxResult<Object> handleRequestMethodException(HttpRequestMethodNotSupportedException e) {
        return AjaxResult.failed(ErrorEnum.REQUEST_METHOD_ERROR.getCode(), METHOD_NOT_SUPPORTED_MESSAGE);
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {IllegalArgumentException.class})
    @ResponseBody
    public AjaxResult<Object> handleIllegalArgumentException(IllegalArgumentException e) {
        return businessOrInternal(e, ErrorEnum.ASSERT_ARGUMENT_ERROR.getCode());
    }

    @ResponseStatus(value = HttpStatus.OK)
    @ExceptionHandler(value = {MybatisPlusException.class})
    @ResponseBody
    public AjaxResult<Object> handleMybatisPlusException(MybatisPlusException e) {
        return businessOrInternal(e, ErrorEnum.ASSERT_MYBATIS_ERROR.getCode());
    }

    /**
     * 业务代码常用 MyBatis-Plus 的 Assert 做提示，这类文案需要保留；
     * 但同样的异常类型也可能携带 SQL，所以统一过一遍白名单校验。
     * 另外要求文案含中文：现有 117 处 Assert 提示均为中文，英文异常
     * 更可能是框架/SQL 内部信息，直接按未知异常处理。
     */
    private AjaxResult<Object> businessOrInternal(Exception e, Integer code) {
        String message = ErrorMessageUtils.safeClientMessage(e.getMessage());
        if (message == null || !containsChinese(message)) {
            return internalError(e);
        }
        return AjaxResult.failed(code, message);
    }

    private boolean containsChinese(String message) {
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (ch >= '\u4e00' && ch <= '\u9fff') {
                return true;
            }
        }
        return false;
    }

    private AjaxResult<Object> internalError(Exception e) {
        String requestId = RequestIdUtils.current();
        log.error("系统异常 requestId={} exception={}", requestId, e.getClass().getName(), e);
        return AjaxResult.failed(ErrorEnum.SYSTEM_ERROR.getCode(), withRequestId(GENERIC_MESSAGE, requestId));
    }

    private String bindingMessage(BindingResult bindingResult) {
        FieldError fieldError = bindingResult == null ? null : bindingResult.getFieldError();
        String message = fieldError == null ? null : fieldError.getDefaultMessage();
        return message == null || message.trim().isEmpty() ? PARAM_INVALID_MESSAGE : message;
    }

    private String withRequestId(String message, String requestId) {
        return message + "（请求ID：" + requestId + "）";
    }
}
