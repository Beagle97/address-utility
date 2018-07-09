package com.williamsonoma.api.utility.address;

import java.util.Collection;
import com.williamsonoma.api.model.address.ZipCodeRange;

/**
 * Author: Kris Jornlin
 * Date: 7/7/2018
 */
public interface ZipCode
{
    /**
     * Compact zip code address ranges. Represent the same restrictions as the ranges passed in using the minimum number of ranges required.
     *
     * @param ranges The collection of zip code ranges to be compacted.
     * @return A compacted collection of zip code ranges.
     * @see Collection<ZipCodeRange>
     */
    Collection<ZipCodeRange> compactRanges(Collection<ZipCodeRange> ranges) throws IllegalArgumentException;
}
