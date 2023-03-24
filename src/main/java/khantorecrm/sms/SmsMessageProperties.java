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
@ConfigurationProperties(prefix = "sms.message")
public class SmsMessageProperties {
    @Value("${sms.message.boss_phone}")
    private String basePhone;
    @Value("${sms.message.callback_url}")
    private String callbackUrl;
    @Value("${sms.message.after_each_sale_message}")
    private String eachSaleMessage;
    @Value("${sms.message.attention_each_week_message}")
    private String eachWeekMessage;
}
