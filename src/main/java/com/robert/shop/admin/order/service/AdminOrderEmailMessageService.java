package com.robert.shop.admin.order.service;

import com.robert.shop.admin.order.model.AdminOrderStatus;

public class AdminOrderEmailMessageService {

    public static String createProcessingEmailSubject(Long id, AdminOrderStatus newStatus) {
        return String.format("Order %s changed status to: %s", id, newStatus);
    }

    public static String createCompletedEmailSubject(Long id) {
        return String.format("The order %s has been completed", id);
    }

    public static String createRefundEmailSubject(Long id) {
        return String.format("The order %s has been refunded", id);
    }

    public static String createProcessingEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Your order: " + id + " is processing." +
                "\nStatus has been changed to: " + newStatus +
                "\nAfter completing the order, we will forward it for shipment." +
                "\n\nGreetings," +
                "\nYour best shop";
    }

    public static String createCompletedEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Your order: " + id + " has been completed." +
                "\nStatus has been changed to: " + newStatus +
                "\nThank you for your purchase and we look forward to seeing you again." +
                "\n\nGreetings," +
                "\nYour best shop";
    }

    public static String createRefundEmailMessage(Long id, AdminOrderStatus newStatus) {
        return "Your order: " + id + " has been refunded." +
                "\nStatus has been changed to: " + newStatus +
                "\n\nGreetings," +
                "\nYour best shop";
    }

}
