package com.robert.shop.common.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FakeEmailService implements EmailSender {

    @Async
    @Override
    public void send(String to, String subject, String msg) {
        log.info("Email Send");
        log.info("To " + to);
        log.info("Subject " + subject);
        log.info("Message " + msg);
    }

}
