package com.brest.bank.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VersionRestController {

    @ResponseBody
    @RequestMapping(value = "version", method= RequestMethod.GET)
    public ResponseEntity<String> getVersion() {
        return new ResponseEntity("1.0", HttpStatus.OK);
    }

}