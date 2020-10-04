package com.rajeevn.languageperformance.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class AppController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppController.class);
    private static final ExecutorService DELETE_SERVICE = Executors.newSingleThreadExecutor();

    ObjectMapper mapper = new ObjectMapper();

    public static class MonthlyDetail
    {
        public Integer month;
        public Integer interest;
        public Integer balance;

        public MonthlyDetail() {}

        public MonthlyDetail(Integer month, Integer interest, Integer balance)
        {
            this.month = month;
            this.interest = interest;
            this.balance = balance;
        }
    }

    @GetMapping("/calculate/monthly/interest")
    public List<MonthlyDetail> calculateMonthlyInterest(
            @RequestParam("months") Integer months,
            @RequestParam("principalAmount") Integer principalAmount,
            @RequestParam("annualRate") Integer annualRate
    )
    {
        List<MonthlyDetail> result = new ArrayList<>(months);
        for (int monthNum = 1; monthNum <= months; monthNum++)
        {
            Integer interest = principalAmount * annualRate / 12 / 100;
            principalAmount += interest;
            result.add(new MonthlyDetail(monthNum, interest, principalAmount));
        }
        return result;
    }

    @PostMapping("/save")
    public JsonNode saveAndFetch(@RequestBody JsonNode json) throws IOException
    {
        LOGGER.info("creating unique file name");
        String filePath = UUID.randomUUID().toString();

        try
        {
            writeJsonToFile(filePath, json);
            return readJsonFromFile(filePath);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage());
            throw e;
        }
        finally
        {
            deleteFileQuietly(filePath);
        }
    }

    private void writeJsonToFile(String filePath, JsonNode json) throws IOException
    {
        LOGGER.info("writing data to file");
        mapper.writeValue(Paths.get(filePath).toFile(), json);
    }

    private JsonNode readJsonFromFile(String filePath) throws IOException
    {
        LOGGER.info("reading data from file name");
        return mapper.readValue(Paths.get(filePath).toFile(), JsonNode.class);
    }

    private void deleteFileQuietly(String filePath)
    {
        try
        {
            DELETE_SERVICE.submit(() ->
            {
                try
                {
                    Files.delete(Paths.get(filePath));
                } catch (IOException e)
                {
                    LOGGER.error("failed to delete file {}", filePath);
                }
            });
        }
        catch (Exception e) {}
    }
}
