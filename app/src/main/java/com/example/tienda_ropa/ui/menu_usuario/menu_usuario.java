package com.example.tienda_ropa.ui.menu_usuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tienda_ropa.R;
import com.example.tienda_ropa.ui.menu_usuario.MiCuentaActivity;
import com.example.tienda_ropa.ui.menu_usuario.MiDireccionActivity;
import com.google.android.material.button.MaterialButton;

public class menu_usuario extends Fragment {

    CardView mbtnMiCuenta, mbtnMiDireccion;
    MaterialButton mBtnCerrarSesion;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_usuario, container, false);

        // Referenciar los CardView
        mbtnMiCuenta = view.findViewById(R.id.btnMiCuenta);
        mbtnMiDireccion = view.findViewById(R.id.btnMiDireccion);
        mBtnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        // Configurar el clic para Mi Cuenta
        mbtnMiCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MiCuentaActivity.class);
            startActivity(intent);
        });

        // Configurar el clic para Mi Dirección
        mbtnMiDireccion.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MiDireccionActivity.class);
            startActivity(intent);
        });

        mBtnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 2. Borrar SharedPreferences
                SharedPreferences prefs = requireActivity().getSharedPreferences("user_session", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear(); // Esto borra token, id_usuario, username, etc.
                editor.apply();

                // 3. Ir a la pantalla de login y limpiar backstack
                Intent intent = new Intent(getActivity(), com.example.tienda_ropa.LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        String saludo = "USUARIO";
        AppCompatActivity act = (AppCompatActivity) getActivity();
        if (act != null && act.getSupportActionBar() != null) {
            act.getSupportActionBar().setTitle(saludo);
        }
    }
}
