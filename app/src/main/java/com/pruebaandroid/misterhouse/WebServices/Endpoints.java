package com.pruebaandroid.misterhouse.WebServices;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Andres on 09/09/2016.
 */
public class Endpoints {

    /*username:mario
     Password:koombea*/
    public static final String BASEURL = "https://api.parse.com";
    public static final String LOGIN = "/1/login?username=%s&password=%s";
    public static final String PRODUCTS ="/1/classes/products";
    public static final String REST_API_KEY ="bsiWfUOJ7vhiongFPQM9PTmeqCUoKJZM8HezfZGx";
    public static final String APLICATION_ID ="UioTROrtK4RxrhKB7n2k2hOXEfTyScQNto9I0zTV";

    public static Map<String, String> getHeaders()
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("X-Parse-REST-API-Key", REST_API_KEY);
        params.put("X-Parse-Application-Id", APLICATION_ID);
        params.put("Content-Type", "application/json");
        return params;
    }

}
