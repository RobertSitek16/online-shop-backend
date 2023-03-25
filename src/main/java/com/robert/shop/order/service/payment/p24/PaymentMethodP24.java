package com.robert.shop.order.service.payment.p24;

import com.robert.shop.common.exception.PaymentMethodP24Exception;
import com.robert.shop.order.dto.NotificationReceiveDto;
import com.robert.shop.order.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;

    public String initPayment(Order newOrder) {
        log.info("Payment init");

        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<TransactionRegisterResponse> result = webClient.post().uri("/transaction/register")
                .bodyValue(TransactionRegisterRequest.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(newOrder))
                        .amount(newOrder.getGrossValue().movePointRight(2).intValue())
                        .currency("PLN")
                        .description("Order id " + newOrder.getId())
                        .email(newOrder.getEmail())
                        .client(newOrder.getFirstname() + " " + newOrder.getLastname())
                        .country("PL")
                        .language("pl")
                        .urlReturn(generateMethodUrl(newOrder.getOrderHash()))
                        .urlStatus(generateStatusUrl(newOrder.getOrderHash()))
                        .sign(createSign(newOrder))
                        .encoding("UTF-8")
                        .build())
                .retrieve()
                .onStatus(httpStatus -> {
                    log.error("Something went wrong... " + httpStatus.name());
                    return httpStatus.is4xxClientError();
                }, clientResponse -> Mono.empty())
                .toEntity(TransactionRegisterResponse.class)
                .block();
        if (result != null && result.getBody() != null && result.getBody().getData() != null) {
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) +
                    "/trnRequest/" + result.getBody().getData().token();
        }
        return null;
    }

    private String generateMethodUrl(String orderHash) {
        String basicUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        return basicUrl + "/order/notification/" + orderHash;
    }

    private String generateStatusUrl(String orderHash) {
        String basicUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        return basicUrl + "/orders/notification/" + orderHash;
    }

    private String createSign(Order newOrder) {
        String json = "{\"sessionId\":\"" + createSessionId(newOrder) +
                "\",\"merchantId\":" + config.getMerchantId() +
                ",\"amount\":" + newOrder.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private String createSessionId(Order newOrder) {
        return String.format("order_id_%s", newOrder.getId().toString());
    }

    public String receiveNotification(Order order, NotificationReceiveDto receiveDto, String remoteAddr) {
        log.info(receiveDto.toString());
        validateIpAddress(remoteAddr);
        validate(receiveDto, order);
        return verifyPayment(receiveDto, order);
    }

    private void validateIpAddress(String remoteAddr) {
        if (!config.getServers().contains(remoteAddr)) {
            throw new PaymentMethodP24Exception("Incorrect IP address for payment confirmation!");
        }
    }

    private void validate(NotificationReceiveDto receiveDto, Order order) {
        validateField(config.getMerchantId().equals(receiveDto.getMerchantId()));
        validateField(config.getPosId().equals(receiveDto.getPosId()));
        validateField(createSessionId(order).equals(receiveDto.getSessionId()));
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getAmount()).movePointLeft(2)) == 0);
        validateField(order.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDto.getCurrency()));
        validateField(createReceivedSign(receiveDto, order).equals(receiveDto.getSign()));
    }

    private String verifyPayment(NotificationReceiveDto receiveDto, Order order) {
        log.info("Payment verification...");
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();
        ResponseEntity<TransactionVerifyResponse> result = webClient.put().uri("/transaction/verify")
                .bodyValue(TransactionVerifyRequest.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(order))
                        .amount(order.getGrossValue().movePointRight(2).intValue())
                        .currency("PLN")
                        .orderId(receiveDto.getOrderId())
                        .sign(createVerifySign(receiveDto, order))
                        .build())
                .retrieve()
                .toEntity(TransactionVerifyResponse.class)
                .block();
        log.info("Verify transaction status: " + result.getBody().getData().status());
        return result.getBody().getData().status();
    }

    private String createVerifySign(NotificationReceiveDto receiveDto, Order order) {
        String json = "{\"sessionId\":\"" + createSessionId(order) +
                "\",\"orderId\":" + receiveDto.getOrderId() +
                ",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private void validateField(boolean condition) {
        if (!condition) {
            throw new PaymentMethodP24Exception("Incorrect validation!");
        }
    }

    private String createReceivedSign(NotificationReceiveDto receiveDto, Order order) {
        String json = "{\"merchantId\":" + config.getMerchantId() +
                ",\"posId\":" + config.getPosId() +
                ",\"sessionId\":\"" + createSessionId(order) +
                "\",\"amount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"originAmount\":" + order.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\"" +
                ",\"orderId\":" + receiveDto.getOrderId() +
                ",\"methodId\":" + receiveDto.getMethodId() +
                ",\"statement\":\"" + receiveDto.getStatement() +
                "\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }
}
