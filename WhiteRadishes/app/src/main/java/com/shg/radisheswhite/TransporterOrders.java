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

public class TransporterOrders extends AppCompatActivity {

    public class Item6 {
        String ordern;
        String odate;
        String ototal;
        String opic;
        String odel;
        String ods;
        String compen;


        // Constructor that is used to create an instance of the Movie object
        public Item6(String ordern, String odate, String ototal, String opic, String odel, String ods, String compen) {
            this.ordern = ordern;
            this.odate = odate;
            this.ototal = ototal;
            this.opic = opic;
            this.odel = odel;
            this.ods = ods;
            this.compen = compen;


        }

        public void setordern(String ordern) {
            this.ordern = ordern;
        }

        public void setodate(String odate) {
            this.odate = odate;
        }

        public void setototal(String ototal) {
            this.ototal = ototal;
        }

        public void setopic(String opic) {
            this.opic = opic;
        }

        public void setodel(String odel) {
            this.odel = odel;
        }

        public void setods(String ods) {
            this.ods = ods;
        }

        public void setcompen(String compen) {
            this.compen = compen;
        }

        public String getordern() {
            return this.ordern;
        }

        public String getodate() {
            return this.odate;
        }

        public String getototal() {
            return this.ototal;
        }

        public String getopic() {
            return this.opic;
        }

        public String getodel() {
            return this.odel;
        }

        public String getods() {
            return this.ods;
        }

        public String getcompen() {
            return this.compen;
        }
    }

    class ItemAdapter6 extends BaseAdapter {

        private List<Item6> itemdata = new ArrayList<>();

        void setData(List<Item6> mData) {
            itemdata.clear();
            itemdata.addAll(mData);
            notifyDataSetChanged();
        }


        @Override
        public int getCount() {
            return itemdata.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.transporter_order_item, parent, false);
            }

            Item6 currentItem = itemdata.get(position);

            TextView odval = (TextView) convertView.findViewById(R.id.odval);
            odval.setText(currentItem.getodate());

            TextView oidval = (TextView) convertView.findViewById(R.id.oidval);
            oidval.setText(currentItem.getordern().substring(0,6));

            TextView ototalval = (TextView) convertView.findViewById(R.id.ototalval);
            ototalval.setText(currentItem.getototal());

            TextView opicval = (TextView) convertView.findViewById(R.id.opicval);
            opicval.setText(currentItem.getopic());

            TextView odelval = (TextView) convertView.findViewById(R.id.odelval);
            odelval.setText(currentItem.getodel());

            TextView dsval = (TextView) convertView.findViewById(R.id.dsval);
            dsval.setText(currentItem.getods());

            TextView compensationval = (TextView) convertView.findViewById(R.id.compval);
            compensationval.setText(currentItem.getcompen());

            TextView smbtn = (TextView) convertView.findViewById(R.id.smbtn);
            smbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getordern());
                    Intent i=new Intent(TransporterOrders.this, CurrentOrderDetails_Transporter.class);
                    i.putExtras(b);
                    startActivity(i);
                    finish();
                }
            });


            return convertView;
        }


    }

    //creating listview instance
    private ListView listView;
    //creating Itemadapter instance
    private ItemAdapter6 mAdapter;
    private ArrayList<Item6> ItemsList6;
    CollectionReference freference;
    FirebaseAuth auth;

    //****user order page ends*****


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_orders);

        listView = (ListView) findViewById(R.id.itemlist6);
        //List of Item objects we need to add
        ItemsList6 = new ArrayList<>();

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
                        if(userid.equals((String) snapshot.get("transporter_id")) && ds.equals("In Transit")){
                            String oid = snapshot.getId().toString();
                            String odate = (String) snapshot.get("order_date");
                            String ototal = (String) snapshot.get("order_total");
                            String compensation = (String) snapshot.get("compensation");
                            String dbefore = (String) snapshot.get("deliver_before");
                            String pickup = (String) snapshot.get("deliver_from");
                            ItemsList6.add(new Item6(oid, odate, ototal,pickup, ds, dbefore, compensation));
                        }
                    }
                    mAdapter = new ItemAdapter6();
                    mAdapter.setData(ItemsList6);
                    listView.setAdapter(mAdapter);
                }
            }
        });



    }
}