package com.example.tienda_ropa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.PagoPaypalRequest;
import com.example.tienda_ropa.model.PagoPaypalResponse;
import com.example.tienda_ropa.model.WebhookRequest;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PagoActivity extends AppCompatActivity {

    Button btnVolver;

    MaterialButton btnPagar;
    ProgressBar progressBar;

    SharedPreferences sharedPref;
    int idUsuario;
    int idDomicilio;
    int idMetodoPago = 1;

    String token;

    String idPagoActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        btnVolver = findViewById(R.id.btnVolver);
        btnPagar = findViewById(R.id.btnPagar);
        progressBar = findViewById(R.id.progressBar);

        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        idUsuario = sharedPref.getInt("id_usuario", -1);
        token = sharedPref.getString("token", "");

        if (idUsuario == -1) {
            Toast.makeText(this, "No hay usuario activo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        idDomicilio = getIntent().getIntExtra("id_domicilio", -1);
        if (idDomicilio == -1) {
            Toast.makeText(this, "No se seleccionó una dirección válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnVolver.setOnClickListener(v -> finish());

        btnPagar.setOnClickListener(v -> {
            PagoPaypalRequest request = new PagoPaypalRequest(idUsuario, idDomicilio, idMetodoPago);
            iniciarPagoPaypal(request);
        });

        MaterialButton btnGenerarQR = findViewById(R.id.btnGenerarQR);
        btnGenerarQR.setOnClickListener(v -> {
            PagoPaypalRequest request = new PagoPaypalRequest(idUsuario, idDomicilio, idMetodoPago);
            iniciaryape(request);
        });




        // Verificar si la activity fue abierta por un deep link
        manejarDeepLink(getIntent());
    }

    private void iniciarPagoPaypal(PagoPaypalRequest pagoPaypalRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/") // Tu URL base
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<PagoPaypalResponse> call = pyAnyApi.iniciarPagoPaypal("JWT "+ token,pagoPaypalRequest);

        call.enqueue(new Callback<PagoPaypalResponse>() {
            @Override
            public void onResponse(Call<PagoPaypalResponse> call, Response<PagoPaypalResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(PagoActivity.this, "Error: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                PagoPaypalResponse pagoResp = response.body();
                if (pagoResp != null && pagoResp.getCode() == 1) {
                    String paypalLink = pagoResp.getData().getPaypal_link();
                    idPagoActual = pagoResp.getData().getId_pago();

                    llamarWebhookPaypal(idPagoActual);


                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalLink));
                    browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(browserIntent);


                    new Handler().postDelayed(() -> finishAffinity(), 5000);

                } else {
                    Toast.makeText(PagoActivity.this, "No se pudo generar el enlace de pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PagoPaypalResponse> call, Throwable throwable) {
                Toast.makeText(PagoActivity.this, "Error de conexión: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llamarWebhookPaypal(String idPago) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        WebhookRequest request = new WebhookRequest(idPago, "pendiente");

        Call<Void> call = pyAnyApi.llamarWebhookPaypal("JWT "+token,request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("Webhook", "Respuesta del servidor: " + response.code());
                    Toast.makeText(PagoActivity.this, "✅ Pago registrado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("Webhook", "Error en la respuesta: " + response.code());
                    Toast.makeText(PagoActivity.this, "❌ Error al registrar el pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("WebhookError", "Error al llamar al webhook", t);
                Toast.makeText(PagoActivity.this, "❌ Error al conectar al webhook", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void iniciaryape(PagoPaypalRequest pagoPaypalRequest) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<PagoPaypalResponse> call = pyAnyApi.iniciarPagoPaypal("JWT "+token,pagoPaypalRequest);

        call.enqueue(new Callback<PagoPaypalResponse>() {
            @Override
            public void onResponse(Call<PagoPaypalResponse> call, Response<PagoPaypalResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(PagoActivity.this, "Error: Código " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                PagoPaypalResponse pagoResp = response.body();
                if (pagoResp != null && pagoResp.getCode() == 1) {
                    PagoPaypalResponse.Data data = pagoResp.getData();

                    idPagoActual = data.getId_pago();
                    llamarWebhookPaypal(idPagoActual);

                    String igv = String.valueOf(data.getIgv());
                    String subtotal = data.getSubtotal();
                    String total = String.valueOf(data.getTotal());

                    // Abre QRActivity con los datos reales
                    Intent intent = new Intent(PagoActivity.this, QRActivity.class);
                    intent.putExtra("igv", igv);
                    intent.putExtra("subtotal", subtotal);
                    intent.putExtra("total", total);
                    startActivity(intent);

                } else {
                    Toast.makeText(PagoActivity.this, "No se pudo generar el enlace de pago", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PagoPaypalResponse> call, Throwable t) {
                Toast.makeText(PagoActivity.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }






    private void manejarError() {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Error al procesar pago", Toast.LENGTH_SHORT).show();
    }

    private void verificarEstadoPago(String idPago) {
        progressBar.setVisibility(View.VISIBLE);

        String url = "https://grupotres.pythonanywhere.com/verificar_pago/" + idPago;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        String estado = response.getString("estado");
                        if (estado.equals("aprobado")) {
                            Toast.makeText(this, "✅ Pago aprobado con PayPal", Toast.LENGTH_LONG).show();
                            finish(); // Puedes redirigir a otra activity aquí
                        } else {
                            Toast.makeText(this, "⚠️ Pago no confirmado aún. Intenta más tarde.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    error.printStackTrace();
                    Toast.makeText(this, "Error al verificar estado del pago", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    private void manejarDeepLink(Intent intent) {
        Uri data = intent.getData();
        if (data != null && "tiendaropa".equals(data.getScheme())) {
            String pagoId = data.getQueryParameter("pago_id");
            if (pagoId != null) {
                verificarEstadoPago(pagoId);
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        manejarDeepLink(intent);
    }
}
