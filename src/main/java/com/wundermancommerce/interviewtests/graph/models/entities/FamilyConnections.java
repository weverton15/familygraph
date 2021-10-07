package com.wundermancommerce.interviewtests.graph.models.entities;

import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FamilyConnections {

    private Map<People, Set<People>> relationships;
    private Map<People, Set<People>> family;
}