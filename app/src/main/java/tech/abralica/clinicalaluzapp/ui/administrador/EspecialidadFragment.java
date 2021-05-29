package tech.abralica.clinicalaluzapp.ui.administrador;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Especialidad;

import static android.app.Activity.RESULT_OK;

public class EspecialidadFragment extends Fragment {

    Uri uriImagen;
    String urlDownloadImage;

    ImageView ivImagen;
    Button bElegir, bRegistrar;
    TextInputLayout tilNombre, tilDesc;
    ProgressBar progressBar;

    StorageReference storageRef;
    CollectionReference espRef;

    ActivityResultLauncher<Intent> activityResultLauncher;

    public EspecialidadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        espRef = FirebaseFirestore.getInstance().collection("especialidades");
        storageRef = FirebaseStorage.getInstance().getReference().child("imagenes");
        progressBar = new ProgressBar(getContext());

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {
                uriImagen = Objects.requireNonNull(result.getData()).getData();
                ivImagen.setImageURI(uriImagen);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_especialidad, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivImagen = requireView().findViewById(R.id.fe_iv_imagen);
        bElegir = requireView().findViewById(R.id.fe_mb_elegir);
        bRegistrar = requireView().findViewById(R.id.fe_mb_registrar);
        tilNombre = requireView().findViewById(R.id.fe_til_nombre);
        tilDesc = requireView().findViewById(R.id.fe_til_desc);
        progressBar = requireView().findViewById(R.id.fe_pb_barra);

        bElegir.setOnClickListener(v -> abrirGaleria());

        bRegistrar.setOnClickListener(v -> uploadFile());

    }

    private void abrirGaleria() {
        if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

        Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        activityResultLauncher.launch(galeria);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = requireActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFile() {
        if (uriImagen != null) {
            StorageReference fileRef = storageRef.child(getRandomHexString() + "." + getFileExtension(uriImagen));

            fileRef.putFile(uriImagen).addOnSuccessListener(taskSnapshot -> {
                Handler handler = new Handler();
                handler.postDelayed(() -> progressBar.setProgress(0), 500);
                Toast.makeText(getContext(), "Se ha subido la imagen", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlDownloadImage = uri.toString();

                    registrarEspecialidad();

                }).addOnFailureListener(e -> Toast.makeText(getContext(), "No se pudo obtener el enlace", Toast.LENGTH_SHORT).show());

            }).addOnFailureListener(e -> Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show()).addOnProgressListener(snapshot -> {
                double progress = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                progressBar.setProgress((int) progress);
            });
        } else {
            Toast.makeText(getContext(), "No se seleccionó una imagen", Toast.LENGTH_SHORT).show();
        }
    }

    private void registrarEspecialidad() {
        Especialidad especialidad = new Especialidad();
        especialidad.setNombre(Objects.requireNonNull(tilNombre.getEditText()).getText().toString());
        especialidad.setDescripcion(Objects.requireNonNull(tilDesc.getEditText()).getText().toString());
        especialidad.setUrlImagen(urlDownloadImage);

        espRef.add(especialidad).addOnSuccessListener(e -> Toast.makeText(getContext(), "Se registró la " + "especialidad", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(getContext(), "No se pudo registrar la especialidad: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getRandomHexString() {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 8) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.substring(0, 8);
    }
}