package com.wundermancommerce.interviewtests.graph.models;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImportEntry {

    private Integer line;
    private List<String> entries;
}
