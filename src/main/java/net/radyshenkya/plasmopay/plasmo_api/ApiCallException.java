package net.radyshenkya.plasmopay.plasmo_api;

public class ApiCallException extends Exception {
    public final String message;
    public final Exception causedBy;

    public ApiCallException(String message, Exception causedBy) {
        this.message = message;
        this.causedBy = causedBy;
    }
}
