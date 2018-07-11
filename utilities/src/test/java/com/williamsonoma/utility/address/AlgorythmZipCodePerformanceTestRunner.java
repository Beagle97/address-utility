package com.williamsonoma.utility.address;

import java.lang.Math;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.Random;
import com.williamsonoma.api.model.address.ZipCodeRange;
import com.williamsonoma.api.utility.address.ZipCode;
import com.williamsonoma.utility.address.AlgorythmZipCode;

/**
 * Author: Kris Jornlin
 * Date: 7/7/2018
 *
 * Run performance tests for compacting zip code ranges.
 */
public class AlgorythmZipCodePerformanceTestRunner
{
    public static String runTest(ZipCode zipCode, int size)
    {
        var ranges = generateRanges(size);
        long start = System.nanoTime();

        var result = zipCode.compactRanges(ranges);
    	String elapsedTime = elapsedTime(start);

        return elapsedTime;
    }

    private static String elapsedTime(long start)
    {
        long elapsed = System.nanoTime() - start;
        long seconds = TimeUnit.NANOSECONDS.toSeconds(elapsed);
        long millis = TimeUnit.NANOSECONDS.toMillis(elapsed) - TimeUnit.SECONDS.toMillis(seconds);
        long nanos = TimeUnit.NANOSECONDS.toNanos(elapsed) - TimeUnit.SECONDS.toNanos(seconds) - TimeUnit.MILLISECONDS.toNanos(millis);

        return String.format("%d sec, %d milli, %d nano", seconds, millis, nanos);
    }

    private static Collection<ZipCodeRange> generateRanges(int size)
    {
        var ranges = new ArrayList<ZipCodeRange>(size);
        var random = new Random();
        while (size-- > 0)
        {
            var lower = random.nextInt(100000);
            var upper = lower + (99999 - lower) % 1000;

            ranges.add(
                    new ZipCodeRange(
                        String.format("%05d", lower), 
                        String.format("%05d", upper)));
        }

        return ranges;
    }
}
