package tech.abralica.clinicalaluzapp.ui.paciente;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tech.abralica.clinicalaluzapp.R;
import tech.abralica.clinicalaluzapp.models.Especialidad;

public class ReservarEspFragment extends Fragment {

    private List<Especialidad> especialidades;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reservar_esp, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

/*        RVAdapter rvAdapter = new RVAdapter(especialidades, view.getContext(), (v, position) -> {

            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.reservar_datetime_fragment);
        });*/

        especialidades = new ArrayList<>();

        Query query = FirebaseFirestore.getInstance().collection("especialidades");
        query.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            especialidades = Objects.requireNonNull(value).toObjects(Especialidad.class);
        });

        FirestoreRecyclerOptions<Especialidad> options = new FirestoreRecyclerOptions.Builder<Especialidad>().setQuery(query, Especialidad.class).build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Especialidad, EspecialidadViewHolder>(options) {

            @NonNull
            @NotNull
            @Override
            public EspecialidadViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.especialidad_card, parent, false);

                return new EspecialidadViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull @NotNull EspecialidadViewHolder holder, int position, @NonNull @NotNull Especialidad model) {
                if (especialidades != null && position < especialidades.size()) {
                    Especialidad especialidad = especialidades.get(position);
                    holder.especialidadNombre.setText(especialidad.getNombre());
                    holder.especialidadDesc.setText(especialidad.getDescripcion());
                    Picasso.get().load(especialidad.getUrlImagen()).into(holder.especialidadImagen);
                }
            }
        };

        recyclerView.setAdapter(adapter);

        return view;
    }

    static class EspecialidadViewHolder extends RecyclerView.ViewHolder {
        public ImageView especialidadImagen;
        public TextView especialidadNombre;
        public TextView especialidadDesc;

        private EspecialidadViewHolder.ClickListener mClickListener;

        public EspecialidadViewHolder(@NonNull View itemView) {
            super(itemView);
            especialidadImagen = itemView.findViewById(R.id.especialidad_imagen);
            especialidadNombre = itemView.findViewById(R.id.especialidad_nombre);
            especialidadDesc = itemView.findViewById(R.id.especialidad_desc);

            itemView.setOnClickListener(v -> mClickListener.onItemClick(v, getAdapterPosition()));
            itemView.setOnLongClickListener(v -> {
                mClickListener.onItemLongClick(v, getAdapterPosition());
                return true;
            });
        }

        public interface ClickListener{
            void onItemClick(View view, int position);
            void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(EspecialidadViewHolder.ClickListener clickListener){
            mClickListener = clickListener;
        }
    }
}