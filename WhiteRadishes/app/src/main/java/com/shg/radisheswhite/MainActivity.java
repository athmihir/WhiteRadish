package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.text.*;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    //******************for main page**************8
    // This is the item class for the products
    public class Item{
        String pname;
        String pidval;
        String sellerval;
        String qtyval;
        String priceval;
        String Pid;
        String Uid;

        // Constructor that is used to create an instance of the Movie object
        public Item(String pname, String pidval, String sellerval, String qtyval, String priceval, String Pid, String Uid) {
            this.pname = pname;
            this.pidval = pidval;
            this.sellerval = sellerval;
            this.qtyval = qtyval;
            this.priceval = priceval;
            this.Pid = Pid;
            this.Uid = Uid;

        }
        public void setpname(String pname){
            this.pname = pname;
        }
        public void setpidval(String pidval){
            this.pidval = pidval;
        }
        public void setsellerval(String sellerval){
            this.pname = sellerval;
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
        public String getpidval(){
            return this.pidval;
        }
        public String getsellerval(){
            return this.sellerval;
        }
        public String getqtyval(){
            return this.qtyval;
        }
        public String getpriceval(){
            return this.priceval;
        }
        public String getPid(){
            return this.Pid;
        }
        public String getUid(){
            return this.Uid;
        }



    }
    //This is the item adapter for the listview
    class ItemAdapter extends BaseAdapter {

        private List<Item> itemdata = new ArrayList<>();
        void setData(List<Item> mData){
            itemdata.clear();
            itemdata.addAll(mData);
            notifyDataSetChanged();
        }
        //for search bar functionality
        void setData2(List<Item> mData, final CharSequence searchparams){
            itemdata.clear();
            for (Item temp : mData) {
                if(temp.getpname().toLowerCase().startsWith(searchparams.toString())){
                    itemdata.add(temp);
                }
                else if(temp.getpname().startsWith(searchparams.toString())){
                    itemdata.add(temp);
                }
            }
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
        public View getView(int position, View convertView, ViewGroup parent){
            if (convertView == null){
                convertView = LayoutInflater.from(parent.getContext()).
                        inflate(R.layout.buyitem, parent,false);
            }

            Item currentItem = itemdata.get(position);

            TextView pname = (TextView) convertView.findViewById(R.id.pname);
            pname.setText(currentItem.getpname());

            TextView pidval = (TextView) convertView.findViewById(R.id.pidval);
            pidval.setText(currentItem.getpidval());

            TextView sellerval = (TextView) convertView.findViewById(R.id.sellerval);
            sellerval.setText(currentItem.getsellerval());

            TextView qtyval = (TextView) convertView.findViewById(R.id.qtyval);
            qtyval.setText(currentItem.getqtyval());

            TextView priceval = (TextView) convertView.findViewById(R.id.priceval);
            priceval.setText(currentItem.getpriceval());

            ImageView plusbtn = convertView.findViewById(R.id.plusbtn);
            plusbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userid = firebaseUser.getUid();
                    CollectionReference freferencelater = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");
                    freferencelater.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){
                                boolean conti = false;
                                for (QueryDocumentSnapshot snapshot: task.getResult()){
                                    String pid = (String) snapshot.get("product_id");
                                    if(pid.equals(currentItem.getPid())){
                                        conti = true;
                                    }
                                }
                                if(!conti){
                                    finish();
                                    Bundle b = new Bundle();
                                    b.putString("stuff", currentItem.getPid());
                                    b.putString("stuff2", currentItem.getUid());
                                    Intent i=new Intent(MainActivity.this, AddToCart.class);
                                    i.putExtras(b);
                                    startActivity(i);
                                }
                                else{
                                    Toast.makeText(MainActivity.this, "Item has been already added to shopping cart.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
                }
            });
            return convertView;
        }

    }
    //creating listview instance
    private ListView listView;
    //creating Itemadapter instance
    private ItemAdapter mAdapter;
    private ArrayList<Item> ItemsList;
    private EditText searchbar;
    private ImageView menubutton;
    FirebaseAuth auth;

    //initializing the carouselview
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    CollectionReference freference;
    //*************main page ends**************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //***************************Main page**********************

        freference = FirebaseFirestore.getInstance().collection("Farmer");
        ArrayList<String> allKeys = new ArrayList<String>();
        //Listview is pointing to the listview we created
        listView = findViewById(R.id.itemlist);
        //List of Item objects we need to add
        ItemsList = new ArrayList<>();
        //search bar functionality

        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentID :
                            task.getResult()) {
                        String key = documentID.getId();
                        FirebaseFirestore.getInstance().collection("Farmer/"+key + "/product").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                                        String pid = snapshot.getId().toString();
                                        String name = (String) snapshot.get("name");
                                        String price = (String) snapshot.get("price");
                                        String quantity = (String) snapshot.get("quantity");
                                        ItemsList.add(new Item(name, pid.substring(0, 5), key.substring(0,5) ,quantity, price, pid , key));
                                    }
                                    mAdapter = new ItemAdapter();
                                    mAdapter.setData(ItemsList);
                                    listView.setAdapter(mAdapter);

                                }
                            }
                        });

                    }
                }
            }
        });


        searchbar = (EditText)findViewById(R.id.search_bar);
        searchbar.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){

                    mAdapter.setData2(ItemsList, s);
                }
                else{
                    mAdapter.setData(ItemsList);
                }

            }
        });
        //for drawer menu
        menubutton = findViewById(R.id.menubtn);
        final PopupMenu dropDownMenu = new PopupMenu(getBaseContext(), menubutton);
        final Menu menu = dropDownMenu.getMenu();
        // add your items:
        menu.add(0, 0, 0, "Profile");
        menu.add(0, 1, 1, "Logout");

        dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1:
                        // item ID 1 was clicked
                        Intent intent = new Intent(MainActivity.this, StartActivity.class);
                        startActivity(intent);
                        return true;

                    case 0:
                        //item ID 1 was clicked
                        Intent intent2 = new Intent(MainActivity.this, UserProfile.class);
                        startActivity(intent2);
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

        // carousel view logic
        carouselView = findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
//        *****************************************main page ends*******************************
    }
}