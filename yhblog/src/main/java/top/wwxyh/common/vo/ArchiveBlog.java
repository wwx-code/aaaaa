package top.wwxyh.common.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:  归档页面博客简要信息
 * @Author: wwx
 * @Date: 2021/3/28 21:04
 */
@Data
@NoArgsConstructor
public class ArchiveBlog {
    private Long id;
    private String title;
    private String day;
    private String password;
    private Boolean privacy;
}
