package com.example.isitvacant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Lunch_fragment extends Fragment {


    private View groupsFreagmentView;



    private RecyclerView lunchMenuRecycler;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private  String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private CollectionReference menuRef = db.collection("/restaurants/"+uid+"/menu");
    private MenuAdapter adapter;
    Query query;








    public Lunch_fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        groupsFreagmentView = inflater.inflate(R.layout.activity_lunch_fragment, container, false);
        lunchMenuRecycler = (RecyclerView) groupsFreagmentView.findViewById(R.id.lunch_recycler);
        lunchMenuRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        query = menuRef.whereEqualTo("type","Lunch");
        setUpRecyclerView(query);








        return groupsFreagmentView;
    }



    private void setUpRecyclerView(Query query) {







        FirestoreRecyclerOptions<ModelMenu> options = new FirestoreRecyclerOptions.Builder<ModelMenu>()
                .setQuery(query, ModelMenu.class)
                .build();

        adapter = new MenuAdapter(options,getActivity().getIntent().getStringExtra("invoiceID"),getActivity().getIntent().getStringExtra("restoUid"));

        RecyclerView recyclerView = groupsFreagmentView.findViewById(R.id.lunch_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onStart() {
        super.onStart();



        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            adapter.startListening();

        }

    }




}
