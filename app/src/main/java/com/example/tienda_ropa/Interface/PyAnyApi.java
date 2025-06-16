package com.example.tienda_ropa.Interface;

import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.example.tienda_ropa.model.ObtenerListaDeseosResp;
import com.example.tienda_ropa.model.RegistrarUsuarioReq;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PyAnyApi {

    @POST("auth")
    Call<AuthResp> obtenerToken(@Body AuthReq authReq);

    @POST("api_registrar_usuario")
    Call<GeneralResp> registrarUsuario(@Header("Authorization") String authorization,
                                    @Body RegistrarUsuarioReq registrarUsuarioReq);

    @GET("api_obtener_carrito/{idUsuario}")
    Call<ObtenerCarritoResp> obtenerCarrito(@Path("idUsuario") int idUsuario,
                                            @Header("Authorization") String authorization);

    @POST("api_incrementar_cantidad")
    Call<GeneralResp> incrementarCantProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    @POST("api_decrementar_cantidad")
    Call<GeneralResp> disminuirCantProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    @POST("api_eliminar_producto")
    Call<GeneralResp> eliminarProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    // API para obtener la lista de deseos
    @GET("api_obtener_lista_Deseos/{idUsuario}")
    Call<ObtenerListaDeseosResp> obtenerListaDeseos( @Path("idUsuario") int idUsuario,
                                                     @Header("Authorization") String authorization);

    // API para mover un producto al carrito
    @POST("api_mover_a_carrito")
    Call<GeneralResp> moverAcarrito(@Header("Authorization") String authorization, @Body ProductoCarritoReq productoCarritoReq);

    // API para eliminar producto de la lista de deseos
    @POST("api_eliminar_lista_deseos")
    Call<GeneralResp> eliminarDeListaDeseos(@Header("Authorization") String authorization, @Body ProductoCarritoReq productoCarritoReq);
}
