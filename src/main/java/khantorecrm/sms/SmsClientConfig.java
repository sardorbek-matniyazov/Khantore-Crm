package khantorecrm.sms;

import feign.Feign;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
@Configuration
public class SmsClientConfig {
    @Bean
    public SmsApiClient smsApiClient() {
        return Feign.builder()
//                .errorDecoder(new CustomErrorDecoder())
                .requestInterceptor(bearerAuthInterceptor())
                .contract(new CustomSpringMvcContract())
                .target(SmsApiClient.class, "https://notify.eskiz.uz/api/");
    }

    @Bean
    public BearerAuthInterceptor bearerAuthInterceptor() {
        return new BearerAuthInterceptor("token");
    }
}
