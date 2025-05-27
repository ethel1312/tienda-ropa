package com.example.tienda_ropa.Interface;

import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PyAnyApi {

    @POST("auth")
    Call<AuthResp> obtenerToken(@Body AuthReq authReq);

}
