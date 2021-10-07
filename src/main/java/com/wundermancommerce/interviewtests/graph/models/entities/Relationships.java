package com.wundermancommerce.interviewtests.graph.models.entities;

import com.wundermancommerce.interviewtests.graph.enums.RelationshipType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Relationships {

    private String personalEmail;
    private RelationshipType relationship;
    private String relativeEmail;
}
