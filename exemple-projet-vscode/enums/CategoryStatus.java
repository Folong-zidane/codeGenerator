package com.example.blog.enums;

public enum CategoryStatus {
    APPROVED : APPROVE(),
    DRAFT,
    INITIAL,
    PUBLISHED : RESTORE(),
    REJECTED : REJECT(),
    DRAFT : REVISE(),
    PENDING_REVIEW,
    PUBLISHED,
    PENDING_REVIEW : SUBMIT(),
    ARCHIVED : ARCHIVE(),
    ARCHIVED,
    APPROVED,
    PUBLISHED : PUBLISH(),
    REJECTED
}
