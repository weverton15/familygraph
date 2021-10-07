package com.wundermancommerce.interviewtests.graph.models.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class People {

    private String name;
    private String age;
    private String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        People people = (People) o;

        return getEmail().equals(people.getEmail());
    }

    @Override
    public int hashCode() {
        return getEmail().hashCode();
    }
}
