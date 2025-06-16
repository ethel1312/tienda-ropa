package com.example.tienda_ropa.Interface;

import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.EmailReq;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.example.tienda_ropa.model.ObtenerListaDeseosResp;
import com.example.tienda_ropa.model.ModificarContraReq;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.PrendaDetalleResp;
import com.example.tienda_ropa.model.RegistrarUsuarioReq;
import com.example.tienda_ropa.model.VerificarCodReq;

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

    @GET("api_obtenerprendas_inicio")
    Call<ObtenerPrendaResp> obtenerPrendas(@Header("Authorization") String authorization);

    @GET("api_obtenerdetalleprenda/{id_prenda}")
    Call<PrendaDetalleResp> obtenerDetallesPrenda(
            @Header("Authorization") String authorization,
            @Path("id_prenda") int prendaId
    );
    @POST("api_enviar_codigo")
    Call<GeneralResp> enviarCodigo(@Body EmailReq emailReq);

    @POST("api_verificar_codigo")
    Call<GeneralResp> verificarCodigo(@Body VerificarCodReq verificarCodReq);

    @POST("api_actualizar_password")
    Call<GeneralResp> actualizar_password(@Body ModificarContraReq modificarContraReq);
}
