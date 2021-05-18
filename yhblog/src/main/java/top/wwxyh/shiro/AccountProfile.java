package top.wwxyh.shiro;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class AccountProfile implements Serializable {
    private Long id;

    private String username;

    private String nickname;

    private String avatar;

    private String email;

    private LocalDateTime create_time;
    //角色访问权限
    private String role;
}
