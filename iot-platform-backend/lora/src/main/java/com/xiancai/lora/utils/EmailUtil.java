package com.xiancai.lora.utils;

import cn.hutool.core.util.RandomUtil;
import com.sun.mail.util.MailSSLSocketFactory;
import com.xiancai.lora.constant.EmailStatus;
import com.xiancai.lora.exception.BusinessException;
import lombok.Data;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.xiancai.lora.constant.EmailStatus.*;
import static com.xiancai.lora.constant.RedisConstants.*;
import static com.xiancai.lora.enums.StatusCode.PARAMS_ERR;

@Data
@Component
public class EmailUtil {

//    public static String code;

    @Resource
    private  StringRedisTemplate stringRedisTemplate;

    public void verityEmailStatus(int emailStatus){
        if(emailStatus!=1&&emailStatus!=2){
            throw new BusinessException(PARAMS_ERR.getMessage(), PARAMS_ERR.getCode(), "emailStatus错误");
        }
    }


    public  String sendEmail(String addressee, int emailStatus) throws Exception{
        verityEmailStatus(emailStatus);
        Properties prop=new Properties();
        prop.setProperty("mail.host","smtp.qq.com");///设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol","smtp");///邮件发送协议
        prop.setProperty("mail.smtp.auth","true");//需要验证用户密码
        prop.put("mail.smtp.port", "587");


//        //QQ邮箱需要设置SSL加密,仅QQ邮箱才有
//        MailSSLSocketFactory sf=new MailSSLSocketFactory();  //此处导包注意mail类
//        sf.setTrustAllHosts(true);
//        prop.put("mail.smtp.ssl.enable","true");
//        prop.put("mail.smtp.ssl.socketFactory",sf);

        //使用javaMail发送邮件的5个步骤
        //1.创建定义整个应用程序所需要的环境信息的session对象
        Session session= Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
//
                return new PasswordAuthentication("183890277@qq.com","ljjhjlbguzgrcaag");
        }});
        //开启session的debug模式，这样可以查看到程序发送Email的运行状态
        session.setDebug(true);
        //2.通过session得到transport对象
        Transport ts=session.getTransport();
        //3.使用邮箱的用户名和授权码连上邮件服务器
        ts.connect("smtp.qq.com","183890277@qq.com","ljjhjlbguzgrcaag");
        //4.创建邮件：写文件
        //注意需要传递session
        MimeMessage message=new MimeMessage(session);
        //指明邮件的发件人
        message.setFrom(new InternetAddress("183890277@qq.com"));
        //指明邮件的收件人
        message.setRecipient(Message.RecipientType.TO,new InternetAddress(addressee));
        //邮件标题
        message.setSubject("邮箱验证码");
        //邮件的文本内容
        String code = RandomUtil.randomNumbers(6);
        if(emailStatus==REGISTER_EMAIL){
            stringRedisTemplate.opsForValue().set(EMAIL_REGISTER_CODE_KEY+addressee,code,EMAIL_CODE_TTL, TimeUnit.MINUTES);
            message.setContent("<div style=\"width:600px;margin:30px auto\"><h1 style=\"text-align:center;\">欢迎注册LoRa系统账号</h1><p style=\"font-size:24px\">此次的验证码如下:</p><strong style=\"font-size:20px;display:block;text-align:center;color:red\">"+code+"</strong><p>验证码十分钟内有效，请及时输入</p><i style=\"color:#00bfff\">此邮件为系统自动发送，请勿回复！若您没有进行注册请忽略。</i><p style=\"text-align:right\">--开封淳阳</p></div>`,","text/html;charset=UTF-8");
        }
        if(emailStatus==CHANGE_PASSWORD_EMAIL){
            stringRedisTemplate.opsForValue().set(EMAIL_CHANGE_PASSWORD_CODE_KEY+addressee,code,EMAIL_CODE_TTL, TimeUnit.MINUTES);
            message.setContent("<div style=\"width:600px;margin:30px auto\"><h1 style=\"text-align:center;\">欢迎更改LoRa系统密码</h1><p style=\"font-size:24px\">此次的验证码如下:</p><strong style=\"font-size:20px;display:block;text-align:center;color:red\">"+code+"</strong><p>验证码十分钟内有效，请及时输入</p><i style=\"color:#00bfff\">此邮件为系统自动发送，请勿回复！若您没有进行注册请忽略。</i><p style=\"text-align:right\">--开封淳阳</p></div>`,","text/html;charset=UTF-8");
        }
        //5.发送邮件
        ts.sendMessage(message,message.getAllRecipients());
        //6.关闭连接
        ts.close();
        return code;
    }

}
