package cbridge.android.adouble.com.cbridge;


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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import cbridge.android.adouble.com.cbridge.Models.Service;


public class SearchFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private Service serv;
    private List<Service> listService = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        this.auth = FirebaseAuth.getInstance();
        this.user = this.auth.getCurrentUser();
        this.database = FirebaseDatabase.getInstance();

        RecyclerView recyclerView = view.findViewById(R.id.recycleView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(new MyAdapter());

        getService();

        return view;
    }

    private class MyViewHolder extends  RecyclerView.ViewHolder{
        private TextView txtTile, txtDescription, txtValue;
        private CardView card_view;

        public MyViewHolder(View view){
            super(view);

            txtTile = view.findViewById(R.id.txtTitle);
            txtDescription = view.findViewById(R.id.txtDescription);
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
            final Service service = listService.get(positon);
            myViewHolder.txtTile.setText(service.getTitle());
            myViewHolder.txtDescription.setText(service.getDescription());
            myViewHolder.txtValue.setText(service.getValue());

            myViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetailsService(service);
                }
            });
        }

        @Override
        public int getItemCount() {
            return listService.size();
        }
    }

    public void getService(){
        DatabaseReference databaseReference = this.database.getReference("services");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    listService.clear();
                    for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        serv = childDataSnapshot.getValue(Service.class);
                        serv.setId(childDataSnapshot.getKey());
                        listService.add(serv);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Error", databaseError.getMessage());
            }
        });
    }

    public void openDetailsService(Service sv){
        DetailsService detailsService = new DetailsService(sv);
        this.getFragmentManager().beginTransaction()
                .replace(R.id.container, detailsService, null)
                .addToBackStack(null)
                .commit();
    }
}
