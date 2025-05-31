package com.example.tienda_ropa.Interface;

import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.EmailReq;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ModificarContraReq;
import com.example.tienda_ropa.model.RegistrarUsuarioReq;
import com.example.tienda_ropa.model.VerificarCodReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface PyAnyApi {

    @POST("auth")
    Call<AuthResp> obtenerToken(@Body AuthReq authReq);

    @POST("api_registrar_usuario")
    Call<GeneralResp> registrarUsuario(@Header("Authorization") String authorization,
                                    @Body RegistrarUsuarioReq registrarUsuarioReq);

    @POST("api_enviar_codigo")
    Call<GeneralResp> enviarCodigo(@Body EmailReq emailReq);

    @POST("api_verificar_codigo")
    Call<GeneralResp> verificarCodigo(@Body VerificarCodReq verificarCodReq);

    @POST("api_actualizar_password")
    Call<GeneralResp> actualizar_password(@Body ModificarContraReq modificarContraReq);
}
