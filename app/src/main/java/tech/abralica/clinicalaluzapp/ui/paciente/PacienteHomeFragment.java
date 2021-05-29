package tech.abralica.clinicalaluzapp.ui.paciente;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import tech.abralica.clinicalaluzapp.R;

public class PacienteHomeFragment extends Fragment {

    private LinearLayout bReservar, bCitas, bHistorial, bUbicanos;
    private TextView tvPaciente;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_paciente_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        SharedPreferences sharedPref = requireContext().getSharedPreferences("usuario",
                Context.MODE_PRIVATE);

        String nombre = sharedPref.getString("nombres", "<nombres>");

        tvPaciente = requireView().findViewById(R.id.home_tv_paciente);
        tvPaciente.setText(String.format("Bienvenido %s!", nombre));

        bReservar = requireView().findViewById(R.id.fph_ll_reservar);
        bReservar.setOnClickListener(view1 ->
                navController.navigate(R.id.nav_reservar_esp));

    }
}