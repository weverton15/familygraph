package com.wundermancommerce.interviewtests.graph.services;

import com.wundermancommerce.interviewtests.graph.exceptions.PeopleNotFoundException;
import com.wundermancommerce.interviewtests.graph.models.entities.FamilyConnections;
import com.wundermancommerce.interviewtests.graph.models.entities.People;
import com.wundermancommerce.interviewtests.graph.models.entities.Relationships;
import com.wundermancommerce.interviewtests.graph.utils.FileUtils;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ServiceTest {

    private final String PEOPLE_CSV_FILE = "people.csv";
    private final String RELATIONSHIPS_CSV_FILE = "relationships.csv";
    private File relationshipFile = null;
    private File peopelFile = null;

    @Before
    public void setUp() throws URISyntaxException {
        relationshipFile = FileUtils.getFile(RELATIONSHIPS_CSV_FILE);
        peopelFile = FileUtils.getFile(PEOPLE_CSV_FILE);
    }

    @Test
    public void import_People_File_Success() throws URISyntaxException {
        Set<People> people = Service.importPeopleData(peopelFile);
        Assert.assertEquals(12, people.size());
    }

    @Test
    public void import_Relationship_File_Success() throws URISyntaxException {
        Set<Relationships> relationships = Service.importRelationshipsData(relationshipFile);
        Assert.assertEquals(16, relationships.size());
    }

    @Test
    public void count_number_connection_success()
        throws URISyntaxException, PeopleNotFoundException {
        final FamilyConnections connections = Service.makeGraphRelations(peopelFile,
            relationshipFile);
        Set<People> bob = connections.getRelationships().get(makeExpectedPeople("bob@bob.com"));
        Assert.assertEquals(4, bob.size());

        Set<People> jenny = connections.getRelationships()
            .get(makeExpectedPeople("jenny@toys.com"));
        Assert.assertEquals(3, jenny.size());

        Set<People> nigel = connections.getRelationships()
            .get(makeExpectedPeople("nigel@marketing.com"));
        Assert.assertEquals(2, nigel.size());

        Set<People> alan = connections.getRelationships()
            .get(makeExpectedPeople("alan@lonely.org"));
        Assert.assertEquals(0, alan.size());
    }

    @Test
    public void count_number_extended_family_success()
        throws URISyntaxException, PeopleNotFoundException {
        final FamilyConnections connections = Service.makeGraphRelations(peopelFile,
            relationshipFile);

        Set<People> jenny = connections.getFamily().get(makeExpectedPeople("jenny@toys.com"));
        Assert.assertEquals(3, jenny.size());

        Set<People> bob = connections.getFamily().get(makeExpectedPeople("bob@bob.com"));
        Assert.assertEquals(4, bob.size());

        Set<People> nigel = connections.getFamily().get(makeExpectedPeople("nigel@marketing.com"));
        Assert.assertEquals(1, nigel.size());

        Set<People> anna = connections.getFamily().get(makeExpectedPeople("anna@clothes.com"));
        Assert.assertEquals(3, anna.size());

        Set<People> alan = connections.getFamily().get(makeExpectedPeople("alan@lonely.org"));
        Assert.assertEquals(1, alan.size());
    }

    private People makeExpectedPeople(String email) {
        return People.builder()
            .email(email)
            .build();
    }
}
