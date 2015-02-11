package com.brest.bank.dao;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

public class JUnitRule implements TestRule {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Statement apply(final Statement base, Description description){
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                LOGGER.debug("JUnit rule - run test ");

                base.evaluate();

                LOGGER.debug("JUnit rule - stop test");
            }
        };
    };
}