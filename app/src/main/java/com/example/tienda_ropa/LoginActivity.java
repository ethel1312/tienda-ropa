package com.example.tienda_ropa;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tienda_ropa.Interface.PyAnyApi;
import com.example.tienda_ropa.model.AuthResp;
import com.example.tienda_ropa.model.GeneralResp;
import com.example.tienda_ropa.model.RegistrarUsuarioGoogleReq;
import com.example.tienda_ropa.model.RegistrarUsuarioGoogleResp;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    Button mBtnCorreo;
    Button mBtnCrearCuenta;

    FirebaseAuth auth;
    GoogleSignInClient googleSignInClient;
    ShapeableImageView imageView;
    TextView name, mail;
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == RESULT_OK) {
                Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                try {
                    GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                    AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                    auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                auth = FirebaseAuth.getInstance();
//                                Glide.with(LoginActivity.this).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
//                                name.setText(auth.getCurrentUser().getDisplayName());
//                                mail.setText(auth.getCurrentUser().getEmail());

                                GoogleSignInAccount cuentaGoogle = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                                if (cuentaGoogle != null) {
                                    String nombre = cuentaGoogle.getDisplayName();
                                    String email = cuentaGoogle.getEmail();

                                    RegistrarUsuarioGoogleReq req = new RegistrarUsuarioGoogleReq();
                                    req.setNombre(nombre);
                                    req.setEmail(email);

                                    registrarUsuarioGoogle(req);
                                }

                                Toast.makeText(LoginActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();

                                // Aquí lanzas MainActivity y cierras LoginActivity
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (ApiException e) {
                    Log.e("MainActivity", "Error al iniciar sesión: ", e);
                }
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        mBtnCorreo = findViewById(R.id.btnCorreo);
        mBtnCrearCuenta = findViewById(R.id.btnCrearCuenta);

        FirebaseApp.initializeApp(this);

//        imageView = findViewById(R.id.profileImage);
//        name = findViewById(R.id.nameTV);
//        mail = findViewById(R.id.mailTV);

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);

        auth = FirebaseAuth.getInstance();

        MaterialButton signInButton = findViewById(R.id.btnGoogle);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });

//        MaterialButton signOut = findViewById(R.id.signout);
//        signOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        if (firebaseAuth.getCurrentUser() == null) {
//                            googleSignInClient.signOut().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Toast.makeText(MainActivity.this, "Signed out successfully", Toast.LENGTH_SHORT).show();
//                                    startActivity(new Intent(MainActivity.this, MainActivity.class));
//                                }
//                            });
//                        }
//                    }
//                });
//                FirebaseAuth.getInstance().signOut();
//            }
//        });

//        if (auth.getCurrentUser() != null) {
//            Glide.with(LoginActivity.this).load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl()).into(imageView);
//            name.setText(auth.getCurrentUser().getDisplayName());
//            mail.setText(auth.getCurrentUser().getEmail());
//        }

        mBtnCorreo.setOnClickListener(v -> {
            Intent intent = new Intent(this, IniciarActivity.class);
            startActivity((intent));
        });

        mBtnCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrarActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void registrarUsuarioGoogle(RegistrarUsuarioGoogleReq registrarUsuarioGoogleReq) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://grupotres.pythonanywhere.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PyAnyApi pyAnyApi = retrofit.create(PyAnyApi.class);

        Call<RegistrarUsuarioGoogleResp> call = pyAnyApi.registrarUsuarioGoogle(registrarUsuarioGoogleReq);

        call.enqueue(new Callback<RegistrarUsuarioGoogleResp>() {
            @Override
            public void onResponse(Call<RegistrarUsuarioGoogleResp> call, Response<RegistrarUsuarioGoogleResp> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RegistrarUsuarioGoogleResp resp = response.body();

                    // Guardar datos en SharedPreferences
                    getSharedPreferences("user_session", MODE_PRIVATE).edit()
                            .putString("token", resp.getToken())
                            .putString("username", resp.getUsername())
                            .putInt("id_usuario", resp.getId_usuario())
                            .apply();

                    Toast.makeText(LoginActivity.this, "¡Inicio de sesión exitoso!", Toast.LENGTH_SHORT).show();

                    // Ir a MainActivity
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Error: No se pudo registrar con Google", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegistrarUsuarioGoogleResp> call, Throwable throwable) {


            }
        });
    }

}