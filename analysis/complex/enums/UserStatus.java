package com.analysis.test.enums;

public enum UserStatus {
    ACTIVE : ACTIVATE(),
    ACTIVE,
    INACTIVE,
    ACTIVE : REACTIVATE(),
    SUSPENDED,
    INITIAL,
    SUSPENDED : SUSPEND()
}
