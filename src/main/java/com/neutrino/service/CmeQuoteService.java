package com.neutrino.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutrino.model.Quote;
import com.neutrino.repository.QuoteRepository;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmeQuoteService {

    @Autowired
    private QuoteRepository quoteRepository;

    public boolean downloadQuotes() throws JsonProcessingException, URISyntaxException, IOException {

        URL url = new URL("https://www.cmegroup.com/CmeWS/mvc/Quotes/Future/133/G?quoteCodes=null");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readValue(url, JsonNode.class);
        String strDate = node.findValue("tradeDate").asText();
        JsonNode quoteNodes = node.findValue("quotes");

        System.out.println();
        System.out.println("Raw Result:");
        System.out.println(node);
        System.out.println();

        List<Quote> quotes = new ArrayList();

        System.out.println("Raw Quotes:");
        for (JsonNode quoteNode : quoteNodes) {

            System.out.println(quoteNode);

            String code = quoteNode.findValue("code").textValue();
            LocalDate date = LocalDate.parse(strDate, DateTimeFormatter.ofPattern("dd MMM yyyy")); // 04 Dec 2020
            String open = quoteNode.findValue("open").textValue();
            String high = quoteNode.findValue("high").textValue();
            String low = quoteNode.findValue("low").textValue();
            String close = quoteNode.findValue("priorSettle").textValue();
            String volume = quoteNode.findValue("volume").textValue().replace(",", "");

            if (isBigDecimal(open) && isBigDecimal(high) && isBigDecimal(low) && isBigDecimal(close)) {
                Quote quote = new Quote();
                quote.setCode(code);
                quote.setDate(date);
                quote.setOpen(new BigDecimal(open));
                quote.setHigh(new BigDecimal(high));
                quote.setLow(new BigDecimal(low));
                quote.setClose(new BigDecimal(close));
                quote.setVolume(new BigDecimal(volume));
                quotes.add(quote);
            }
        }

        quoteRepository.saveAll(quotes);

        return true;
    }

    public boolean printAllData() {
        List<Quote> quotes = quoteRepository.findAll();
        System.out.println();
        System.out.println("Quotes stored in Database:");
        for (Quote quote : quotes) {
            System.out.println(quote);
        }
        System.out.println();
        return true;
    }

    public static boolean isBigDecimal(String value) {
        if (value == null) {
            return false;
        }
        try {
            BigDecimal d = new BigDecimal(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
