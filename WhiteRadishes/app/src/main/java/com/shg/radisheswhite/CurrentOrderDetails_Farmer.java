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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderDetails_Farmer extends AppCompatActivity {

    //**************for the CODFarmer page***********
    // This is the item class for the products
    public class Item5{
        String pname;
        String qtyval;
        String priceval;

        // Constructor that is used to create an instance of the Movie object
        public Item5(String pname, String qtyval, String priceval) {
            this.pname = pname;
            this.qtyval = qtyval;
            this.priceval = priceval;

        }
        public void setpname(String pname){
            this.pname = pname;
        }
        public void setqtyval(String qtyval){
            this.qtyval = qtyval;
        }
        public void setpriceval(String priceval){
            this.priceval = priceval;
        }
        public String getpname(){
            return this.pname;
        }
        public String getqtyval(){
            return this.qtyval;
        }
        public String getpriceval(){
            return this.priceval;
        }

    }
    //This is the item adapter for the listview
    class ItemAdapter5 extends BaseAdapter {

        private List<Item5> itemdata2 = new ArrayList<>();
        void setData(List<Item5> mData){
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
                        inflate(R.layout.orderitem, parent,false);
            }
            Item5 currentItem = itemdata2.get(position);

            TextView pname = (TextView) convertView2.findViewById(R.id.pname);
            pname.setText(currentItem.getpname());

            TextView qtyval = (TextView) convertView2.findViewById(R.id.qtyval);
            qtyval.setText(currentItem.getqtyval());

            TextView priceval = (TextView) convertView2.findViewById(R.id.priceval);
            priceval.setText(currentItem.getpriceval());
            return convertView2;
        }

    }
    //creating listview instance
    private ListView listView2;
    //creating Itemadapter instance
    private ItemAdapter5 mAdapter2;
    private ArrayList<Item5> ItemsList2;
    DocumentReference freference, freference3;
    CollectionReference freference2;
    FirebaseAuth auth;
    //*************************CODFArmer page ends*************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_order_details__farmer);



        //************************************CODFarmer page***********************************************
        //Listview2 is pointing to the listview we created
        listView2 = findViewById(R.id.itemlist5);
        //List of Item objects we need to add
        ItemsList2 = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String orderid= bundle.getString("stuff");

        TextView oid = findViewById(R.id.orderid);
        TextView ordertotal = findViewById(R.id.ordertotal);
        TextView location = findViewById(R.id.location);
        TextView orderdate = findViewById(R.id.orderdate);
        TextView deliverystatus = findViewById(R.id.deliverystatus);
        TextView transporterid = findViewById(R.id.transporterid);
        TextView customerid = findViewById(R.id.customerid);
        ImageView closeButton = findViewById(R.id.closebtn);
        ImageView cancelbutton = findViewById(R.id.cancelbtn);
        //Show top data
        freference = FirebaseFirestore.getInstance().collection("Orders/").document(orderid);
        freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                    oid.setText(orderid.substring(0,6));
                    ordertotal.setText(documentSnapshot.get("order_total").toString());
                    location.setText(documentSnapshot.get("deliver_to").toString());
                    orderdate.setText(documentSnapshot.get("order_date").toString());
                    deliverystatus.setText(documentSnapshot.get("delivery_status").toString());
                    String tid = documentSnapshot.get("transporter_id").toString();
                    transporterid.setText(tid.substring(0,Math.min(6, tid.length())));
                    customerid.setText(documentSnapshot.get("customer_id").toString().substring(0,6));
                }
                else{
                    Toast.makeText(CurrentOrderDetails_Farmer.this, "Error displaying order.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //Show product detail list
        freference2  = FirebaseFirestore.getInstance().collection("Orders/"+orderid+"/product");
        freference2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){

                        String name = (String) snapshot.get("name");
                        String quantity = (String) snapshot.get("quantity");
                        String price = (String) snapshot.get("price");
                        ItemsList2.add(new Item5(name, quantity, price));
                    }
                    mAdapter2 = new ItemAdapter5();
                    mAdapter2.setData(ItemsList2);
                    listView2.setAdapter(mAdapter2);
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CurrentOrderDetails_Farmer.this, FarmerOrder.class));
            }
        });

        //cancel order
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                freference3 = FirebaseFirestore.getInstance().collection("Orders/").document(orderid);
                freference3.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CurrentOrderDetails_Farmer.this, "Order cancelled successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(CurrentOrderDetails_Farmer.this, FarmerOrder.class));
                        }
                    }
                });
            }
        });

        //************************************CODFarmer page ends***********************************************


    }
}