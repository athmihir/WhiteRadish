package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Inventory extends AppCompatActivity {

    //*****for the inventory page****
    //This is the item adapter for the listview
    class ItemAdapter2 extends BaseAdapter {

        private List<Itemtemp1> itemdata2 = new ArrayList<>();
        void setData(List<Itemtemp1> mData){
            itemdata2.clear();
            itemdata2.addAll(mData);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return itemdata2.size();
        }

        @Override
        public String getItem(int position){
            return null;
        }

        @Override
        public long getItemId(int position){
            return 0;
        }

        @Override
        public View getView(int position, View convertView2, ViewGroup parent){
            if (convertView2 == null){
                convertView2 = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.viewitem, parent,false);
            }
            Itemtemp1 currentItem = itemdata2.get(position);

            TextView pname = (TextView) convertView2.findViewById(R.id.pname);
            pname.setText(currentItem.getPname());

            TextView qtyval = (TextView) convertView2.findViewById(R.id.qtyval);
            qtyval.setText(currentItem.getQtyval());

            TextView priceval = (TextView) convertView2.findViewById(R.id.priceval);
            priceval.setText(currentItem.getPriceval());
            ImageView edit = convertView2.findViewById(R.id.editbtn);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getPid());
                    b.putString("stuff2", currentItem.getPname());
                    Intent i=new Intent(Inventory.this, EditProduct.class);
                    i.putExtras(b);
                    startActivity(i);
                }
            });
            return convertView2;
        }

    }
    //creating listview instance
    private ListView listView2;
    //creating Itemadapter instance
    private ItemAdapter2 mAdapter2;
    private ArrayList<Itemtemp1> ItemsList2;
    //database variables
    FirebaseAuth auth;

    CollectionReference freference;
    //********inventory page ends************


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        mAdapter2 = new ItemAdapter2();

        //*************Inventory page****************
        //Listview2 is pointing to the listview we created
        listView2 = (ListView) findViewById(R.id.itemlist2);
        //List of Item objects we need to add
        ItemsList2 = new ArrayList<>();
        //querying the database to get list items
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        //Get product array for specific user
        freference = FirebaseFirestore.getInstance().collection("Farmer/"+userid + "/product");
        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String pid = snapshot.getId().toString();
                        String name = (String) snapshot.get("name");
                        String price = (String) snapshot.get("price");
                        String quantity = (String) snapshot.get("quantity");
                        ItemsList2.add(new Itemtemp1(name, price, quantity, pid));
                    }
                    mAdapter2 = new ItemAdapter2();
                    mAdapter2.setData(ItemsList2);
                    listView2.setAdapter(mAdapter2);
                }
            }
        });

        //*************Inventory page ends****************

        ImageView add = findViewById(R.id.addbutton);
        add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Inventory.this, AddProduct.class));
            }
        });

    }
}