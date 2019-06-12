package cbridge.android.adouble.com.cbridge;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cbridge.android.adouble.com.cbridge.Models.Proposal;
import cbridge.android.adouble.com.cbridge.Models.Service;


@SuppressLint("ValidFragment")
public class InfoServiceFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private Service service;

    private List<Proposal> listProposal = new ArrayList<>();

    public InfoServiceFragment(Service service){
        this.service = service;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyAdapter());

        getProposal();


        return view;
    }

    private class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView txtTime, txtObservation, txtValue;
        private CardView card_view;

        public MyViewHolder(View view){
            super(view);

            txtTime = view.findViewById(R.id.txtTime);
            txtObservation = view.findViewById(R.id.txtObservation);
            txtValue = view.findViewById(R.id.txtValue);
            card_view = view.findViewById(R.id.card_view);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_list, parent, false);

            MyViewHolder viewHolder = new MyViewHolder(v);
            return  viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int positon) {
            final Proposal proposal = listProposal.get(positon);
            myViewHolder.txtTime.setText(proposal.getTime());
            myViewHolder.txtObservation.setText(proposal.getObservation());
            myViewHolder.txtValue.setText(proposal.getValue());

            myViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

        @Override
        public int getItemCount() {
            return listProposal.size();
        }
    }

    public void getProposal(){
        Query query = this.database.getReference().child("proposals").orderByChild("idService").equalTo(this.service.getId());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    listProposal.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        Proposal pro = childDataSnapshot.getValue(Proposal.class);
                        listProposal.add(pro);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }
}
