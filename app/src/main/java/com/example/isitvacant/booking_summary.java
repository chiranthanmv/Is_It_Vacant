package com.example.isitvacant;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class booking_summary extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    private RecyclerView menuRecyclerList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference menuRef ;
    private MenuInfoAdapter adapter;
    Query query;
    TextView username;
    String restoID,uid,timeSlot,bookDate,invoiceID,noOfPeople,tableId;
    TextView grandTotal;
    TextView mobile_no;
    ArrayList<String> tableIDList;
    FirebaseAuth mAuth ;
    FirebaseFirestore mstore;
    TextView hotel_name,reservation_date,reservation_time,table_id,numberOfGuests,location;
    Button Payment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_summary);
        collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar);
        menuRecyclerList = (RecyclerView) findViewById(R.id.menu_info_recycler);
        menuRecyclerList.setLayoutManager(new LinearLayoutManager(this));
        mAuth = FirebaseAuth.getInstance();
        uid  = mAuth.getCurrentUser().getUid();
        invoiceID = getIntent().getStringExtra("invoiceID");
        menuRef = db.collection("/users/"+uid+"/current_reservations/"+invoiceID+"/cart");
        query = menuRef.orderBy("foodName");
        setUpRecyclerView(query);
        grandTotal = findViewById(R.id.total_no_of_items);
        mstore = FirebaseFirestore.getInstance();
        mstore.collection("/users/"+uid+"/current_reservations/"+invoiceID+"/cart")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {


                        List<Double> allPrice = new ArrayList<>();
                        double sum =0;
                        for (QueryDocumentSnapshot doc : value) {
                            if (doc.get("foodName") != null) {
                                allPrice.add(doc.getDouble("totalPrice"));
                                //Toast.makeText(GroupProfileActivity.this, "Messages is: "+value,Toast.LENGTH_LONG).show();
                            }
                        }
                        //Toast.makeText(GroupChatActivity.this, "Messages are: "+allMssages,Toast.LENGTH_LONG).show();



                        for(int i=0; i<allPrice.size(); i++)

                        {


                            sum = sum+allPrice.get(i);




                        }
                        grandTotal.setText(String.valueOf(sum));
                    }
                });

        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#099ae1"));
        collapsingToolbarLayout.setExpandedTitleGravity(Gravity.BOTTOM);
        collapsingToolbarLayout.setExpandedTitleTypeface(Typeface.DEFAULT_BOLD);


        username = findViewById(R.id.Username);
        mobile_no = findViewById(R.id.Mobile_No);
        hotel_name = findViewById(R.id.hotel_name);
        reservation_date = findViewById(R.id.Reservation_date);
        reservation_time = findViewById(R.id.Reservation_time);
        numberOfGuests = findViewById(R.id.no_of_guests);
        restoID = getIntent().getStringExtra("restoID");


        timeSlot = getIntent().getStringExtra("timeSlot");
        reservation_time.setText(timeSlot);
        bookDate = getIntent().getStringExtra("bookDate");
        reservation_date.setText(bookDate);
        location = findViewById(R.id.Location);
        table_id = findViewById(R.id.Table_no);
        tableIDList = getIntent().getStringArrayListExtra("tableID");
        tableId = tableIDList.toString();
    tableId = tableId.substring(1,tableId.length()-1);
       table_id.setText(tableId);

        noOfPeople = getIntent().getStringExtra("no_of_people");
        numberOfGuests.setText(noOfPeople);


        Payment = findViewById(R.id.Payment);
        Payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(booking_summary.this, "Payment", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecyclerView(Query query) {







        FirestoreRecyclerOptions<ModelMenuInfo> options = new FirestoreRecyclerOptions.Builder<ModelMenuInfo>()
                .setQuery(query, ModelMenuInfo.class)
                .build();

        adapter = new MenuInfoAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.menu_info_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
        restoID = getIntent().getStringExtra("restoID");
        uid  = mAuth.getCurrentUser().getUid();
        DocumentReference documentReferences = mstore.collection("restaurants").document(restoID);
        documentReferences.addSnapshotListener(booking_summary.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {





                hotel_name.setText(documentSnapshot.getString("name"));
                location.setText(documentSnapshot.getString("Address"));
                collapsingToolbarLayout.setTitle(documentSnapshot.getString("name"));


            }
        });
        DocumentReference documentReferences2 = mstore.collection("users").document(uid);
        documentReferences2.addSnapshotListener(booking_summary.this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {





                mobile_no.setText(documentSnapshot.getString("Mobile"));
                username.setText(documentSnapshot.getString("name"));



            }
        });
    }
}
