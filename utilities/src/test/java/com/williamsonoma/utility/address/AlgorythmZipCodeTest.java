package com.williamsonoma.utility.address;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.williamsonoma.api.model.address.ZipCodeRange;
import com.williamsonoma.api.utility.address.ZipCode;
import com.williamsonoma.utility.address.AlgorythmZipCode;

public class AlgorythmZipCodeTest
{
    private ZipCode zipCodeUtility;
    private static final Logger logger = LogManager.getLogger(AlgorythmZipCodeTest.class);

    @Before
    public void setUp()
    {
        zipCodeUtility = new AlgorythmZipCode();
    }

    @Test
    public void compactOneToOne()
    {
        logger.info("Test compacting one range into one range successfully");
        var ranges = Arrays.asList(
               new ZipCodeRange("00010", "00015"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 1);

        var compactedRanges = result.toArray(new ZipCodeRange[0]);
        assertEquals(compactedRanges[0].getLower(), "00010");
        assertEquals(compactedRanges[0].getUpper(), "00015");
    }

    @Test
    public void compactTwoToTwo()
    {
        logger.info("Test compacting two ranges into two ranges successfully");
        var ranges = Arrays.asList(
               new ZipCodeRange("00000", "00005"),
               new ZipCodeRange("00007", "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 2);

        var compactedRanges = result.toArray(new ZipCodeRange[0]);
        assertEquals(compactedRanges[0].getLower(), "00000");
        assertEquals(compactedRanges[0].getUpper(), "00005");
        assertEquals(compactedRanges[1].getLower(), "00007");
        assertEquals(compactedRanges[1].getUpper(), "00009");
    }

    @Test
    public void compactZero()
    {
        logger.info("Test compacting an empty range set into an empty range set successfully");
        var ranges = new ArrayList<ZipCodeRange>();
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 0);
    }

    @Test
    public void compactThreeToTwo()
    {
        logger.info("Test compacting three out-of-order ranges into two ranges successfully");
        var ranges = Arrays.asList(
               new ZipCodeRange("00009", "00011"),
               new ZipCodeRange("00000", "00005"),
               new ZipCodeRange("00003", "00007"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 2);

        var compactedRanges = result.toArray(new ZipCodeRange[0]);
        assertEquals(compactedRanges[0].getLower(), "00000");
        assertEquals(compactedRanges[0].getUpper(), "00007");
        assertEquals(compactedRanges[1].getLower(), "00009");
        assertEquals(compactedRanges[1].getUpper(), "00011");
    }

    @Test
    public void compactThreeAdjacentToOne()
    {
        logger.info("Test compacting three adjacent ranges into one range successfully");
        var ranges = Arrays.asList(
               new ZipCodeRange("00006", "00008"),
               new ZipCodeRange("00009", "00011"),
               new ZipCodeRange("00003", "00006"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 1);

        var compactedRanges = result.toArray(new ZipCodeRange[0]);
        assertEquals(compactedRanges[0].getLower(), "00003");
        assertEquals(compactedRanges[0].getUpper(), "00011");
    }

    @Test(expected = IllegalArgumentException.class)
    public void compactRangesWithNullLowerBound()
    {
        logger.info("Test null value in lower bound expecting IllegalArgumentException");
        var ranges = Arrays.asList(
               new ZipCodeRange("00000", "00005"),
               new ZipCodeRange(null, "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compactRangesWithNullUpperBound()
    {
        logger.info("Test null value in upper bound expecting IllegalArgumentException");
        var ranges = Arrays.asList(
               new ZipCodeRange("00000", null),
               new ZipCodeRange("00007", "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compactRangesWithInvalidLowerZipCode()
    {
        logger.info("Test invalid zip code in lower bound expecting IllegalArgumentException");
        var ranges = Arrays.asList(
               new ZipCodeRange("0001", "00005"),
               new ZipCodeRange("00007", "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compactRangesWithInvalidUpperZipCode()
    {
        logger.info("Test invalid zip code in upper bound expecting IllegalArgumentException");
        var ranges = Arrays.asList(
               new ZipCodeRange("00000", "+0005"),
               new ZipCodeRange("00007", "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void compactRangesWithLowerGreaterThanUpperBound()
    {
        logger.info("Test lower bound greater than upper bound expecting IllegalArgumentException");
        var ranges = Arrays.asList(
               new ZipCodeRange("00003", "00002"),
               new ZipCodeRange("00007", "00009"));
        printRanges("   ranges before", ranges);

        var result = zipCodeUtility.compactRanges(ranges);
        printRanges("   ranges after", result);
    }


    @Test()
    public void compactRangesWithNull()
    {
        logger.info("Test compacting ranges with null input expecting empty collection output");
        logger.info("   ranges before null");

        var result = zipCodeUtility.compactRanges(null);
        printRanges("   ranges after", result);

        assertTrue(result.size() == 0);
    }

    private void printRanges(String logMsg, Collection<ZipCodeRange> ranges)
    {
        if (ranges == null)
        {
            logger.info("{} null", logMsg);
        }
        else if (ranges.size() == 0)
        {
            logger.info("{} empty", logMsg);
        }
        else
        {
            logger.info("{}", logMsg);
            for (var range : ranges)
            {
                logger.info("      {} to {}", range.getLower(), range.getUpper());
            }
        }
    }
}
