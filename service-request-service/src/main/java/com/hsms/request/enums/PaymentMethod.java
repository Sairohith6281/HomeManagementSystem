package com.hsms.request.enums;

public enum PaymentMethod {

    UPI("UPI"),
    CARD("Credit/Debit Card"),
    WALLET("Digital Wallet"),
    NET_BANKING("Net Banking"),
    CASH("Cash");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
