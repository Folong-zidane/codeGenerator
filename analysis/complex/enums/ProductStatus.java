package com.analysis.test.enums;

public enum ProductStatus {
    ACTIVE : ACTIVATE(),
    ACTIVE,
    INACTIVE,
    ACTIVE : REACTIVATE(),
    SUSPENDED,
    INITIAL,
    SUSPENDED : SUSPEND()
}
