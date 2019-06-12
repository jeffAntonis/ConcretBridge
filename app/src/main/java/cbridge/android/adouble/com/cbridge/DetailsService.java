package cbridge.android.adouble.com.cbridge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.Reference;

import cbridge.android.adouble.com.cbridge.Models.Proposal;
import cbridge.android.adouble.com.cbridge.Models.Service;


@SuppressLint("ValidFragment")
public class DetailsService extends Fragment {
    private Service service;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private Proposal proposal;
    private TextView txtTitle, txtDescription;
    private EditText txtObservation, txtValue, txtTime;
    private Button btnSend;
    private String key;
    private Boolean control;

    public DetailsService(Service service) {
        this.service = service;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();

        this.control = false;
        verifyProposal();

        this.txtTitle = view.findViewById(R.id.txtTitle);
        this.txtTitle.setText(this.service.getTitle());
        this.txtDescription = view.findViewById(R.id.txtDescription);
        this.txtDescription.setText(this.service.getDescription());

        this.txtObservation = view.findViewById(R.id.txtObservation);
        this.txtValue = view.findViewById(R.id.txtValue);
        this.txtTime = view.findViewById(R.id.txtTime);

        this.btnSend = view.findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendProposal(view);
            }
        });

        return view;
    }

    public  void verifyProposal(){
        Query query = this.database.getReference().child("proposals").orderByChild("idUser").equalTo(this.user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Proposal p = childDataSnapshot.getValue(Proposal.class);
                        Log.d("IAE", p.getObservation());
                        if(p.getIdService().contentEquals(service.getId())){
                            key = childDataSnapshot.getKey();
                            control = true;
                            proposal = p;

                            txtObservation.setText(proposal.getObservation());
                            txtValue.setText(proposal.getValue());
                            txtTime.setText(proposal.getTime());
                            btnSend.setText("Alterar Proposta");
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void sendProposal(final View v){
        String userId = this.user.getUid();
        this.proposal = new Proposal(this.service.getId(), userId, this.txtValue.getText().toString(), this.txtTime.getText().toString(), this.txtObservation.getText().toString());

        if(this.control){
            this.database.getReference().child("proposals").child(this.key).setValue(this.proposal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Proposta alterada com sucesso!", Toast.LENGTH_LONG).show();

                        updateValueService();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("@@@", e.getMessage());
                    }
                });
        } else{
            this.database.getReference().child("proposals").push().setValue(this.proposal)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(v.getContext(), "Proposta enviada com sucesso!", Toast.LENGTH_LONG).show();

                        updateValueService();
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

    public void updateValueService(){
        Query query = this.database.getReference().child("proposals").orderByChild("idService").equalTo(this.service.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    Double value = 0.0;
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Service serv = childDataSnapshot.getValue(Service.class);
                        value = Double.parseDouble(serv.getValue());
                    }

                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Log.d("BORA", childDataSnapshot.getValue().toString());

                        Service serv = childDataSnapshot.getValue(Service.class);

                        if(value > Double.parseDouble(serv.getValue())){
                            value = Double.parseDouble(serv.getValue());
                        }
                    }
                    updateValueService(value);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void updateValueService(Double value){
        this.service.setValue(value.toString());
        this.database.getReference().child("services").child(this.service.getId()).setValue(this.service)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

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
