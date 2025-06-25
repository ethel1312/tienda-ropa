package com.example.tienda_ropa.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.R;
import com.example.tienda_ropa.comentarios.ComentarioAdapter;
import com.example.tienda_ropa.comentarios.ComentarioEntry;
import com.example.tienda_ropa.model.AgregarAlCarritoReq;
import com.example.tienda_ropa.model.ColorApi;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.RatingReq;
import com.example.tienda_ropa.model.TallaApi;
import com.example.tienda_ropa.ui.home.ImageRequester;
import com.example.tienda_ropa.model.ObtenerPrendaResp;
import com.example.tienda_ropa.model.PrendaApi;
import com.example.tienda_ropa.model.PrendaDetalleResp;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetallesPrenda extends AppCompatActivity {

    private TextView nombrePrenda, precioPrenda, descripcionPrenda;
    private NetworkImageView imagenPrenda;
    private SharedPreferences sharedPreferences;
    String token;
    int idUsuario;
    private int prendaId;
    private PrendaApi prenda;
    ChipGroup chipGroupTallas;
    ChipGroup chipGroupColores;
    Button agregarCarritoButton;
    MaterialRatingBar ratingBarAvg;
    TextView  txtTotalRatings;
    Button btnEscribirResena;
    LinearLayout layoutEditor;
    MaterialRatingBar ratingBarEditor;
    TextInputEditText edtComentario;
    private MaterialButton btnEnviarResena, btnCancelarResena;
    private List<ComentarioEntry> comentarioList;
    ImageButton btnAtras;
    private static final Map<String, String> colorMap = new HashMap<>();
    static {
        colorMap.put("Rojo", "#FF0000");
        colorMap.put("Azul", "#0000FF");
        colorMap.put("Negro", "#000000");
        colorMap.put("Blanco", "#FFFFFF");
        colorMap.put("Verde", "#008000");
        colorMap.put("Amarillo", "#FFFF00");
        colorMap.put("Rosado", "#FFC0CB");
        // Agrega más colores según lo que tengas en la BD
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalles_prenda);

        sharedPreferences = this.getSharedPreferences("user_session", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        idUsuario = sharedPreferences.getInt("id_usuario", 0);

        nombrePrenda = findViewById(R.id.nombrePrenda);
        precioPrenda = findViewById(R.id.precioPrenda);
        descripcionPrenda = findViewById(R.id.descripcionPrenda);
        imagenPrenda = findViewById(R.id.imagenPrenda);
        chipGroupTallas = findViewById(R.id.chipGroupTallas);
        chipGroupColores = findViewById(R.id.chipGroupColores);
        agregarCarritoButton = findViewById(R.id.agregarCarritoButton);
        btnAtras = findViewById(R.id.btnAtras);

        //RESEÑA
        ratingBarAvg = findViewById(R.id.ratingBar);
        txtTotalRatings  = findViewById(R.id.txtTotalRatings);
        btnEscribirResena = findViewById(R.id.btnEscribirResena);
        layoutEditor      = findViewById(R.id.layoutEditorResena);
        ratingBarEditor   = findViewById(R.id.ratingBarEditor);
        edtComentario     = findViewById(R.id.edtComentario);
        btnEnviarResena   = findViewById(R.id.btnEnviarResena);
        btnCancelarResena = findViewById(R.id.btnCancelarResena);

        // Obtener el aidi
        prendaId = getIntent().getIntExtra("prenda_id", -1);

        if(prendaId==-1){
            Toast.makeText(this,"Prenda inválida",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEscribirResena.setOnClickListener(v -> {
            btnEscribirResena.setVisibility(View.GONE);
            layoutEditor.setVisibility(View.VISIBLE);
        });

        btnCancelarResena.setOnClickListener(v -> {
            layoutEditor.setVisibility(View.GONE);
            btnEscribirResena.setVisibility(View.VISIBLE);
            ratingBarEditor.setRating(0f);
            edtComentario.setText("");
        });


        agregarCarritoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validar selección de talla
                int idChipSeleccionado = chipGroupTallas.getCheckedChipId();
                if (idChipSeleccionado == View.NO_ID) {
                    Toast.makeText(DetallesPrenda.this, "Selecciona una talla", Toast.LENGTH_SHORT).show();
                    return;
                }

                Chip chipSeleccionado = findViewById(idChipSeleccionado);
                String tipoTalla = chipSeleccionado.getText().toString();

                int idTallaSeleccionada = -1;
                for (TallaApi talla : prenda.getTallas()) {
                    if (talla.getTipo_talla().equalsIgnoreCase(tipoTalla)) {
                        idTallaSeleccionada = talla.getId_talla();
                        break;
                    }
                }

                if (idTallaSeleccionada == -1) {
                    Toast.makeText(DetallesPrenda.this, "Error al obtener la talla seleccionada", Toast.LENGTH_SHORT).show();
                    return;
                }

                AgregarAlCarritoReq req = new AgregarAlCarritoReq();
                req.setId_prenda(prendaId);
                req.setId_talla(idTallaSeleccionada);
                req.setCantidad(1);

                agregarAlCarrito(req);
            }
        });

        btnEnviarResena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int estrellas = (int) ratingBarEditor.getRating();
                if (estrellas == 0) {
                    Toast.makeText(DetallesPrenda.this, "Selecciona al menos una estrella", Toast.LENGTH_SHORT).show();
                    return;
                }

                String comentario = edtComentario.getText() != null
                        ? edtComentario.getText().toString().trim()
                        : "";

                RatingReq req = new RatingReq();
                req.setId_prenda(prendaId);
                req.setEstrellas(estrellas);
                req.setComentario(comentario);
                registrarRating(req);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        obtenerDetallesPrenda(prendaId);
    }

    private void obtenerDetallesPrenda(int prendaId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")  // URL de tu API
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);
        sharedPreferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        Call<PrendaDetalleResp> call = pyAnyApi.obtenerDetallesPrenda("JWT " + token, prendaId);
        call.enqueue(new Callback<PrendaDetalleResp>() {
            @Override
            public void onResponse(Call<PrendaDetalleResp> call, Response<PrendaDetalleResp> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(DetallesPrenda.this, "Código de error: " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                prenda = response.body().getData();
                if (prenda == null) {
                    Toast.makeText(DetallesPrenda.this, "No se encontraron detalles", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Setear info básica
                nombrePrenda.setText(prenda.getNomPrenda());
                precioPrenda.setText("S/ " + prenda.getPrecio());
                descripcionPrenda.setText(prenda.getDescripcion());
                ImageRequester.getInstance(getBaseContext()).setImageFromUrl(imagenPrenda, prenda.getUrl_imagen());

                // Mostrar Tallas
                chipGroupTallas.removeAllViews();
                for (TallaApi talla : prenda.getTallas()) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_talla_layout, chipGroupTallas, false);
                    chip.setText(talla.getTipo_talla());
                    chipGroupTallas.addView(chip);
                }

                // Mostrar Colores
                chipGroupColores.removeAllViews();
                for (ColorApi color : prenda.getColores()) {
                    Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_color_layout, chipGroupColores, false);

                    String nombreColor = color.getColor(); // Ej: "Rojo"
                    String colorHex = colorMap.getOrDefault(nombreColor, "#000000");

                    try {
                        chip.setChipBackgroundColor(ColorStateList.valueOf(Color.parseColor(colorHex)));
                    } catch (IllegalArgumentException e) {
                        chip.setChipBackgroundColorResource(R.color.black);
                    }

                    chipGroupColores.addView(chip);
                }

                // Rating
                ratingBarAvg.setRating(prenda.getPromedio_rating());

                if (prenda.getTotal_ratings() == 0) {
                    txtTotalRatings.setText("Sin reseñas");
                } else {
                    Locale localeES = new Locale("es", "ES");
                    // Ej: "4.2 · 23 reseñas"
                    txtTotalRatings.setText(
                            String.format(localeES, "%.1f · %d reseñas",
                                    prenda.getPromedio_rating(),
                                    prenda.getTotal_ratings()));
                }

                if (prenda.getMi_rating() == 0) {
                    btnEscribirResena.setText("ESCRIBIR UNA RESEÑA");
                } else {
                    btnEscribirResena.setText("EDITAR MI RESEÑA");
                    ratingBarAvg.setSecondaryProgress(prenda.getMi_rating()); // opcional: sombrear las estrellas que él dio
                }

                // ─── 4. LISTA DE COMENTARIOS ───────────────────────────────
                List<ComentarioEntry> comentarios = prenda.getComentarios(); // mapeado por Gson

                MaterialCardView cardComentarios  = findViewById(R.id.cardComentarios);
                RecyclerView rvComentarios    = findViewById(R.id.recyclerComentarios);
                rvComentarios.setLayoutManager(new LinearLayoutManager(DetallesPrenda.this));
                rvComentarios.setHasFixedSize(true);

                if (comentarios == null || comentarios.isEmpty()) {
                    // no muestres la tarjeta
                    cardComentarios.setVisibility(View.GONE);
                } else {
                    cardComentarios.setVisibility(View.VISIBLE);
                    rvComentarios.setAdapter(new ComentarioAdapter(comentarios));
                }

            }

            @Override
            public void onFailure(Call<PrendaDetalleResp> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(DetallesPrenda.this, "Error de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void agregarAlCarrito(AgregarAlCarritoReq agregarAlCarritoReq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.agregarAlCarrito("JWT " + token, agregarAlCarritoReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1) {
                    Toast.makeText(DetallesPrenda.this, "Agregado al carrito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetallesPrenda.this, "No se pudo agregar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(DetallesPrenda.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registrarRating(RatingReq ratingReq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<GeneralResp> call = pyAnyApi.registrarRating("JWT " + token, ratingReq);

        call.enqueue(new Callback<GeneralResp>() {
            @Override
            public void onResponse(Call<GeneralResp> call, Response<GeneralResp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getCode() == 1) {
                    // 1️⃣ feedback al usuario
                    Toast.makeText(DetallesPrenda.this,
                            "¡Gracias por tu reseña!", Toast.LENGTH_SHORT).show();

                    // 2️⃣ colapsar editor y limpiar campos
                    layoutEditor.setVisibility(View.GONE);
                    btnEscribirResena.setVisibility(View.VISIBLE);
                    ratingBarEditor.setRating(0f);
                    edtComentario.setText("");

                    // 3️⃣ refrescar la info de ratings
                    obtenerDetallesPrenda(prendaId);
                } else {
                    Toast.makeText(DetallesPrenda.this,
                            "No se pudo guardar la reseña", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<GeneralResp> call, Throwable t) {
                Toast.makeText(DetallesPrenda.this, "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}