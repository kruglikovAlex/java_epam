package com.brest.bank.client;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.client.RestTemplate;

public class RestClientVersion {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void main(String[] args) throws IOException{
        RestClient restClient = new RestClient();
        restClient.setHost("http://localhost:8080/SpringHibernateBDeposit-1.0/rest");
        String version = restClient.getRestVersion();
        LOGGER.debug("Rest client version is {}",version);
    }
}
