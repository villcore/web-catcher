package com.villcore.web;

import com.villcore.web.rest.ManagerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;

public class EntryPoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryPoint.class);

    public static void main(String[] args) {
        LOGGER.info("start running.");

        //spring boot start.
        SpringApplication.run(ManagerController.class, args);
    }
}
