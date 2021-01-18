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

public class TransporterHistory extends AppCompatActivity {

    public class Item7 {
        String ordern;
        String odate;
        String ototal;
        String odel;
        String loc;
        String comp;


        // Constructor that is used to create an instance of the Movie object
        public Item7(String ordern, String odate, String ototal, String odel, String loc, String comp) {
            this.ordern = ordern;
            this.odate = odate;
            this.ototal = ototal;
            this.odel = odel;
            this.loc = loc;
            this.comp = comp;


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

        public void setodel(String odel) {
            this.odel = odel;
        }

        public void setloc(String loc) {
            this.loc = loc;
        }

        public void setcomp(String comp) {
            this.comp = comp;
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

        public String getodel() {
            return this.odel;
        }

        public String getloc() {
            return this.loc;
        }

        public String getcomp() {
            return this.comp;
        }
    }


    class ItemAdapter7 extends BaseAdapter {

        private List<Item7> itemdata = new ArrayList<>();

        void setData(List<Item7> mData) {
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
                        inflate(R.layout.transporter_history_item, parent, false);
            }

            Item7 currentItem = itemdata.get(position);

            TextView odval = (TextView) convertView.findViewById(R.id.odval);
            odval.setText(currentItem.getodate());

            TextView oidval = (TextView) convertView.findViewById(R.id.oidval);
            oidval.setText(currentItem.getordern().substring(0,6));

            TextView ototalval = (TextView) convertView.findViewById(R.id.ototalval);
            ototalval.setText(currentItem.getototal());

            TextView odelval = (TextView) convertView.findViewById(R.id.odelval);
            odelval.setText(currentItem.getodel());

            TextView locval = (TextView) convertView.findViewById(R.id.locval);
            locval.setText(currentItem.getloc());

            TextView compval = (TextView) convertView.findViewById(R.id.compval);
            compval.setText(currentItem.getcomp());

            TextView smbtn = (TextView) convertView.findViewById(R.id.smbtn);
            smbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getordern());
                    Intent i=new Intent(TransporterHistory.this, HistoryOrderDetails_Transporter.class);
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
    private ItemAdapter7 mAdapter;
    private ArrayList<Item7> ItemsList7;
    CollectionReference freference;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_history);


        listView = (ListView) findViewById(R.id.itemlist7);
        //List of Item objects we need to add
        ItemsList7 = new ArrayList<>();

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
                        if(userid.equals((String) snapshot.get("transporter_id")) && ds.equals("delivered")){
                            String oid = snapshot.getId().toString();
                            String odate = (String) snapshot.get("order_date");
                            String ototal = (String) snapshot.get("order_total");
                            String compensation = (String) snapshot.get("compensation");
                            String deliver_to = (String) snapshot.get("deliver_to");
                            String don = (String) snapshot.get("delivered_on");
                            ItemsList7.add(new Item7(oid, odate, ototal, don, deliver_to, compensation));
                        }
                    }
                    mAdapter = new ItemAdapter7();
                    mAdapter.setData(ItemsList7);
                    listView.setAdapter(mAdapter);
                }
            }
        });

    }
}