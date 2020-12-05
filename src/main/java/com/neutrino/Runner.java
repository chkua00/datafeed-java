package com.neutrino;

import com.neutrino.service.CmeQuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Runner implements CommandLineRunner {

    @Autowired
    private CmeQuoteService cmeQuoteService;

    @Override
    public void run(String... args) throws Exception {
        cmeQuoteService.downloadQuotes();
        cmeQuoteService.printAllData();
    }

}
