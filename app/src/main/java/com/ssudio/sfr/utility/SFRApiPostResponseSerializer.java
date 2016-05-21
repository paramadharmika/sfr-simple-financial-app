package com.ssudio.sfr.utility;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ssudio.sfr.network.response.SFRApiPostResponse;

import java.lang.reflect.Type;

//correct serialization of inherited class, refer to
// link http://stackoverflow.com/questions/16220010/gson-cant-deserialize-inherited-class
public class SFRApiPostResponseSerializer implements JsonDeserializer<SFRApiPostResponse> {
    @Override
    public SFRApiPostResponse deserialize(JsonElement je, Type type, JsonDeserializationContext jdc)
            throws JsonParseException {

        JsonObject jo = je.getAsJsonObject().getAsJsonObject("data");

        Gson g = new Gson();

        return g.fromJson(jo, SFRApiPostResponse.class);
    }
}
