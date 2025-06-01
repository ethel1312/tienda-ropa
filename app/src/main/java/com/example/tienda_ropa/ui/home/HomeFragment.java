package com.example.tienda_ropa.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tienda_ropa.MainActivity;
import com.example.tienda_ropa.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Obtener el nombre directamente desde Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String nombre = (user != null && user.getDisplayName() != null)
                ? user.getDisplayName()
                : "Usuario";

        // Mostrar en el TextView
        binding.textHome.setText("Bienvenido, " + nombre);

        return root;
    }

    // para el nombre en el inicio

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("user_session", Context.MODE_PRIVATE);
        String username = prefs.getString("username", "Usuario");
        String saludo = "Hola, " + username;
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null && act.getSupportActionBar() != null) {
            act.getSupportActionBar().setTitle(saludo);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}