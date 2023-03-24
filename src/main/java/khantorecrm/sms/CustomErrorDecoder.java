package khantorecrm.sms;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
@NoArgsConstructor
public class CustomErrorDecoder implements ErrorDecoder {
    private final Logger logger = LoggerFactory.getLogger(CustomErrorDecoder.class);

    private final ErrorDecoder defaultDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        if (status.is4xxClientError() || status.is5xxServerError()) {
            // handle error
            // ...
            logger.error("Error occurred while calling {} with status code {}", methodKey, status.value());
        }
        return defaultDecoder.decode(methodKey, response);
    }
}
