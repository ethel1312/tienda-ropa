package com.example.tienda_ropa;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tienda_ropa.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    public static String nombreUsuario = ""; // Variable pública y estática

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            nombreUsuario = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            Log.d("MainActivity", "Nombre de usuario: " + nombreUsuario); // ← LOG DE DEPURACIÓN
        }

        setContentView(binding.getRoot());

        MaterialToolbar topAppBar = binding.topAppBar;      // (1)
        topAppBar.setBackgroundColor(getResources().getColor(R.color.white));   // si no lo pusiste en XML
        topAppBar.setTitleTextColor(getResources().getColor(R.color.black));     // idem
        setSupportActionBar(topAppBar);

        // Define estados y colores
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },    // seleccionado
                new int[] { -android.R.attr.state_checked }    // no seleccionado
        };
        int[] colors = new int[] {
                Color.BLACK,   // cuando esté seleccionado
                Color.GRAY     // cuando NO esté seleccionado
        };

        ColorStateList csl = new ColorStateList(states, colors);

        // Aplica SOLO a este BottomNavigationView
        binding.navView.setItemIconTintList(csl);
        binding.navView.setItemTextColor(csl);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Pasando cada ID de menú como un conjunto
        //de ID porque cada menú debe considerarse como un destino de nivel superior.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}