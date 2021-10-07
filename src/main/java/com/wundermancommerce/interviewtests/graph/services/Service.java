package com.wundermancommerce.interviewtests.graph.services;

import com.google.common.collect.Sets;
import com.wundermancommerce.interviewtests.graph.ImporterCSV;
import com.wundermancommerce.interviewtests.graph.enums.RelationshipType;
import com.wundermancommerce.interviewtests.graph.exceptions.PeopleNotFoundException;
import com.wundermancommerce.interviewtests.graph.models.ImportEntry;
import com.wundermancommerce.interviewtests.graph.models.entities.FamilyConnections;
import com.wundermancommerce.interviewtests.graph.models.entities.People;
import com.wundermancommerce.interviewtests.graph.models.entities.Relationships;
import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Service {

    private Service() {
    }

    public static Set<People> importPeopleData(File peopleFile) {
        log.info("Loading the file {}", peopleFile);
        Set<People> peopleSet = new HashSet<>();
        List<ImportEntry> peopleEntryList = ImporterCSV.importer(peopleFile);
        for (ImportEntry entry : peopleEntryList) {
            peopleSet.add(peopleBuilder(entry));
        }
        log.info("People loaded {}", peopleSet);
        return peopleSet;
    }

    public static Set<Relationships> importRelationshipsData(File relationShipFile) {
        log.info("Loading the file {}", relationShipFile);
        Set<Relationships> relationshipSet = new HashSet<>();
        List<ImportEntry> relationShipEntryList = ImporterCSV.importer(relationShipFile);
        for (ImportEntry entry : relationShipEntryList) {
            relationshipSet.add(relationshipBuilder(entry));
        }
        log.info("Relationships loaded {}", relationshipSet);
        return relationshipSet;
    }

    public static FamilyConnections makeGraphRelations(File peopleFile, File relationshipFile)
        throws PeopleNotFoundException {
        final Set<People> peopleSet = importPeopleData(peopleFile);
        final Set<Relationships> relationshipsSet = importRelationshipsData(relationshipFile);

        FamilyConnections connections = FamilyConnections.builder()
            .family(familyInitializer(peopleSet))
            .relationships(connectionsInitializer(peopleSet))
            .build();

        for (Relationships entry : relationshipsSet) {
            People person = getPerson(peopleSet, entry.getPersonalEmail());
            People connectedPerson = getPerson(peopleSet, entry.getRelativeEmail());
            connections.getRelationships().get(person).add(connectedPerson);
            connections.getRelationships().get(connectedPerson).add(person);

            if (RelationshipType.FAMILY.equals(entry.getRelationship())) {
                addFamilyMember(new HashSet<>(), connections, person, connectedPerson);
            }
        }
        log.info("FamilyConnections loaded {}", connections);
        return connections;

    }

    private static void addFamilyMember(Set<People> processedPeople, FamilyConnections connections,
        People person, People connectedPerson) {
        Set<People> connectedPeople = connections.getFamily().get(person);
        if (processedPeople.containsAll(connectedPeople)) {
            return;
        }
        processedPeople.add(person);
        processedPeople.add(connectedPerson);
        for (People familyMember : connectedPeople) {
            if (!person.equals(familyMember) && !processedPeople.contains(familyMember)) {
                Set<People> family = connections.getFamily().get(familyMember);
                for (People familyFromFamilyMember : family) {
                    addFamilyMember(processedPeople, connections, connectedPerson,
                        familyFromFamilyMember);
                }
            }
        }
        connections.getFamily().get(person).add(connectedPerson);
    }

    private static People getPerson(Set<People> peopleSet, String personalEmail)
        throws PeopleNotFoundException {
        return peopleSet.parallelStream()
            .filter(people -> people.getEmail().equals(personalEmail))
            .findFirst()
            .orElseThrow(
                () -> new PeopleNotFoundException(
                    "Error! People with email " + personalEmail + " not found in people"
                ));
    }


    private static Map<People, Set<People>> familyInitializer(Set<People> people) {
        return people
            .parallelStream()
            .collect(
                Collectors.toMap(entry -> entry, Sets::newHashSet));
    }

    private static Map<People, Set<People>> connectionsInitializer(Set<People> people) {
        return people
            .parallelStream()
            .collect(Collectors.toMap(entry -> entry, entry -> new HashSet<>()));
    }

    private static People peopleBuilder(ImportEntry entry) {
        return People.builder()
            .name(entry.getEntries().get(0))
            .email(entry.getEntries().get(1))
            .age(entry.getEntries().get(2))
            .build();
    }

    private static Relationships relationshipBuilder(ImportEntry entry) {
        return Relationships.builder()
            .personalEmail(entry.getEntries().get(0))
            .relationship(RelationshipType.valueOf(entry.getEntries().get(1)))
            .relativeEmail(entry.getEntries().get(2))
            .build();
    }
}
