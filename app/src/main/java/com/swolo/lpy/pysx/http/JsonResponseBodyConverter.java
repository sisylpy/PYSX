package com.swolo.lpy.pysx.http;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2016/9/8 0008.
 */

public class JsonResponseBodyConverter implements Converter<ResponseBody, CommonResponse> {
    @Override
    public CommonResponse convert(ResponseBody value) throws IOException {
        CommonResponse commonResponse = new CommonResponse();
        String response=value.string();
        try {
            JSONObject jsonObject = new JSONObject(response);
//            commonResponse.success = jsonObject.optBoolean("Success");
            commonResponse.data = jsonObject.optString("data");
            commonResponse.code = jsonObject.getInt("code");
            commonResponse.msg = jsonObject.optString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return commonResponse;
    }
}
