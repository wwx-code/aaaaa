package top.wwxyh;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import top.wwxyh.util.SensitiveUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootTest
class YhblogApplicationTests {

    @Test
    void creatJwtToken() {
        String token = Jwts.builder().setId("1").setSubject("用户名")
                .setIssuedAt(new Date())  //设置创建时间
                .signWith(SignatureAlgorithm.HS256, "secretKey")  //设置加密方法和密钥
                .claim("test", "自定义")  //设置自定义claim数据
                .compact();  //构建
        System.out.println(token);
    }

    @Test
    void parseJetToken(){
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZG1pbjEiLCJleHAiOjE2MTg1NDI0OTN9.4busQMj42OdLVSv2BTaPMH_JMDl8QRuy-4e9gKHMZXvkS1z1v6vox69Ogpbl5z8ACkYWmxtTh9a43UMEAd2-Kg";
        Claims claims = Jwts.parser().setSigningKey("f4e2e52034348f86b67cde581c0f9eb5")  //设置解密密钥
                .parseClaimsJws(token)  //设置要解密的token
                .getBody();

        String id = claims.getId();
        String subject = claims.getSubject();

        System.out.println(id);
        System.out.println(subject);
    }

    @Test
    void testSent(){
        //手动设置关键词
        SensitiveUtil.initKeyWord(new HashSet<String>(){{
            add("傻逼");
            add("草泥马");
        }});
        String text = "草泥马，傻逼一个";
        boolean b = SensitiveUtil.isContaintSensitiveWord(text, 1);
        if (b) {
            System.out.println("存在敏感字符");
        }
    }

}
