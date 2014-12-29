package com.brest.bank.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RestClientDemo {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {

        RestClient restClient = new RestClient("http://localhost:8080");
        String version = restClient.getRestVersion();
        LOGGER.debug("Rest version is {}", version);

    }
}
