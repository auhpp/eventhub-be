package com.auhpp.event_management.constant;

import lombok.Getter;

@Getter
public enum WalletTransactionDescription {
    BOOKING("Tiền từ đơn hàng "),
    RESELL_BOOKING("Tiền từ đơn hàng bán lại vé "),
    WITHDRAWAL_REQUEST("Rút tiền ");

    private final String value;

    WalletTransactionDescription(String value) {
        this.value = value;
    }
}
