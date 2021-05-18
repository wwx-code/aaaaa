package top.wwxyh.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * @Author wwx
 * @Description  邮件工具类
 * @Date 2021/4/15 19:54
 * @Param
 * @return
 **/
@Component
@EnableAsync
public class MailUtils {
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private MailProperties mailProperties;

	/**
	 * @Author wwx
	 * @Description  发送普通邮件
	 * @Date 2021/4/15 23:20
	 * @Param [toAccount, subject, content]
	 * toAccount：邮件地址； subject：邮件标题； content：邮件内容
	 * @return void
	 **/
	@Async
	public void sendSimpleMail(String toAccount, String subject, String content) {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(mailProperties.getUsername());
			message.setTo(toAccount);
			message.setSubject(subject);
			message.setText(content);
			javaMailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Author wwx
	 * @Description  发送HTML邮件
	 * @Date 2021/5/10 20:00
	 * @Param [toAccount, subject, content]
	 * toAccount：邮件地址； subject：邮件标题； content：邮件内容
	 * @return void
	 **/
	@Async
	public void sendHTMLMail(String toAccount, String subject, String content) {
		try {
			//构建邮件，创建mimeMessage对象
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			//生成helper类，构建邮件收发信息
			MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
			messageHelper.setFrom(mailProperties.getUsername());
			messageHelper.setTo(toAccount);
			messageHelper.setSubject(subject);
			messageHelper.setText(content, true);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
