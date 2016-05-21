package com.ssudio.sfr.utility;

import org.junit.Assert;
import org.junit.Test;

public class DateUtilityTest {
    @Test
    public void parseToApplicationDateFormat_shouldParseExpected_ddMMMyyyy_whenGivenGMTStringFormat() {
        String feedDate = "2016-01-01T05:00:00.00Z";

        String parsedDate = DateUtility.parseToApplicationDateFormat(feedDate);

        Assert.assertEquals(parsedDate, "Fri, 01 Jan 2016");
    }
}
