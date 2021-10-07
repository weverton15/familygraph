package com.wundermancommerce.interviewtests.graph.enums;

public enum RelationshipType {
    FAMILY("FAMILY"),
    FRIEND("FRIEND");

    private final String relationship;

    RelationshipType(String relationship) {
        this.relationship = relationship;
    }
}
