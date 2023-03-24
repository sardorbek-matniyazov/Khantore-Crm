package khantorecrm.sms;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsConfigProperties {
    @Value("${sms.mail}")
    private String login;
    @Value("${sms.pass}")
    private String password;
    @Value("${sms.from}")
    private String from;
}
