package com.example.tienda_ropa.Interface;

import com.example.tienda_ropa.ObtenerCarrito.ProductoCarritoReq;

import com.example.tienda_ropa.model.AgregarAlCarritoReq;
import com.example.tienda_ropa.model.AgregarListaDeseosReq;
import com.example.tienda_ropa.model.AuthReq;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.Departamento;
import com.example.tienda_ropa.model.DetalleApi;
import com.example.tienda_ropa.model.DetalleCompraResp;
import com.example.tienda_ropa.model.DevolucionRequest;
import com.example.tienda_ropa.model.DireccionEliminarRequest;
import com.example.tienda_ropa.model.Distrito;

import com.example.tienda_ropa.model.EmailReq;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.ObtenerCarritoResp;
import com.example.tienda_ropa.model.ObtenerDirecciones;
import com.example.tienda_ropa.model.ObtenerListaDeseosResp;
import com.example.tienda_ropa.model.ModificarContraReq;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.ObtenerUsuario;
import com.example.tienda_ropa.model.PagoPaypalRequest;
import com.example.tienda_ropa.model.PagoPaypalResponse;
import com.example.tienda_ropa.model.ParamsCategoria;
import com.example.tienda_ropa.model.ParamsDepartamento;
import com.example.tienda_ropa.model.ParamsProvincia;
import com.example.tienda_ropa.model.ParamsUsuario;
import com.example.tienda_ropa.model.PedidosResp;
import com.example.tienda_ropa.model.PrendaDetalleResp;
import com.example.tienda_ropa.model.Provincia;
import com.example.tienda_ropa.model.RatingReq;
import com.example.tienda_ropa.model.RegistrarUsuarioGoogleReq;
import com.example.tienda_ropa.model.RegistrarUsuarioGoogleResp;
import com.example.tienda_ropa.model.RegistrarUsuarioReq;
import com.example.tienda_ropa.model.Ubigeo;
import com.example.tienda_ropa.model.VerificarCodReq;
import com.example.tienda_ropa.model.DireccionRequest;
import com.example.tienda_ropa.model.WebhookRequest;

import java.util.List;

import java.util.List;
import java.util.Map;

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
    Call<GeneralResp> registrarUsuario(@Body RegistrarUsuarioReq registrarUsuarioReq);
    @POST("api_registrar_usuario_google")
    Call<RegistrarUsuarioGoogleResp> registrarUsuarioGoogle(@Body RegistrarUsuarioGoogleReq registrarUsuarioGoogleReq);
    @POST("api_agregar_lista_deseos")
    Call<GeneralResp> agregarListaDeseos(@Header("Authorization") String authorization,
                                       @Body AgregarListaDeseosReq agregarListaDeseosReq);
    @POST("api_agregar_carrito")
    Call<GeneralResp> agregarAlCarrito(@Header("Authorization") String authorization,
                                         @Body AgregarAlCarritoReq agregarAlCarritoReq);
    @POST("api_registrar_rating")
    Call<GeneralResp> registrarRating(@Header("Authorization") String authorization,
                                      @Body RatingReq ratingReq);
    @GET("api_obtener_carrito")
    Call<ObtenerCarritoResp> obtenerCarrito(@Header("Authorization") String authorization);

    @GET("api_historial_pedidos")
    Call<PedidosResp> historialPedidos(@Header("Authorization") String authorization);

    @POST("api_incrementar_cantidad")
    Call<GeneralResp> incrementarCantProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    @POST("api_decrementar_cantidad")
    Call<GeneralResp> disminuirCantProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    @POST("api_eliminar_producto")
    Call<GeneralResp> eliminarProduc(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);
    @GET("api_obtener_lista_Deseos")
    Call<ObtenerListaDeseosResp> obtenerListaDeseos(@Header("Authorization") String authorization);

    @POST("api_mover_a_carrito")
    Call<GeneralResp> moverAcarrito(@Header("Authorization") String authorization,
                                    @Body ProductoCarritoReq productoCarritoReq);

    // API para eliminar producto de la lista de deseos
    @POST("api_eliminar_lista_deseos")
    Call<GeneralResp> eliminarDeListaDeseos(@Header("Authorization") String authorization,
                                            @Body ProductoCarritoReq productoCarritoReq);

    @GET("api_obtenerprendas_inicio")
    Call<ObtenerPrendaResp> obtenerPrendas(@Header("Authorization") String authorization);

    @GET("api_obtener_detalle_prenda/{id_prenda}")
    Call<PrendaDetalleResp> obtenerDetallesPrenda(
            @Header("Authorization") String authorization,
            @Path("id_prenda") int prendaId
    );

    @GET("api_detalle_compra/{id}")
    Call<DetalleCompraResp> obtenerDetalleCompra(
            @Header("Authorization") String authorization,
            @Path("id") long idOrden);


    @POST("api_enviar_codigo")
    Call<GeneralResp> enviarCodigo(@Body EmailReq emailReq);

    @POST("api_verificar_codigo")
    Call<GeneralResp> verificarCodigo(@Body VerificarCodReq verificarCodReq);

    @POST("api_actualizar_password")
    Call<GeneralResp> actualizar_password(@Body ModificarContraReq modificarContraReq);

    @POST("api_prendas_por_categoria")
    Call<ObtenerPrendaResp> prendasPorCategoria(
            @Header("Authorization") String authorization,
            @Body ParamsCategoria paramsCategoria);


    @POST("api_obtener_usuario_x_id")
    Call<ObtenerUsuario> obtenerUsuario(@Body ParamsUsuario paramsUsuario);

    @POST("api_obtener_direcciones_usuario")
    Call<ObtenerDirecciones> obtenerDireccion(@Header("Authorization") String authorization,
                                              @Body ParamsUsuario paramsUsuario);

    @GET("api_obtener_departamento")
    Call<List<Departamento>> obtenerDepartamentos(@Header("Authorization") String authorization);

    @POST("api_obtener_provincia")
    Call<List<Provincia>> obtenerProvincias(@Header("Authorization") String authorization,
                                            @Body ParamsDepartamento paramsDepartamento);

    @POST("api_obtener_distrito")
    Call<List<Distrito>> obtenerDistritos(@Header("Authorization") String authorization,
                                          @Body ParamsProvincia paramsProvincia);


    @POST("api_agregar_direccion")
    Call<GeneralResp> agregarDireccion(@Header("Authorization") String authorization,
                                       @Body DireccionRequest direccionRequest);

    @POST("api_iniciar_pago_pay")
    Call<PagoPaypalResponse> iniciarPagoPaypal(@Header("Authorization") String authorization,
                                               @Body PagoPaypalRequest pagoPaypalRequest);

    @POST("webhook_pago_pay")
    Call<Void> llamarWebhookPaypal(@Header("Authorization") String authorization,
                                   @Body WebhookRequest webhookRequest);


   //PARA ELIMINAR EL DOMICILIO
    @POST("api_eliminar_direccion_usuario")
    Call<GeneralResp> eliminarDireccion(
            @Header("Authorization") String authorization,
            @Body DireccionEliminarRequest direccionEliminarRequest
    );

    @POST("devolver_compra")
    Call<Void> devolverCompra(@Header("Authorization") String token,
                              @Body DevolucionRequest request);

}
