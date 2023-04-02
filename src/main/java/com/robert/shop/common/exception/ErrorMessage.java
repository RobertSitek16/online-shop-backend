package com.robert.shop.common.exception;

import java.time.LocalDateTime;

public record ErrorMessage(
        LocalDateTime timestamp,
        int statusCode,
        String error,
        String message,
        String path
) {
}
