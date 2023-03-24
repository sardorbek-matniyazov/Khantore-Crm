package khantorecrm.service.impl;

import khantorecrm.model.Client;
import khantorecrm.repository.ClientRepository;
import khantorecrm.service.IClientSmsService;
import khantorecrm.sms.BearerAuthInterceptor;
import khantorecrm.sms.SmsApiClient;
import khantorecrm.sms.SmsConfigProperties;
import khantorecrm.sms.SmsMessageProperties;
import khantorecrm.sms.payload.SmsLoginResponse;
import khantorecrm.sms.payload.SmsMessageSentResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Mar 2023
 **/
@Service
public class ClientSmsService implements IClientSmsService {
    private final ClientRepository clientRepository;
    private final SmsApiClient smsApiClient;
    private final SmsMessageProperties smsMessageProperties;
    private final SmsConfigProperties configProperties;
    private final BearerAuthInterceptor bearerAuthInterceptor;

    private final Logger logger = LoggerFactory.getLogger(ClientSmsService.class);

    public ClientSmsService(ClientRepository clientRepository, SmsApiClient smsApiClient, SmsMessageProperties smsMessageProperties, SmsConfigProperties configProperties, BearerAuthInterceptor bearerAuthInterceptor) {
        this.clientRepository = clientRepository;
        this.smsApiClient = smsApiClient;
        this.smsMessageProperties = smsMessageProperties;
        this.configProperties = configProperties;
        this.bearerAuthInterceptor = bearerAuthInterceptor;
    }

    @Override
    public void sendSmsAfterEachSale(String clientName, String phone, String wholePrice, String paidPrice, String debtPrice) {
        final String message = String.format(smsMessageProperties.getEachSaleMessage(),
                clientName, wholePrice, paidPrice, debtPrice);

        // send message
        sendMessage(phone, message, 1);
    }

    private void sendMessage(String phoneNumber, String message, int count) {
        if (count > 3) {
            logger.error("Sms sending failed 4xx error, reLogin process failed");
            return;
        }

        try {
            final ResponseEntity<SmsMessageSentResponse> objectResponseEntity = smsApiClient.sendSms(
                    String.format("998%s", phoneNumber),
                    message,
                    "4546",
                    smsMessageProperties.getCallbackUrl()
            );
        } catch (Exception e) {
            final ResponseEntity<SmsLoginResponse> login = smsApiClient.login(configProperties.getLogin(), configProperties.getPassword());
            final String token = Objects.requireNonNull(login.getBody()).getData().get("token");
            bearerAuthInterceptor.setToken(String.format("Bearer %s", token));
            sendMessage(phoneNumber, message, count + 1);
        }
    }

    @Scheduled(cron = "0 0 8 * * 1")
    public void scheduledEachWeek() {
        final List<Client> debtClients = clientRepository.findAllByBalance_AmountSmallerThan0();
        debtClients.forEach(this::sendSmsToDebtClients);
    }

    private void sendSmsToDebtClients(Client client) {
        final String message = String.format(
                smsMessageProperties.getEachWeekMessage(),
                client.getName(),
                getDate(),
                client.getBalance().getAmount()
        );
        sendMessage(client.getPhone(), message, 0);
    }

    private String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());
    }
}
