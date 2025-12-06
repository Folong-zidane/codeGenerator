package com.analysis.test.enums;

public enum OrderStatus {
    ACTIVE : ACTIVATE(),
    ACTIVE,
    INACTIVE,
    ACTIVE : REACTIVATE(),
    SUSPENDED,
    INITIAL,
    SUSPENDED : SUSPEND()
}
