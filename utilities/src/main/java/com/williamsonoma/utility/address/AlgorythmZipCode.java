package com.williamsonoma.utility.address;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import com.williamsonoma.api.model.address.ZipCodeRange;
import com.williamsonoma.api.utility.address.ZipCode;

/**
 * Author: Kris Jornlin
 * Date: 7/7/2018
 */
@Service
public class AlgorythmZipCode implements ZipCode
{
    private enum BoundType { Lower, Upper };

    private Boolean processZipCodesInParallel;

    private static final String zipRx = "[0-9]{5}";
    private static final Logger logger = LogManager.getLogger(AlgorythmZipCode.class);

    public AlgorythmZipCode(Boolean processZipCodesInParallel)
    {
        this.processZipCodesInParallel = processZipCodesInParallel;
    }

    public Collection<ZipCodeRange> compactRanges(Collection<ZipCodeRange> ranges) throws IllegalArgumentException
    {
        if (ranges == null)
        {
            logger.warn("compactRanges called with null zip code range, returning empty range collection");
            return new ArrayList<ZipCodeRange>();
        }

        logRanges(ranges, "compactRanges called with ranges");

        // Synchronize the TreeSet and use java 8 streams to add each lower and upper bound separately.
        // At the end traverse the tree and return the new compacted ranges.

        var rangeBounds = Collections.synchronizedSortedSet(new TreeSet<Bound>(new Comparator<Bound>()
            {
                @Override
                public int compare(Bound a, Bound b)
                {
                    if (a.value == b.value)
                    {
                        return a.type.compareTo(b.type);
                    }
    
                    return a.value.compareTo(b.value);
                };
            }));

        if (processZipCodesInParallel)
        {
            ranges.parallelStream().forEach(x -> addBounds(x, rangeBounds));
        }
        else
        {
            ranges.forEach(x -> addBounds(x, rangeBounds));
        }

        return extractRanges(rangeBounds);
    }

    private Collection<ZipCodeRange> extractRanges(SortedSet<Bound> rangeBounds)
    {
        var compactedRanges = new ArrayList<ZipCodeRange>();

        var lower = -1;
        var upper = -1;
        for (var bound : rangeBounds)
        {
            if (bound.isLower())
            {
                logger.debug("lower bound {} upper {}", bound.getValue(), upper);
                if (checkAndAddNewRange(lower, upper, bound.getValue(), compactedRanges) || lower < 0)
                {
                    lower = bound.getValue();
                }

                upper = -1;
            }

            if (bound.isUpper())
            {
                logger.debug("upper bound {}", bound.getValue());
                upper = bound.getValue();
            }
        }

        checkAndAddNewRange(lower, upper, -1, compactedRanges);

        return compactedRanges;
    }

    private boolean checkAndAddNewRange(int lower, int upper, int nextLower, List<ZipCodeRange> newRanges)
    {
        var rangeAdded = false;
        if (lower > -1 && upper > -1)
        {
            // Make sure that adjacent ranges get compacted, ie: 1-3 and 4-7 should be one range of 1-7
            logger.debug("checking for adjacent range");
            if (nextLower > upper + 1 || nextLower < 0)
            {
                rangeAdded = true;

                var lowerZip = String.format("%05d", lower);
                var upperZip = String.format("%05d", upper);

                logger.debug("adding new range {} {}", lowerZip, upperZip);
                newRanges.add(new ZipCodeRange(lowerZip, upperZip));
            }
        }

        return rangeAdded;
    }

    private void addBounds(ZipCodeRange range, SortedSet<Bound> rangeBounds) throws IllegalArgumentException
    {
        if (range.getLower() == null || range.getUpper() == null)
        {
            throw new IllegalArgumentException("Zip code range cannot have null as the upper or lower bound.");
        }

        if (!Pattern.matches(zipRx, range.getLower()) || !Pattern.matches(zipRx, range.getUpper()))
        {
            throw new IllegalArgumentException("Zip code values must be 5 digits.");
        }

        var lower = NumberUtils.toInt(range.getLower(), -1);
        var upper = NumberUtils.toInt(range.getUpper(), -1);

        if (lower > upper)
        {
            throw new IllegalArgumentException("Zip code range lower bound cannot be greater than upper bound.");
        }

        rangeBounds.add(new Bound(lower, BoundType.Lower));
        rangeBounds.add(new Bound(upper, BoundType.Upper));
    }

    private void logRanges(Collection<ZipCodeRange> ranges, String logMsg)
    {
        logger.debug(logMsg);
        if (ranges != null)
        {
            for (var range : ranges)
            {
                logger.debug("   {} to {}", range.getLower(), range.getUpper());
            }
        }
        else
        {
            logger.debug("   null");
        }
    }

    private class Bound
    {
        private Integer value;
        private BoundType type;

        public Bound(Integer x, BoundType y)
        {
            value = x;
            type = y;
        }

        public boolean isLower()
        {
            return type == BoundType.Lower;
        }

        public boolean isUpper()
        {
            return type == BoundType.Upper;
        }

        public Integer getValue()
        {
            return value;
        }

        public void setValue(Integer x) 
        {
            value = x;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj != null && this != null && obj instanceof Bound)
            {
                var bound = (Bound) obj;
                return this.value == bound.value && this.type == bound.type;
            }

            return false;
        }

        @Override
        public int hashCode()
        {
            return Objects.hash(value, type);
        }
    }
}
