package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import static java.lang.Integer.parseInt;

public class NewOrderDetails_Transporter extends AppCompatActivity {

    //**************for the NODTransporter page***********
    // This is the item class for the products
    public class Item7{
        String pname;
        String qtyval;
        String priceval;

        // Constructor that is used to create an instance of the Movie object
        public Item7(String pname, String qtyval, String priceval) {
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
    class ItemAdapter7 extends BaseAdapter {

        private List<Item7> itemdata2 = new ArrayList<>();
        void setData(List<Item7> mData){
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
            Item7 currentItem = itemdata2.get(position);

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
    private ItemAdapter7 mAdapter2;
    private ArrayList<Item7> ItemsList2;
    DocumentReference freference, freference3;
    CollectionReference freference2;
    FirebaseAuth auth;

    //*************************NODTransporter page ends*************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order_details__transporter);
        //************************************NODTransporter page***********************************************
        //Listview2 is pointing to the listview we created
        listView2 = (ListView) findViewById(R.id.itemlist7);
        //List of Item objects we need to add
        ItemsList2 = new ArrayList<>();
        Bundle bundle = getIntent().getExtras();
        String orderid= bundle.getString("stuff");
        String dbefore= bundle.getString("stuff2");
        String transporter_contact= bundle.getString("stuff3");

        TextView oid = findViewById(R.id.orderid);
        TextView ordertotal = findViewById(R.id.ordertotal);
        TextView compensation = findViewById(R.id.compensation);
        TextView orderdate = findViewById(R.id.orderdate);
        TextView deliverybefore = findViewById(R.id.deliverybefore);
        TextView farmerid = findViewById(R.id.farmerid);
        TextView customerid = findViewById(R.id.customerid);
        ImageView closeButton = findViewById(R.id.closebtn);
        ImageView acceptbutton = findViewById(R.id.acceptbtn);




        freference = FirebaseFirestore.getInstance().collection("Orders/").document(orderid);
        freference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                    oid.setText(orderid.substring(0,6));
                    ordertotal.setText(documentSnapshot.get("order_total").toString());
                    compensation.setText(documentSnapshot.get("compensation").toString());
                    orderdate.setText(documentSnapshot.get("order_date").toString());
                    deliverybefore.setText(dbefore);
                    farmerid.setText(documentSnapshot.get("farmer_id").toString().substring(0,6));
                    customerid.setText(documentSnapshot.get("customer_id").toString().substring(0,6));
                }
                else{
                    Toast.makeText(NewOrderDetails_Transporter.this, "Error displaying order.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        freference2  = FirebaseFirestore.getInstance().collection("Orders/"+orderid+"/product");
        freference2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){

                        String name = (String) snapshot.get("name");
                        String quantity = (String) snapshot.get("quantity");
                        String price = (String) snapshot.get("price");

                        ItemsList2.add(new Item7(name, quantity, price));
                    }
                    mAdapter2 = new ItemAdapter7();
                    mAdapter2.setData(ItemsList2);
                    listView2.setAdapter(mAdapter2);
                }
            }
        });



        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewOrderDetails_Transporter.this, NewOrders.class));
            }
        });


        //accept order
        acceptbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("delivery_status", "In Transit");
                hashMap.put("deliver_before", dbefore);
                hashMap.put("transporter_id", userid);
                hashMap.put("transporter_contact", transporter_contact);
                freference3 = FirebaseFirestore.getInstance().collection("Orders/").document(orderid);
                freference3.set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(NewOrderDetails_Transporter.this, "Order Accepted!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewOrderDetails_Transporter.this, NewOrders.class));
                        }
                    }
                });
            }
        });


        //************************************NODTransporter page ends***********************************************

    }
}