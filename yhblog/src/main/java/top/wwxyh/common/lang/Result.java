package top.wwxyh.common.lang;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
//统一结果封装
public class Result implements Serializable {

    private String code;  // 200表示正常，非200表示非正常
    private String msg;
    private Object data;

    public static Result succ(String code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result succ(String msg, Object data){
        return succ("200", msg, data);
    }

    public static Result succ(String msg) {
        return succ("200", msg, null);
    }

    public static Result succ(Object data){
        return succ("200","操作成功",data);
    }

    public static Result fail(String code, String msg, Object data){
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Result fail(String msg, Object data){
        return fail("400",msg,data);
    }

    public static Result fail(String msg){
        return fail("400",msg,null);
    }
}
