package com.example.futinfov2;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.futinfov2.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thekhaeng.pushdownanim.PushDownAnim;

import static com.thekhaeng.pushdownanim.PushDownAnim.MODE_SCALE;


public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private NavController navController;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegisterBinding.inflate(inflater, container, false)).getRoot();
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();



        binding.imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_logInFragment);
            }
        });

        PushDownAnim.setPushDownAnimTo(binding.register)
                .setScale( MODE_SCALE, 0.89f  )
                .setDurationPush( PushDownAnim.DEFAULT_PUSH_DURATION )
                .setDurationRelease( PushDownAnim.DEFAULT_RELEASE_DURATION )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.registerEmail.getText().toString();
                String password = binding.registerPassword.getText().toString();
                String passwordAgain = binding.registerEnterPasswordAgain.getText().toString();



                if(!(password.equals(passwordAgain))){
                   Toast.makeText(getContext(),"Las contrase??as deben coincidir",Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.createUserWithEmailAndPassword(email,password);
                     //String document = mAuth.getCurrentUser().getUid();
                    //mAuth.c
                      //  firestore.collection("users").document(document).set(email);

                    navController.navigate(R.id.action_registerFragment_to_logInFragment);
                }

            }
        });

    }
}