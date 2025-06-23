package com.example.tienda_ropa;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tienda_ropa.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    public static String nombreUsuario = "";

    private FloatingActionButton btnUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            // Inicializamos binding correctamente
            binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            // Si el usuario está autenticado, obtenemos su nombre
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                nombreUsuario = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                Log.d("MainActivity", "Nombre de usuario: " + nombreUsuario);
            }

            // Configurar toolbar
            MaterialToolbar topAppBar = binding.topAppBar;
            topAppBar.setBackgroundColor(getResources().getColor(R.color.white));
            topAppBar.setTitleTextColor(getResources().getColor(R.color.black));
            setSupportActionBar(topAppBar);

            // Cambiar colores del BottomNavigationView
            int[][] states = new int[][]{
                    new int[]{android.R.attr.state_checked},
                    new int[]{-android.R.attr.state_checked}
            };

            int[] colors = new int[]{
                    Color.BLACK, // Color cuando está seleccionado
                    Color.GRAY   // Color cuando NO está seleccionado
            };

            ColorStateList csl = new ColorStateList(states, colors);
            binding.navView.setItemIconTintList(csl);
            binding.navView.setItemTextColor(csl);

            // Configuración del Navigation Controller
            BottomNavigationView navView = binding.navView;
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);

            AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            ).build();

            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(navView, navController);

            // Configurar el botón flotante para redirigir al fragmento de usuario
            btnUser = findViewById(R.id.btnUser);
            btnUser.setOnClickListener(v -> {
                navController.navigate(R.id.menu_usuario); // Este ID debe existir en el nav_graph
            });

        } catch (Exception e) {
            Log.e("MainActivity", "Error en onCreate: ", e);
        }
    }


}
