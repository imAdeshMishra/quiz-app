package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.quizapp.databinding.FragmentProfileBinding;
import com.example.quizapp.databinding.FragmentWalletBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    FragmentProfileBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false);

        database = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user  = documentSnapshot.toObject(User.class);
                        binding.emailBox.setText(user.getEmail());
                        binding.nameBox.setText(user.getName());


                        Glide.with(getContext())
                                .load(user.getProfile())
                                .into(binding.profileImage);
                    }
                });

        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        return binding.getRoot();
    }

    private void logout() {
        // Clear the user session or authentication state
        auth.signOut(); // or any method that clears the authentication state

        Toast.makeText(getActivity(), "Logged out Succesfully", Toast.LENGTH_SHORT).show();

        // Redirect the user to the login screen
        Intent intent = new Intent(getActivity(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }
}