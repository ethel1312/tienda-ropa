package com.example.tienda_ropa;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class PagoActivity extends AppCompatActivity {

    Button btnVolver;
    WebView webViewPago;
    ProgressBar progressBar;

    SharedPreferences sharedPref;
    String idUsuario = "";
    String idDomicilio = "";
    String idMetodoPago = "1";

    String idPagoActual = "";
    Handler handler = new Handler();
    Runnable verificarPagoRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pago);

        btnVolver = findViewById(R.id.btnVolver);
        webViewPago = findViewById(R.id.webViewPago);
        progressBar = findViewById(R.id.progressBar);

        // Obtener id_usuario desde SharedPreferences
        sharedPref = getSharedPreferences("user_session", Context.MODE_PRIVATE);
        int idUsuarioInt = sharedPref.getInt("id_usuario", -1);

        if (idUsuarioInt == -1) {
            Toast.makeText(this, "No hay usuario activo", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        idUsuario = String.valueOf(idUsuarioInt);

        // Obtener id_domicilio desde Intent
        idDomicilio = String.valueOf(getIntent().getIntExtra("id_domicilio", -1));
        if (idDomicilio.equals("-1")) {
            Toast.makeText(this, "No se seleccionó una dirección válida", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        webViewPago.getSettings().setJavaScriptEnabled(true);
        webViewPago.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("pago_exitoso")) {
                    handler.removeCallbacks(verificarPagoRunnable);
                    verificarEstadoPago(idPagoActual, true);
                    return true;
                }
                return false;
            }
        });

        btnVolver.setOnClickListener(v -> cerrarWebView());

        iniciarPagoPaypal(); // Inicia automáticamente
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
                        String idPago = data.getString("id_pago");

                        idPagoActual = idPago;

                        webViewPago.setVisibility(View.VISIBLE);
                        btnVolver.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.VISIBLE);

                        webViewPago.loadUrl(paypalLink);
                        iniciarVerificacionPago();

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

    private void manejarError() {
        progressBar.setVisibility(View.GONE);
        webViewPago.setVisibility(View.GONE);
        btnVolver.setVisibility(View.GONE);
        Toast.makeText(this, "Error al procesar pago", Toast.LENGTH_SHORT).show();
    }

    private void iniciarVerificacionPago() {
        verificarPagoRunnable = () -> {
            verificarEstadoPago(idPagoActual, false);
            handler.postDelayed(verificarPagoRunnable, 5000);
        };
        handler.postDelayed(verificarPagoRunnable, 5000);
    }

    private void verificarEstadoPago(String idPago, boolean redirigirInmediato) {
        String url = "https://grupotres.pythonanywhere.com/verificar_pago/" + idPago;
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        String estado = response.getString("estado");
                        if (estado.equals("aprobado")) {
                            handler.removeCallbacks(verificarPagoRunnable);
                            Toast.makeText(this, "✅ Pago aprobado con PayPal", Toast.LENGTH_LONG).show();
                            cerrarWebView();
                        } else if (redirigirInmediato) {
                            Toast.makeText(this, "⚠️ Aún no confirmado. Esperando...", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Error al verificar estado", Toast.LENGTH_SHORT).show();
                }
        );

        queue.add(request);
    }

    private void cerrarWebView() {
        webViewPago.setVisibility(View.GONE);
        btnVolver.setVisibility(View.GONE);
        handler.removeCallbacks(verificarPagoRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(verificarPagoRunnable);
    }
}

