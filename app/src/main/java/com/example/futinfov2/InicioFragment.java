package com.example.futinfov2;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.futinfov2.databinding.FragmentInicioBinding;
import com.example.futinfov2.databinding.ViewholderJugadorBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.List;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;

public class InicioFragment extends Fragment {

    FragmentInicioBinding binding;
    private NavController navController;
    private FirebaseFirestore db;
    private TacticasViewModel tacticasViewModel;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentInicioBinding.inflate(inflater, container, false)).getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        db = FirebaseFirestore.getInstance();
        tacticasViewModel = new ViewModelProvider(requireActivity()).get(TacticasViewModel.class);
        db.collection("jugadores").document("iUZSJOddr0oP8hL2NJJp").addSnapshotListener(((value, error) ->{
            Jugador jugador = new Jugador(value.getString("name"),value.getString("position"),value.getString("rating"),value.getString("country"),value.getString("team"),value.getString("face"),value.getString("card"));
            binding.position2.setText(jugador.posicion);
            binding.name.setText(jugador.nombre);
            binding.rating.setText(String.valueOf(jugador.media));
            Glide.with(requireContext())
                    .load(jugador.cara)
                    .into(binding.face);
            Glide.with(requireContext())
                    .load(jugador.pais)
                    .into(binding.country);
            Glide.with(requireContext())
                    .load(jugador.equipo)
                    .into(binding.team);
            Glide.with(requireContext())
                    .load(jugador.tipo)
                    .into(binding.tipo);

        }

                ));

        FirebaseJugadoresViewModel firebaseJugadoresViewModel = new ViewModelProvider(this).get(FirebaseJugadoresViewModel.class);

        firebaseJugadoresViewModel.obtener();

        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.recyclerView3.setAdapter(contenidosAdapter);

        firebaseJugadoresViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<FirebaseJugadores.Jugadores>() {
            @Override
            public void onChanged(FirebaseJugadores.Jugadores respuesta) {
                contenidosAdapter.establecerListaContenido(respuesta.documents);
                respuesta.documents.forEach(contenido -> Log.e("ABCD", contenido.fields.name.stringValue + ", " + contenido.fields.position.stringValue + ", " + contenido.fields.rating.stringValue));
            }
        });




        Glide.with(requireContext())
                .load("https://firebasestorage.googleapis.com/v0/b/futinfo-1b129.appspot.com/o/iconos_app%2FxpIcon.png?alt=media&token=cc6a994b-65e8-45dd-bfb3-7fea549374d3")
                .into(binding.imageView3);



        binding.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacticasViewModel.setIdSBC("Hitos");
                navController.navigate(R.id.action_inicioFragment_to_objetivosHitosFragment);
            }
        });
        binding.imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacticasViewModel.setIdSBC("Objetivos diarios");
                navController.navigate(R.id.action_inicioFragment_to_objetivosHitosFragment);
            }
        });
        PushDownAnim.setPushDownAnimTo( binding.button)
                .setScale( MODE_SCALE, 0.89f  )
                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacticasViewModel.setIdSBC((String) binding.button.getText());
                navController.navigate(R.id.action_inicioFragment_to_tacticasOfensivasFragment);
            }
        });

        PushDownAnim.setPushDownAnimTo( binding.button2)
                .setScale( MODE_SCALE, 0.89f  )
                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tacticasViewModel.setIdSBC((String) binding.button2.getText());
                navController.navigate(R.id.action_inicioFragment_to_tacticasOfensivasFragment);
            }
        });
    }
    static class ContenidoViewHolder extends RecyclerView.ViewHolder {
        ViewholderJugadorBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderJugadorBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


    class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder>{
        List<FirebaseJugadores.Jugador> jugadoresList;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderJugadorBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            FirebaseJugadores.Jugador jugador = jugadoresList.get(position);

            binding.searchPlayer.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void afterTextChanged(Editable s) { }

                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().equals(jugador.fields.name.stringValue)){
                        binding.recyclerView3.setVisibility(View.VISIBLE);
                        holder.binding.nombre.setText(jugador.fields.name.stringValue);
                        holder.binding.media.setText(jugador.fields.rating.stringValue);
                        holder.binding.posicion.setText(jugador.fields.position.stringValue);
                        Glide.with(requireActivity()).load(jugador.fields.card.stringValue).into(holder.binding.fondo);
                        Glide.with(requireActivity()).load(jugador.fields.team.stringValue).into(holder.binding.equipo);
                        Glide.with(requireActivity()).load(jugador.fields.country.stringValue).into(holder.binding.pais);
                        Glide.with(requireActivity()).load(jugador.fields.face.stringValue).into(holder.binding.cara);
                        holder.binding.constLJug.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                tacticasViewModel.setJugadorMutableLiveData(jugador);
                                navController.navigate(R.id.action_inicioFragment_to_infoJugadorFragment);
                            }
                        });
                    }else{
                        binding.recyclerView3.setVisibility(View.INVISIBLE);
                    }
                }


            });


        }

        @Override
        public int getItemCount() {
            return jugadoresList == null ? 0 : jugadoresList.size();
        }

        void establecerListaContenido(List<FirebaseJugadores.Jugador> jugadoresList){
            this.jugadoresList = jugadoresList;
            notifyDataSetChanged();
        }
    }
}