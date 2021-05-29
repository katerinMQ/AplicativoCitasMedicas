package tech.abralica.clinicalaluzapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    String tipoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = getSharedPreferences("usuario",
                Context.MODE_PRIVATE);
        tipoUsuario = sharedPref.getString("tipoUsuario", "<tipoUsuario>");

        setToolbar();
    }

    private void setToolbar() {
        int navigationId;
        int menuId;

        switch (tipoUsuario) {
            case "Administrador":
                navigationId = R.navigation.admin_navigation;
                menuId = R.menu.admin_menu;
                break;
            case "Paciente":
                navigationId = R.navigation.paciente_navigation;
                menuId = R.menu.paciente_menu;
                break;
            case "Médico":
                navigationId = R.navigation.medico_navigation;
                menuId = R.menu.medico_menu;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tipoUsuario);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id
                        .nav_host_fragment_content_main);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();
        navController.setGraph(navigationId);

        navigationView.getMenu().clear();
        navigationView.inflateMenu(menuId);

        switch (tipoUsuario) {
            case "Administrador":
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home_admin, R.id.nav_reg_especialidad_f, R.id.nav_reg_persona)
                        .setOpenableLayout(drawer)
                        .build();
                break;
            case "Paciente":
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home_paciente, R.id.nav_gestionar_citas_f, R.id.nav_historial_f,
                        R.id.nav_ubicanos)
                        .setOpenableLayout(drawer)
                        .build();
                break;
            case "Médico":
                mAppBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_home_medico, R.id.nav_ver_citas)
                        .setOpenableLayout(drawer)
                        .build();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + tipoUsuario);
        }

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_ubicanos) {
            Toast.makeText(this, "Click", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}