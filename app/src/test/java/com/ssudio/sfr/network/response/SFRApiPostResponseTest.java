package com.ssudio.sfr.network.response;

import com.google.gson.Gson;
import com.ssudio.sfr.network.request.SFRNetworkRequestType;

import org.junit.Assert;
import org.junit.Test;

public class SFRApiPostResponseTest {
    @Test
    public void deserialize_shouldReturnInsertIdPropertyWith15_whenGivenJsonStringWithInsertId15() {
        String result = "{\"data\":{\"fieldCount\":0,\"affectedRows\":1,\"insertId\":15,\"serverStatus\":2,\"warningCount\":0,\"message\":\"\",\"protocol41\":true,\"changedRows\":0},\"code\":\"00\",\"status\":\"\"}";

        Gson gson = new Gson();

        SFRApiPostResponse apiResponse = gson.fromJson(result, SFRApiPostResponse.class);

        Assert.assertEquals(15, apiResponse.getData().getInsertId());
    }

    @Test
    public void deserialize_shouldReturnIsSuccessTrue_whenGivenJsonString() {
        String result = "{\"data\":{\"fieldCount\":0,\"affectedRows\":1,\"insertId\":15,\"serverStatus\":2,\"warningCount\":0,\"message\":\"\",\"protocol41\":true,\"changedRows\":0},\"code\":\"00\",\"status\":\"\"}";

        Gson gson = new Gson();

        SFRApiPostResponse apiResponse = gson.fromJson(result, SFRApiPostResponse.class);

        apiResponse.setOperation(SFRNetworkRequestType.Insert);

        Assert.assertEquals(true, apiResponse.isSuccess());
    }
}
