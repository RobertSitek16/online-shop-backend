package com.robert.shop.admin.order.service;

import com.robert.shop.admin.order.model.AdminOrder;
import com.robert.shop.common.mail.EmailClientService;
import com.robert.shop.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createCompletedEmailMessage;
import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createCompletedEmailSubject;
import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createProcessingEmailMessage;
import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createProcessingEmailSubject;
import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createRefundEmailMessage;
import static com.robert.shop.admin.order.service.AdminOrderEmailMessageService.createRefundEmailSubject;

@Service
@RequiredArgsConstructor
class EmailNotificationStatusChangeService {

    private final EmailClientService emailClientService;

    public void sendEmailNotification(OrderStatus newStatus, AdminOrder adminOrder) {
        if (newStatus == OrderStatus.PROCESSING) {
            sendEmail(adminOrder.getEmail(),
                    createProcessingEmailSubject(adminOrder.getId(), newStatus),
                    createProcessingEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.COMPLETED) {
            sendEmail(adminOrder.getEmail(),
                    createCompletedEmailSubject(adminOrder.getId()),
                    createCompletedEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.REFUND) {
            sendEmail(adminOrder.getEmail(),
                    createRefundEmailSubject(adminOrder.getId()),
                    createRefundEmailMessage(adminOrder.getId(), newStatus));
        }
    }

    private void sendEmail(String email, String subject, String content) {
        emailClientService.getInstance().send(email, subject, content);
    }


}
