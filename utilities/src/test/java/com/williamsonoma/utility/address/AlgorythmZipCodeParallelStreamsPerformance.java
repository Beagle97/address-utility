package com.williamsonoma.utility.address;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.williamsonoma.api.model.address.ZipCodeRange;
import com.williamsonoma.api.utility.address.ZipCode;
import com.williamsonoma.utility.address.AlgorythmZipCode;

/**
 * Author: Kris Jornlin
 * Date: 7/7/2018
 *
 * Test performance of Java parallel streams with different sample sizes.
 */
@RunWith(SpringRunner.class)
@ContextConfiguration
@SpringBootTest
public class AlgorythmZipCodeParallelStreamsPerformance
{
    @Configuration
    static class Config
    {
        @Bean
        public ZipCode zipCode() {
            return new AlgorythmZipCode(true);
        }
    }

    @Autowired
    private ZipCode zipCode;

    private static final Logger logger = LogManager.getLogger(AlgorythmZipCodeParallelStreamsPerformance.class);

    @Test
    public void compactOneThousandInParallel()
    {
        logger.info("Begin parallel streams performance test for one thousand ranges");

        String elapsedTime = AlgorythmZipCodePerformanceTestRunner.runTest(zipCode, 1000);
        logger.info("Finished in {}", elapsedTime);
    }

    @Test
    public void compactTenThousandInParallel()
    {
        logger.info("Begin parallel streams performance test for ten thousand ranges");

        String elapsedTime = AlgorythmZipCodePerformanceTestRunner.runTest(zipCode, 10000);
        logger.info("Finished in {}", elapsedTime);
    }

    @Test
    public void compactHundredThousandInParallel()
    {
        logger.info("Begin parallel streams performance test for one hundred thousand ranges");

        String elapsedTime = AlgorythmZipCodePerformanceTestRunner.runTest(zipCode, 100000);
        logger.info("Finished in {}", elapsedTime);
    }

    @Test
    public void compactOneMillionInParallel()
    {
        logger.info("Begin parallel streams performance test for one million ranges");

        String elapsedTime = AlgorythmZipCodePerformanceTestRunner.runTest(zipCode, 1000000);
        logger.info("Finished in {}", elapsedTime);
    }

    @Test
    public void compactFiveMillionInParallel()
    {
        logger.info("Begin parallel streams performance test for five million ranges");

        String elapsedTime = AlgorythmZipCodePerformanceTestRunner.runTest(zipCode, 5000000);
        logger.info("Finished in {}", elapsedTime);
    }
}
