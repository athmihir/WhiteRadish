package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.os.Bundle;

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

public class NewOrders extends AppCompatActivity {

    public class Item8 {
        String dt;
        String pb;
        String pu;
        String db;
        String dr;
        String comp;



        // Constructor that is used to create an instance of the Movie object
        public Item8(String dt, String pb, String pu, String db, String dr, String comp) {
            this.dt = dt;
            this.pb = pb;
            this.pu = pu;
            this.db = db;
            this.dr = dr;
            this.comp = comp;



        }

        public void setdt(String dt) {
            this.dt = dt;
        }

        public void setpb(String pb) {
            this.pb = pb;
        }

        public void setpu(String pu) {
            this.pu = pu;
        }

        public void setdb(String db) {
            this.db = db;
        }

        public void setdr(String dr) {
            this.dr = dr;
        }

        public void setcomp(String comp) {
            this.comp = comp;
        }

        public String getdt() {
            return this.dt;
        }

        public String getpb() {
            return this.pb;
        }

        public String getpu() {
            return this.pu;
        }

        public String getdb() { return this.db; }

        public String getdr() {
            return this.dr;
        }

        public String getcomp() {
            return this.comp;
        }

    }


    class ItemAdapter8 extends BaseAdapter {

        private List<Item8> itemdata = new ArrayList<>();

        void setData(List<Item8> mData) {
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
                        inflate(R.layout.new_orders_item, parent, false);
            }

            Item8 currentItem = itemdata.get(position);

            TextView oidval = (TextView) convertView.findViewById(R.id.oidval);
            oidval.setText(currentItem.getdt().substring(0,7));

            TextView pbval = (TextView) convertView.findViewById(R.id.pbval);
            pbval.setText(currentItem.getpb());

            TextView puval = (TextView) convertView.findViewById(R.id.puval);
            puval.setText(currentItem.getpu());

            TextView dbval = (TextView) convertView.findViewById(R.id.dbval);
            dbval.setText(currentItem.getdb());

            TextView drval = (TextView) convertView.findViewById(R.id.drval);
            drval.setText(currentItem.getdr());

            TextView compval = (TextView) convertView.findViewById(R.id.compval);
            compval.setText(currentItem.getcomp());

            TextView smbtn = (TextView) convertView.findViewById(R.id.smbtn);
            smbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getdt());
                    b.putString("stuff2", currentItem.getdb());
                    b.putString("stuff3", tcontact);
                    Intent i=new Intent(NewOrders.this, NewOrderDetails_Transporter.class);
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
    private ItemAdapter8 mAdapter;
    private ArrayList<Item8> ItemsList8;
    CollectionReference freference;
    FirebaseAuth auth;
    String tcontact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_orders);

        listView = (ListView) findViewById(R.id.itemlist8);
        //List of Item objects we need to add
        ItemsList8= new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        DocumentReference freferencelater = FirebaseFirestore.getInstance().document("Transporter/" + userid);
        freferencelater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                    tcontact = documentSnapshot.get("contact").toString();
                }

            }
        });

        freference = FirebaseFirestore.getInstance().collection("Orders/");
        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        if(((String) snapshot.get("delivery_status")).equals("processing")){
                            String oid = snapshot.getId().toString();
                            String pick_up = (String) snapshot.get("deliver_from");
                            String deliver = (String) snapshot.get("deliver_to");
                            String compensation = (String) snapshot.get("compensation");
                            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
                            Calendar calobj = Calendar.getInstance();
                            calobj.add(Calendar.DAY_OF_MONTH, 1);
                            String pick_up_by = df.format(calobj.getTime());
                            calobj.add(Calendar.DAY_OF_MONTH, 2);
                            String deliver_by = df.format(calobj.getTime());
                            ItemsList8.add(new Item8(oid, pick_up_by, pick_up, deliver_by, deliver, compensation));
                        }
                    }
                    mAdapter = new ItemAdapter8();
                    mAdapter.setData(ItemsList8);
                    listView.setAdapter(mAdapter);
                }
            }
        });

        //for drawer menu
        ImageButton menubutton = findViewById(R.id.menubtn);
        final PopupMenu dropDownMenu = new PopupMenu(getBaseContext(), menubutton);
        final Menu menu = dropDownMenu.getMenu();
        // add your items:
        menu.add(0, 0, 0, "Profile");

        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 0:
                        // item ID 0 was clicked
                        Intent intent = new Intent(NewOrders.this, TransporterProfile.class);
                        startActivity(intent);
                        return true;
                }
                return false;
            }
        });

        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropDownMenu.show();
            }
        });

    }
}