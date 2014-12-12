package com.epam.brest.courses.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

/**
 * Created by sberdachuk on 11/12/14.
 */
public class RestClientDemo {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException {

        RestClient restClient = new RestClient("http://localhost:8080");
        String version = restClient.getRestVesrsion();
        LOGGER.debug("Rest version is {}", version);

    }
}
