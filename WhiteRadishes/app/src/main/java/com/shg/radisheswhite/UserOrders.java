package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class UserOrders extends AppCompatActivity {

    //*******user order starts*****8
    // This is the item class for the products
    public class Item4
    {
        String ordern;
        String odate;
        String ototal;
        String ds;

        // Constructor that is used to create an instance of the Movie object
        public Item4(String ordern, String odate, String ototal, String ds) {
            this.ordern= ordern;
            this.odate = odate;
            this.ototal = ototal;
            this.ds = ds;


        }
        public void setordern(String ordern){
            this.ordern = ordern;
        }
        public void setodate(String odate){
            this.odate = odate;
        }
        public void setototal(String ototal){
            this.ototal = ototal;
        }
        public void setds(String ds){
            this.ds = ds;
        }

        public String getordern(){
            return this.ordern;
        }
        public String getodate(){
            return this.odate;
        }
        public String getototal(){
            return this.ototal;
        }
        public String getds(){
            return this.ds;
        }

    }
    //This is the item adapter for the listview
    class ItemAdapter4 extends BaseAdapter
    {

        private List<Item4> itemdata = new ArrayList<>();
        void setData(List<Item4> mData){
            itemdata.clear();
            itemdata.addAll(mData);
            notifyDataSetChanged();
        }

        @Override
        public int getCount(){
            return itemdata.size();
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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.user_order_item, parent,false);
            }

            Item4 currentItem = itemdata.get(position);

            TextView odval = (TextView) convertView.findViewById(R.id.odval);
            odval.setText(currentItem.getodate());

            TextView oidval = (TextView) convertView.findViewById(R.id.oidval);
            oidval.setText(currentItem.getordern().substring(0,6));

            TextView ototalval = (TextView) convertView.findViewById(R.id.ototalval);
            ototalval.setText(currentItem.getototal());

            TextView dsval = (TextView) convertView.findViewById(R.id.dsval);
            dsval.setText(currentItem.getds());

            TextView smbtn = (TextView) convertView.findViewById(R.id.smbtn);
            smbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentItem.getds().equals("delivered")){
                        Bundle b = new Bundle();
                        b.putString("stuff", currentItem.getordern());
                        Intent i=new Intent(UserOrders.this, HistoryOrderDetails_Customer.class);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Bundle b = new Bundle();
                        b.putString("stuff", currentItem.getordern());
                        Intent i=new Intent(UserOrders.this, CurrentOrderDetails_Customer.class);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }
                }
            });

            return convertView;
        }

    }
    //creating listview instance
    private ListView listView;
    //creating Itemadapter instance
    private ItemAdapter4 mAdapter;
    private ArrayList<Item4> ItemsList4;
    CollectionReference freference;
    FirebaseAuth auth;

    //****user order page ends*****

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_orders);

        //**********user order page*********
        //Listview is pointing to the listview we created
        listView = (ListView) findViewById(R.id.itemlist4);
        //List of Item objects we need to add
        ItemsList4 = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        freference = FirebaseFirestore.getInstance().collection("Orders/");
        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String ds = (String) snapshot.get("delivery_status");
                        if(userid.equals((String) snapshot.get("customer_id"))){
                            String oid = snapshot.getId().toString();
                            String odate = (String) snapshot.get("order_date");
                            String ototal = (String) snapshot.get("order_total");
                            ItemsList4.add(new Item4(oid, odate, ototal, ds));
                        }
                    }
                    mAdapter = new ItemAdapter4();
                    mAdapter.setData(ItemsList4);
                    listView.setAdapter(mAdapter);
                }
            }
        });

//        **************shooping cart page ends**********

    }
}