package com.ssudio.sfr.network.response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ssudio.sfr.network.response.SFRApiGetResponse;
import com.ssudio.sfr.report.model.ReportRequestModel;
import com.ssudio.sfr.report.model.ReportResponseModel;

import org.junit.Assert;
import org.junit.Test;

public class SFRApiGetResponseWithGeneralClassOfTypeReportResponseModelTest {
    @Test
    public void deserialize_shouldReturnSize2_whenGivenJsonWithDataSize2() {
        String result = "{\"data\":" +
                "[{\"date\":\"2015-05-06T16:00:00.000Z\",\"nama\":\"Tukiyem\",\"amount\":5000,\"currency\":\"IDR\"}," +
                "{\"date\":\"2015-05-07T16:00:00.000Z\",\"nama\":\"an1dre\",\"amount\":10000,\"currency\":\"IDR\"}]," +
                "\"code\":\"00\",\"status\":\"\"}";

        Gson gson = new Gson();

        SFRApiGetResponse<ReportRequestModel> test = gson.fromJson(result,
                new TypeToken<SFRApiGetResponse<ReportResponseModel>>(){}.getType());

        Assert.assertEquals(test.getData().size(), 2);
    }

    @Test
    public void deserialize_shouldGetItemOnIndexZeroWithValueTypeIsReportResponseModel_whenGivenJsonWithDataSize2() {
        String result = "{\"data\":" +
                "[{\"date\":\"2015-05-06T16:00:00.000Z\",\"nama\":\"Tukiyem\",\"amount\":5000,\"currency\":\"IDR\"}," +
                "{\"date\":\"2015-05-07T16:00:00.000Z\",\"nama\":\"an1dre\",\"amount\":10000,\"currency\":\"IDR\"}]," +
                "\"code\":\"00\",\"status\":\"\"}";

        Gson gson = new Gson();

        SFRApiGetResponse<ReportRequestModel> test = gson.fromJson(result,
                new TypeToken<SFRApiGetResponse<ReportResponseModel>>(){}.getType());

        ReportResponseModel testReportResponse = (ReportResponseModel) (test.getData().toArray())[0];

        Assert.assertEquals(testReportResponse.getName(), "Tukiyem");
    }
}
