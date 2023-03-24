package khantorecrm.sms;

import khantorecrm.sms.payload.SmsLoginResponse;
import khantorecrm.sms.payload.SmsMessageSentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
@FeignClient(name = "sms-api", url = "https://notify.eskiz.uz/api")
public interface SmsApiClient {
    @RequestMapping(method = POST, value = "/auth/login")
    ResponseEntity<SmsLoginResponse> login(
            @RequestParam("email") String username,
            @RequestParam("password") String password);

    @RequestMapping(method = POST, value = "/message/sms/send")
    ResponseEntity<SmsMessageSentResponse> sendSms(
            @RequestParam("mobile_phone") String to,
            @RequestParam("message") String text,
            @RequestParam("from") String from,
            @RequestParam("callback_url") String callbackUrl
    );
}
