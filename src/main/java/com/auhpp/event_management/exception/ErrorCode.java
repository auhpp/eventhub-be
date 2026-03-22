package com.auhpp.event_management.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    EMAIL_ALREADY_EXISTS(1000, "Email already exists", HttpStatus.BAD_REQUEST),
    OTP_NOT_VALID(1001, "Otp not valid", HttpStatus.BAD_REQUEST),
    OTP_NOT_FOUND(1002, "Otp not found", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1003, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1004, "Forbidden", HttpStatus.FORBIDDEN),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    USER_LOCKED(1006, "User locked", HttpStatus.LOCKED),
    INVALID_TOKEN(1007, "Invalid token", HttpStatus.UNAUTHORIZED),
    RESOURCE_NOT_FOUND(1008, "Resource not found", HttpStatus.NOT_FOUND),
    RESOURCE_CAN_NOT_DELETE(1009, "Resource can not delete", HttpStatus.BAD_REQUEST),
    RESOURCE_CAN_NOT_CANCEL(1010, "Resource can not cancel", HttpStatus.BAD_REQUEST),
    RESOURCE_CAN_NOT_UPDATE(1011, "Resource can not update", HttpStatus.BAD_REQUEST),
    FILE_UPLOAD_FAILED(1012, "file upload failed", HttpStatus.INTERNAL_SERVER_ERROR),
    FILE_CAN_NOT_EMPTY(1013, "File can not empty", HttpStatus.BAD_REQUEST),
    FILE_TOO_LARGE(1014, "File too large", HttpStatus.BAD_REQUEST),
    INVALID_FILE_NAME(1015, "Invalid file name", HttpStatus.BAD_REQUEST),
    INVALID_EXTENSION(1016, "Invalid extension", HttpStatus.BAD_REQUEST),
    NOT_AN_IMAGE(1017, "Not an image", HttpStatus.BAD_REQUEST),
    FILE_DELETE_FAILED(1018, "file delete failed", HttpStatus.INTERNAL_SERVER_ERROR),
    ORGANIZER_ALREADY(1019, "Organizer already", HttpStatus.BAD_REQUEST),
    INVALID_PARAMS(1020, "Invalid params", HttpStatus.BAD_REQUEST),
    NOT_ENOUGH_QUANTITY(1021, "Not enough quantity", HttpStatus.BAD_REQUEST),
    INVALID_QUANTITY(1022, "Invalid quantity", HttpStatus.BAD_REQUEST),
    INVALID_BOOKING(1023, "Invalid booking", HttpStatus.BAD_REQUEST),
    INVALID_TIME_BOOKING(1024, "Invalid time booking", HttpStatus.BAD_REQUEST),
    INVALID_TIME_INVITATION(1025, "Invalid time invitation", HttpStatus.BAD_REQUEST),
    INVALID_TIME_JOIN(1026, "Invalid time join", HttpStatus.BAD_REQUEST),
    WRONG_PASSWORD(1027, "Wrong password", HttpStatus.BAD_REQUEST),
    INVALID_ACCOUNT_LOGIN(1028, "Invalid account login", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL(1029, "Invalid email", HttpStatus.BAD_REQUEST),
    EXPIRED_EVENT_SESSION(1030, "Expired event session", HttpStatus.BAD_REQUEST),
    INVALID_TICKET(1031, "Invalid ticket", HttpStatus.BAD_REQUEST),
    WRONG_EVENT(1032, "Wrong event", HttpStatus.BAD_REQUEST),
    CHECKED_IN_TICKET(1033, "Checked in ticket", HttpStatus.BAD_REQUEST),
    EVENT_ON_GOING(1034, "Event on going", HttpStatus.BAD_REQUEST),
    CANNOT_CANCEL_TICKET(1035, "Cannot cancel ticket", HttpStatus.BAD_REQUEST),
    CODE_EXISTS(1036, "Code exists", HttpStatus.BAD_REQUEST),
    NOT_EXPIRED_EVENT_SESSION(1037, "Not Expired event session", HttpStatus.BAD_REQUEST),
    MAX_BOOKING_PER_USER(1038, "Max booking per user", HttpStatus.BAD_REQUEST),
    MIN_TICKET_IN_BOOKING(1039, "Min ticket in booking", HttpStatus.BAD_REQUEST),
    MAX_COUPON_USAGE(1040, "Max coupon usage", HttpStatus.BAD_REQUEST),
    MAX_TICKET_IN_BOOKING(1041, "Max ticket in booking", HttpStatus.BAD_REQUEST),
    EVENT_SERIES_NOT_ACTIVE(1042, "Event series not active", HttpStatus.BAD_REQUEST),
    ATTENDEE_STATUS_INVALID(1043, "Attendee status invalid", HttpStatus.BAD_REQUEST),
    ATTENDEE_TYPE_INVALID(1044, "Attendee type invalid", HttpStatus.BAD_REQUEST),
    ATTENDEE_OWNER_INVALID(1045, "Attendee owner invalid", HttpStatus.BAD_REQUEST),
    RESALE_PRICE_INVALID(1046, "Resale price invalid", HttpStatus.BAD_REQUEST),
    ATTENDEE_SOURCE_TYPE_INVALID(1047, "Attendee source type invalid", HttpStatus.BAD_REQUEST),
    OWNER_CANNOT_BUY(1048, "Owner cannot buy", HttpStatus.BAD_REQUEST),

    ;


    private final int code;
    private final String message;
    private final HttpStatusCode status;

    ErrorCode(int code, String message, HttpStatusCode status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
