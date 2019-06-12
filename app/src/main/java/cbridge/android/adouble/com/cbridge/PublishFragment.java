package cbridge.android.adouble.com.cbridge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import cbridge.android.adouble.com.cbridge.Models.Service;
import cbridge.android.adouble.com.cbridge.Models.User;


public class PublishFragment extends Fragment {

    private Service service;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private EditText txtTile, txtDescription, txtValue;


    public PublishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_publish, container, false);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();

        this.txtTile = view.findViewById(R.id.txtTitle);
        this.txtDescription = view.findViewById(R.id.txtDescription);
        this.txtValue = view.findViewById(R.id.txtDescription);

        Button btnPublish = view.findViewById(R.id.btnPublish);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                publishService(view);
            }
        });

        return view;
    }

    public void publishService(final View v){
        String userId = this.user.getUid();
        this.service = new Service(this.txtTile.getText().toString(), this.txtDescription.getText().toString(), this.txtValue.getText().toString(), userId);
        this.database.getReference().child("services").push().setValue(this.service)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(v.getContext(), "Serviço publicado com sucesso!", Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("@@@", e.getMessage());
                }
            });
    }
}
