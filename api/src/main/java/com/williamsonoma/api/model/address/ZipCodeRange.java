package com.williamsonoma.api.model.address;

/**
 * Author: Kris Jornlin
 * Date: 7/7/2018
 */
public class ZipCodeRange implements java.io.Serializable
{
    private String lower;
    private String upper;

    public ZipCodeRange()
    {
    }

    public ZipCodeRange(String x, String y)
    {
        lower = x;
        upper = y;
    }

    public String getLower()
    {
        return lower;
    }

    public void setLower(String x)
    {
        lower = x;
    }

    public String getUpper()
    {
        return upper;
    }

    public void setUpper(String x)
    {
        upper = x;
    }
}
