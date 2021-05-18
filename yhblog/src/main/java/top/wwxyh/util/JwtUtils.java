package top.wwxyh.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.GrantedAuthority;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * @Author wwx
 * @Description  JWT工具类
 * @Date 2021/3/29 16:08
 **/
@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "wwxyh.jwt")
public class JwtUtils {
	//签名的失效时间
	private static long expireTime;
	//签名私钥
	private static String secretKey;

	@Value("${wwxyh.jwt.secretKey}")
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	@Value("${wwxyh.jwt.expireTime}")
	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}

	/**
	 * 判断token是否存在
	 *
	 * @param token
	 * @return
	 */
	public static boolean judgeTokenIsExist(String token) {
		return token != null && !"".equals(token) && !"null".equals(token);
	}

	/**
	 * 生成token
	 *
	 * @param subject
	 * @return
	 */
	public static String generateToken(String subject) {
		String jwt = Jwts.builder()
				.setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		return jwt;
	}

	/**
	 * 生成带角色权限的token
	 *
	 * @param subject
	 * @param authorities
	 * @return
	 */
//	public static String generateToken(String subject, Collection<? extends GrantedAuthority> authorities) {
//		StringBuilder sb = new StringBuilder();
//		for (GrantedAuthority authority : authorities) {
//			sb.append(authority.getAuthority()).append(",");
//		}
//		String jwt = Jwts.builder()
//				.setSubject(subject)
//				.claim("authorities", sb)
//				.setExpiration(new Date(System.currentTimeMillis() + expireTime))
//				.signWith(SignatureAlgorithm.HS512, secretKey)
//				.compact();
//		return jwt;
//	}

	/**
	 * 生成自定义过期时间token
	 *
	 * @param subject
	 * @param expireTime
	 * @return
	 */
	public static String generateToken(String subject, long expireTime) {
		String jwt = Jwts.builder()
				.setSubject(subject)
				.setExpiration(new Date(System.currentTimeMillis() + expireTime))
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		System.out.println(new Date(System.currentTimeMillis() + expireTime));
		return jwt;
	}


	/**
	 * 获取tokenBody同时校验token是否有效（无效则会抛出异常）
	 * （解密Token）
	 * @param token
	 * @return
	 */
	public static Claims getTokenBody(String token) {
//		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.replace("Bearer ", "")).getBody();
//		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
//		return claims;
		try {
			return Jwts.parser()
					.setSigningKey(secretKey)
					.parseClaimsJws(token)
					.getBody();
		}catch (Exception e){
			log.debug("validate is token error ", e);
			return null;
		}
	}

	/**
	 * token是否过期
	 * @return  true：过期
	 */
	public boolean isTokenExpired(Date expiration) {
		return expiration.before(new Date());
	}
}
