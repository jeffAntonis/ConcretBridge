package cbridge.android.adouble.com.cbridge;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cbridge.android.adouble.com.cbridge.Models.User;


public class ProfileFragment extends Fragment {
    private User userLog;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private List<String> spinnerArray = new ArrayList<>();

    TextView txtEmail;
    EditText txtName;
    EditText txtPhone;


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();

        this.userLog = new User();
        this.userLog.setId(this.user.getUid());
        this.userLog.setEmail(this.user.getEmail());

        getDataUser(view);

        this.txtEmail = (TextView) view.findViewById(R.id.txtEmail);
        this.txtName = (EditText) view.findViewById(R.id.txtName);
        this.txtPhone = (EditText) view.findViewById(R.id.txtPhone);

        DatabaseReference databaseReference = this.database.getReference("cidades");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    spinnerArray.add(childDataSnapshot.getValue().toString());
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });

        Button confirmUpdate = view.findViewById(R.id.confirmUpdate);
        confirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDataUser(view);
            }
        });

        return view;
    }

    public void getDataUser(final View v){

        this.database.getReference().child("users").child(this.auth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //this.txtEmail.setText(this.userLog.getEmail());
                if(dataSnapshot.getValue() != null){
                    showDataUser(v, dataSnapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void updateDataUser(final View v){
        this.userLog.setName(this.txtName.getText().toString());
        this.userLog.setPhone(this.txtPhone.getText().toString());

        this.database.getReference().child("users").child(this.userLog.getId()).setValue(this.userLog)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(v.getContext(), "Dados alterados com sucesso!", Toast.LENGTH_LONG).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("@@@", e.getMessage());
                }
            });
    }

    public void showDataUser(View v, User u){
        this.userLog.setName(u.getName());
        this.userLog.setPhone(u.getPhone());

        this.txtName.setText(this.userLog.getName());
        this.txtPhone.setText(this.userLog.getPhone());
    }

}

