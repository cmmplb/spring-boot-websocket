package com.cmmplb.websocket.exception;

import com.cmmplb.websocket.result.HttpCodeEnum;
import com.cmmplb.websocket.result.Result;
import com.cmmplb.websocket.result.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.NoSuchMessageException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Objects;

/**
 * @author plb
 * @date 2020/6/12 9:58
 * 全局异常捕获
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler<T> implements ResponseBodyAdvice<T> {

    @Override
    public T beforeBodyWrite(T o, @NonNull MethodParameter methodParameter, @NonNull MediaType mediaType, @NonNull Class<? extends HttpMessageConverter<?>> aClass, @NonNull ServerHttpRequest serverHttpRequest, @NonNull ServerHttpResponse serverHttpResponse) {
        return o;
    }

    @Override
    public boolean supports(@NonNull MethodParameter methodParameter, @NonNull Class aClass) {
        return true;
    }

    @ExceptionHandler({Exception.class})
    public Result<?> exceptionHandler(Exception e) {
        log.error("error:", e);
        // 统一处理文件过大问题
        if ((e instanceof MaxUploadSizeExceededException)) {
            return ResultUtil.custom(HttpCodeEnum.REQUEST_ENTITY_TOO_LARGE);
        }
        // 空指针异常
        if ((e instanceof NullPointerException)) {
            return ResultUtil.custom(HttpCodeEnum.NULL_POINT_ERROR);
        }
        // 算术异常
        if ((e instanceof ArithmeticException)) {
            return ResultUtil.custom(HttpCodeEnum.ARITHMETIC_ERROR);
        }
        // 类型转换异常
        if ((e instanceof ClassCastException)) {
            return ResultUtil.custom(HttpCodeEnum.CLASS_CAST_ERROR);
        }
        // 消息获取异常
        if ((e instanceof NoSuchMessageException)) {
            return ResultUtil.custom(HttpCodeEnum.NO_SUCH_MESSAGE_ERROR);
        }
        // 方法参数类型不匹配异常
        if ((e instanceof MethodArgumentTypeMismatchException)) {
            return ResultUtil.custom(HttpCodeEnum.METHOD_ARGUMENT_TYPE_MISMATCH);
        }
        // 请求方式
        if ((e instanceof HttpRequestMethodNotSupportedException)) {
            HttpRequestMethodNotSupportedException h = (HttpRequestMethodNotSupportedException) e;
            StringBuilder sb = new StringBuilder().append("不支持").append(h.getMethod()).append("请求方法，").append("支持");
            String[] methods = h.getSupportedMethods();
            if (methods != null) {
                for (String str : methods) {
                    sb.append(str);
                }
            }
            return ResultUtil.custom(HttpCodeEnum.METHOD_NOT_ALLOWED.getCode(), sb.toString());
        }
        // 上述异常都没匹配
        log.error(e.getMessage(), e);
        setStatusCode(HttpCodeEnum.INTERNAL_SERVER_ERROR.getCode());
        return ResultUtil.custom(HttpCodeEnum.INTERNAL_SERVER_ERROR);
    }

    private void setStatusCode(int code) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        Objects.requireNonNull(Objects.requireNonNull(requestAttributes).getResponse()).setStatus(code);
    }
}

