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
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

public class PagoActivity extends AppCompatActivity {

    Button btnVolver;

    MaterialButton btnPagar;
    ProgressBar progressBar;

    SharedPreferences sharedPref;
    String idUsuario = "";
    String idDomicilio = "";
    String idMetodoPago = "1";

    String idPagoActual = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        btnVolver = findViewById(R.id.btnVolver);
        btnPagar = findViewById(R.id.btnPagar);
        progressBar = findViewById(R.id.progressBar);

        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int idUsuarioInt = sharedPref.getInt("id_usuario", -1);

        if (idUsuarioInt == -1) {
            Toast.makeText(this, "No hay usuario activo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        idUsuario = String.valueOf(idUsuarioInt);

        idDomicilio = String.valueOf(getIntent().getIntExtra("id_domicilio", -1));
        if (idDomicilio.equals("-1")) {
            Toast.makeText(this, "No se seleccionó una dirección válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnVolver.setOnClickListener(v -> finish());

        btnPagar.setOnClickListener(v -> iniciarPagoPaypal());

        // Verificar si la activity fue abierta por un deep link
        manejarDeepLink(getIntent());
    }

    private void iniciarPagoPaypal() {
        String url = "https://grupotres.pythonanywhere.com/api_iniciar_pago_pay";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject datos = new JSONObject();
        try {
            datos.put("id_usuario", idUsuario);
            datos.put("id_domicilio", idDomicilio);
            datos.put("id_metodo_pago", idMetodoPago);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                datos,
                response -> {
                    try {
                        JSONObject data = response.getJSONObject("data");
                        String paypalLink = data.getString("paypal_link");
                        idPagoActual = data.getString("id_pago");
                        llamarWebhookPaypal(idPagoActual);
                        // Abrir navegador externo
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paypalLink));
                        browserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(browserIntent);

                        new Handler().postDelayed(() -> {
                            finishAffinity(); // Cierra toda la pila de la aplicación
                        }, 5000); // 2000 milisegundos = 2 segundos

                    } catch (Exception e) {
                        e.printStackTrace();
                        manejarError();
                    }
                },
                error -> {
                    error.printStackTrace();
                    manejarError();
                }
        );

        queue.add(request);
    }

    private void llamarWebhookPaypal(String idPago) {
        String url = "https://grupotres.pythonanywhere.com/webhook_pago_pay";
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject datos = new JSONObject();
        try {
            datos.put("custom", idPago);
            datos.put("payment_status", "pendiente");
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                response -> {
                    Log.d("Webhook", "Respuesta del servidor: " + response);
                    Toast.makeText(this, "✅ Webhook procesado correctamente", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    Log.e("WebhookError", "Error al llamar al webhook", error);
                    Toast.makeText(this, "❌ Error al llamar al webhook", Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public byte[] getBody() {
                return datos.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        queue.add(request);
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
