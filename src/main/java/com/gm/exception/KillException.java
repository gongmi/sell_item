package com.gm.exception;

/**
 * 秒杀相关的所有业务异常
 */
public class KillException extends RuntimeException {
    public KillException(String message) {
        super(message);
    }

    public KillException(String message, Throwable cause) {
        super(message, cause);
    }
}
