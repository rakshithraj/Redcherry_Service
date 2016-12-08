package com.redcherry.app.redcherry.extras;

/**
 * Created by admin123 on 8/19/2015.
 */
class TimeOutException extends Exception {

    public TimeOutException() {
        super();
    }

    public TimeOutException(String message) {
        super(message);
    }

    public TimeOutException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeOutException(Throwable cause) {
        super(cause);
    }
}
