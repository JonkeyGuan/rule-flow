package com.droolsinaction.loanapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.ruleflow.Condition;
import com.example.ruleflow.Fact;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.KieServices;
import org.kie.api.command.Command;
import org.kie.api.command.KieCommands;
import org.kie.api.runtime.ClassObjectFilter;
import org.kie.api.runtime.ExecutionResults;
import org.kie.server.api.marshalling.MarshallingFormat;
import org.kie.server.api.model.KieServiceResponse.ResponseType;
import org.kie.server.api.model.ServiceResponse;
import org.kie.server.client.KieServicesClient;
import org.kie.server.client.KieServicesConfiguration;
import org.kie.server.client.KieServicesFactory;
import org.kie.server.client.RuleServicesClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTest {

    static final Logger log = LoggerFactory.getLogger(RuleTest.class);

    private static final String droolsUrl = "http://localhost:8080/kie-server/services/rest/server";
    private static final String username = "rhdmAdmin";
    private static final String password = "admin@123";

    private static final MarshallingFormat FORMAT = MarshallingFormat.JSON;

    private static KieServicesConfiguration kieServicesConfig;
    private static KieServicesClient kieServicesClient;

    @Before
    public void setup() {
        kieServicesConfig = KieServicesFactory.newRestConfiguration(droolsUrl, username, password);
        kieServicesConfig.setMarshallingFormat(FORMAT);

        Set<Class<?>> allClasses = new HashSet<Class<?>>();
        allClasses.add(Condition.class);
        allClasses.add(Fact.class);
        kieServicesConfig.addExtraClasses(allClasses);

        kieServicesClient = KieServicesFactory.newKieServicesClient(kieServicesConfig);
    }

    @After
    public void teardown() {

    }

    @Test
    public void test_yes_flow() {

        RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);

        KieCommands commandFactory = KieServices.Factory.get().getCommands();

        List<Command<?>> commands = new ArrayList<>();

        Condition condition = new Condition("yes");
        commands.add(commandFactory.newInsert(condition));

        Fact fact = new Fact("field1", "field2", "field3", "field4");
        commands.add(commandFactory.newInsert(fact));

        Command<?> fireAllRules = commandFactory.newFireAllRules();
        Command<?> process = commandFactory.newStartProcess("RuleFlow.Flow");
        Command<?> getObjectsApplicant = commandFactory.newGetObjects(new ClassObjectFilter(Condition.class),
                "Condition");
        Command<?> getObjectsLoan = commandFactory.newGetObjects(new ClassObjectFilter(Fact.class), "Fact");
        Command<?> dispose = commandFactory.newDispose();
        commands.addAll(Arrays.asList(fireAllRules, process, getObjectsApplicant, getObjectsLoan, dispose));

        Command<?> batchCommand = commandFactory.newBatchExecution(commands);
        ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults("RuleFlow",
                batchCommand);

        if (executeResponse.getType() == ResponseType.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Condition> conditions = (List<Condition>) executeResponse.getResult().getValue("Condition");
            for (Condition c : conditions) {
                log.info("" + c);
            }

            @SuppressWarnings("unchecked")
            List<Fact> facts = (List<Fact>) executeResponse.getResult().getValue("Fact");
            for (Fact f : facts) {
                log.info("" + f);
            }
        }
    }

    @Test
    public void test_no_flow() {

        RuleServicesClient rulesClient = kieServicesClient.getServicesClient(RuleServicesClient.class);

        KieCommands commandFactory = KieServices.Factory.get().getCommands();

        List<Command<?>> commands = new ArrayList<>();

        Condition condition = new Condition("no");
        commands.add(commandFactory.newInsert(condition));

        Fact fact = new Fact("field1", "field2", "field3", "field4");
        commands.add(commandFactory.newInsert(fact));

        Command<?> fireAllRules = commandFactory.newFireAllRules();
        Command<?> process = commandFactory.newStartProcess("RuleFlow.Flow");
        Command<?> getObjectsApplicant = commandFactory.newGetObjects(new ClassObjectFilter(Condition.class),
                "Condition");
        Command<?> getObjectsLoan = commandFactory.newGetObjects(new ClassObjectFilter(Fact.class), "Fact");
        Command<?> dispose = commandFactory.newDispose();
        commands.addAll(Arrays.asList(fireAllRules, process, getObjectsApplicant, getObjectsLoan, dispose));

        Command<?> batchCommand = commandFactory.newBatchExecution(commands);
        ServiceResponse<ExecutionResults> executeResponse = rulesClient.executeCommandsWithResults("RuleFlow",
                batchCommand);

        if (executeResponse.getType() == ResponseType.SUCCESS) {
            @SuppressWarnings("unchecked")
            List<Condition> conditions = (List<Condition>) executeResponse.getResult().getValue("Condition");
            for (Condition c : conditions) {
                log.info("" + c);
            }

            @SuppressWarnings("unchecked")
            List<Fact> facts = (List<Fact>) executeResponse.getResult().getValue("Fact");
            for (Fact f : facts) {
                log.info("" + f);
            }
        }
    }
}