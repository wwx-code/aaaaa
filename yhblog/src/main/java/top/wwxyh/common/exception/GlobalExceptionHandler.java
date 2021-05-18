package top.wwxyh.common.exception;


import top.wwxyh.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)  //返回状态码给前端
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e){
        log.error("Shiro异常：-------{}",e);
        String message = e.getMessage();
        return Result.fail("401",e.getMessage(),null);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  //返回状态码给前端
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体校验异常：-------{}",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)  //返回状态码给前端
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常：-------{}",e);
        return Result.fail(e.getMessage());
    }

    //处理Assert的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)  //返回状态码给前端
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常：-------{}",e);
        return Result.fail(e.getMessage());
    }

}
