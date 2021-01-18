package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FarmerOrder extends AppCompatActivity {

    public class Item5 {
        String ordern;
        String odate;
        String ototal;
        String ds;
        String loc;


        // Constructor that is used to create an instance of the Movie object
        public Item5(String ordern, String odate, String ototal, String ds, String loc) {
            this.ordern = ordern;
            this.odate = odate;
            this.ototal = ototal;
            this.ds = ds;
            this.loc = loc;


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
        public void setloc(String loc){
            this.loc = loc;
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
        public String getloc(){
            return this.loc;
        }
    }



    class ItemAdapter5 extends BaseAdapter {

        private List<Item5> itemdata = new ArrayList<>();

        void setData(List<Item5> mData) {
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
                        inflate(R.layout.farmer_order2_item, parent,false);
            }

            Item5 currentItem = itemdata.get(position);

            TextView odval = (TextView) convertView.findViewById(R.id.odval);
            odval.setText(currentItem.getodate());

            TextView oidval = (TextView) convertView.findViewById(R.id.oidval);
            oidval.setText(currentItem.getordern().substring(0,6));

            TextView ototalval = (TextView) convertView.findViewById(R.id.ototalval);
            ototalval.setText(currentItem.getototal());

            TextView dsval = (TextView) convertView.findViewById(R.id.dsval);
            dsval.setText(currentItem.getds());

            TextView olocval = (TextView) convertView.findViewById(R.id.olocval);
            olocval.setText(currentItem.getloc());

            TextView smbtn = (TextView) convertView.findViewById(R.id.smbtn);
            smbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getordern());
                    if(currentItem.getds().equals("delivered")){
                        Intent i=new Intent(FarmerOrder.this, HistoryOrderDetails_Farmer.class);
                        i.putExtras(b);
                        startActivity(i);
                        finish();
                    }
                    else{
                        Intent i=new Intent(FarmerOrder.this, CurrentOrderDetails_Farmer.class);
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
    private ItemAdapter5 mAdapter;
    private ArrayList<Item5> ItemsList5;
    CollectionReference freference;
    FirebaseAuth auth;

    //****user order page ends*****

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_order);



        listView = (ListView) findViewById(R.id.itemlist5);
        //List of Item objects we need to add
        ItemsList5 = new ArrayList<>();

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
                        if(userid.equals((String) snapshot.get("farmer_id")) ){
                            String oid = snapshot.getId().toString();
                            String odate = (String) snapshot.get("order_date");
                            String ototal = (String) snapshot.get("order_total");
                            String compensation = (String) snapshot.get("compensation");
                            String delivered_at = (String) snapshot.get("deliver_to");
                            String dbefore = (String) snapshot.get("deliver_before");
                            ItemsList5.add(new Item5(oid, odate, ototal, ds, delivered_at));
                        }
                    }
                    mAdapter = new ItemAdapter5();
                    mAdapter.setData(ItemsList5);
                    listView.setAdapter(mAdapter);
                }
            }
        });


    }
}