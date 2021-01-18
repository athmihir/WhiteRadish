package com.shg.radisheswhite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static java.lang.Integer.parseInt;
import static java.lang.Integer.valueOf;

public class ShoppingCart extends AppCompatActivity {

    //******************for shopping cart**************8
    // This is the item class for the products
    public class Item{
        String pname;
        String pidval;
        String sellerval;
        String qtyval;
        String priceval;
        String cartItemID;

        // Constructor that is used to create an instance of the Movie object
        public Item(String pname, String pidval, String sellerval, String qtyval, String priceval, String cartItemID) {
            this.pname = pname;
            this.pidval = pidval;
            this.sellerval = sellerval;
            this.qtyval = qtyval;
            this.priceval = priceval;
            this.cartItemID = cartItemID;

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
        public String getCartItemID(){
            return this.cartItemID;
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
                        inflate(R.layout.cartitem, parent,false);
            }

            Item currentItem = itemdata.get(position);

            TextView pname = (TextView) convertView.findViewById(R.id.pname);
            pname.setText(currentItem.getpname());

            TextView pidval = (TextView) convertView.findViewById(R.id.pidval);
            pidval.setText(currentItem.getpidval().substring(0,5));

            TextView sellerval = (TextView) convertView.findViewById(R.id.sellerval);
            sellerval.setText(currentItem.getsellerval().substring(0,5));

            TextView qtyval = (TextView) convertView.findViewById(R.id.qtyval);
            qtyval.setText(currentItem.getqtyval());

            TextView priceval = (TextView) convertView.findViewById(R.id.priceval);
            priceval.setText(currentItem.getpriceval());

            ImageView editbtn = convertView.findViewById(R.id.editbtn);
            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    Bundle b = new Bundle();
                    b.putString("stuff", currentItem.getCartItemID());
                    b.putString("stuff2", currentItem.getpidval());
                    b.putString("stuff3", currentItem.getsellerval());
                    b.putString("stuff4", currentItem.getqtyval());

                    Intent i=new Intent(ShoppingCart.this, EditProduct2.class);
                    i.putExtras(b);
                    startActivity(i);
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
    private ImageButton checkoutbtn;
    FirebaseAuth auth;
    CollectionReference freference;
    Multimap<String, HashMap<String, String>> Items;
    private CollectionReference freference4;
    ArrayList<HashMap<String, String>> Productlist;
    String orderid, farmerid;
    String location;
    //*************shopping cart page ends**************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);

        //***************************shopping cart page**********************8
        //Listview is pointing to the listview we created
        listView = (ListView) findViewById(R.id.itemlist3);
        //List of Item objects we need to add
        ItemsList = new ArrayList<>();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();

        DocumentReference freferencelater = FirebaseFirestore.getInstance().document("Customer/"+ userid);
        freferencelater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot){
                if(documentSnapshot.exists()){
                     location = documentSnapshot.get("location").toString();
                }
            }
        });

        freference = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");
        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot snapshot: task.getResult()){
                        String cid = snapshot.getId().toString();
                        String pid = (String) snapshot.get("product_id");
                        String seller_id = (String) snapshot.get("farmer_id");
                        String name = (String) snapshot.get("name");
                        String price = (String) snapshot.get("price");
                        String quantity = (String) snapshot.get("quantity");
                        ItemsList.add(new Item(name, pid, seller_id, quantity, price, cid));
                    }
                    mAdapter = new ItemAdapter();
                    mAdapter.setData(ItemsList);
                    listView.setAdapter(mAdapter);
                }
            }
        });

        checkoutbtn = findViewById(R.id.checkoutbtn);
        checkoutbtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                cleanCart();

                Items= ArrayListMultimap.create();
                freference = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");
                //putting items in hashmap
                freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot documentID :
                                    task.getResult()) {
                                String c_id = documentID.getId().toString();
                                String p_id = documentID.getString("product_id");
                                String f_id = documentID.getString("farmer_id");
                                String name = documentID.getString("name");
                                String cart_quantity = documentID.getString("quantity");
                                String cart_price = documentID.getString("price");

                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("product_id", p_id);
                                hashMap.put("name", name);
                                hashMap.put("quantity",cart_quantity);
                                hashMap.put("price", cart_price);
                                Items.put(f_id, hashMap);

                            }
                            if(Items.isEmpty()){
                                Toast.makeText(ShoppingCart.this, "Cannot checkout empty cart. Please add an item.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //inserting orders using callback function from Items hashmap
                                callback1();
                            }

                        }
                    }
                });


            }
        });
//        *****************************************shopping cart page ends*******************************

    }

    //callback for multiple orders
    private void callback1(){

            Iterator itemsiterator = Items.entries().iterator();
            farmerid = "";
            int order_total = 0;
            Productlist= new ArrayList<>();
            while (itemsiterator.hasNext()) {
                HashMap.Entry mapElement = (HashMap.Entry)itemsiterator.next();
                if(farmerid == ""){
                    farmerid = (String) mapElement.getKey();
                }
                if(farmerid == (String) mapElement.getKey()){
                    Productlist.add((HashMap<String, String>) mapElement.getValue());
                    order_total +=( valueOf(((HashMap<String, String>) mapElement.getValue()).get("price")) * valueOf(((HashMap<String, String>) mapElement.getValue()).get("quantity"))   );
                    itemsiterator.remove();
                }
            }
            Log.e("plistlalal", Productlist.toString());
            Log.e("plistlalal", Integer.toString(Productlist.size()));

            auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();
            String userid = firebaseUser.getUid();
            CollectionReference freference3 = FirebaseFirestore.getInstance().collection("Orders/");

            HashMap<String, String> order = new HashMap<>();
            order.put("customer_id", userid);
            order.put("farmer_id", farmerid);
            order.put("transporter_id", "");
            order.put("order_total", Integer.toString(order_total));
            order.put("compensation", Integer.toString(Math.max(200,(int)(0.05 * order_total))));
            DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
            Calendar calobj = Calendar.getInstance();
            order.put("order_date", df.format(calobj.getTime()));
            order.put("deliver_before", "");
            order.put("delivery_status", "processing");
            order.put("delivered_on", "");
            order.put("transporter_contact", "");
            order.put("deliver_to", location);

            DocumentReference freferencelater = FirebaseFirestore.getInstance().document("Farmer/"+ farmerid);
            freferencelater.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot){
                    if(documentSnapshot.exists()){
                        order.put("farmer_name", documentSnapshot.get("name").toString());
                        order.put("deliver_from", documentSnapshot.get("location").toString());
                        freference3.add(order).addOnCompleteListener(task ->{
                            if (task.isSuccessful()) {
                                orderid = (String) task.getResult().getId();
                                freference4 = FirebaseFirestore.getInstance().collection("Orders/"+orderid+"/product");
                                callback2(farmerid);

                            }
                            else{
                                Toast.makeText(ShoppingCart.this, "Checkout Unsuccessful!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
    }
    //callback2 to enter each product for a particular order
    private void callback2(String fid){
        freference4 = FirebaseFirestore.getInstance().collection("Orders/"+orderid+"/product");
        freference4.add(Productlist.get(0)).addOnCompleteListener(task2 ->{
            if (task2.isSuccessful()){
                DocumentReference freference5 = FirebaseFirestore.getInstance().document("Farmer/" + fid + "/product/"+ Productlist.get(0).get("product_id"));
                freference5.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>(){
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot){
                        if(documentSnapshot.exists()){
                            String qty = documentSnapshot.get("quantity").toString();
                            if(valueOf(qty) == valueOf(Productlist.get(0).get("quantity"))){
                                freference5.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Productlist.remove(0);
                                        if(!Productlist.isEmpty()){
                                            callback2(fid);
                                        }
                                        else if(Productlist.isEmpty()){
                                            if (Items.isEmpty()) {
                                                Toast.makeText(ShoppingCart.this, "Checkout Successfull!", Toast.LENGTH_SHORT).show();
                                                deleteCart();
                                                finish();
                                                startActivity(new Intent(ShoppingCart.this, CheckoutWaiting.class));
                                            }
                                            else if (!Items.isEmpty()) {
                                                callback1();
                                            }
                                        }
                                        else {
                                            Toast.makeText(ShoppingCart.this, "Checkout Unsuccessful!", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                            else if(valueOf(qty) > valueOf(Productlist.get(0).get("quantity"))) {
                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("quantity", Integer.toString(valueOf(qty) - valueOf(Productlist.get(0).get("quantity"))));
                                freference5.set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Productlist.remove(0);
                                            if(!Productlist.isEmpty()){
                                                callback2(fid);
                                            }
                                            else if(Productlist.isEmpty()){
                                                if (Items.isEmpty()) {
                                                    Toast.makeText(ShoppingCart.this, "Checkout Successfull!", Toast.LENGTH_SHORT).show();
                                                    deleteCart();
                                                    finish();
                                                    startActivity(new Intent(ShoppingCart.this, CheckoutWaiting.class));
                                                }
                                                else if (!Items.isEmpty()) {
                                                    callback1();
                                                }
                                            }
                                            else {
                                                Toast.makeText(ShoppingCart.this, "Checkout Unsuccessful!", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {

                                        }
                                    }
                                });
                            }

                        }
                    }
                });
            }

        });

     }


    //clean cart function
    private void cleanCart(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        freference = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");

        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentID :
                            task.getResult()) {
                        String c_id = documentID.getId().toString();
                        String p_id = documentID.getString("product_id");
                        String f_id = documentID.getString("farmer_id");
                        String cart_quantity = documentID.getString("quantity");
                        String cart_price = documentID.getString("price");

                        DocumentReference freference2 = FirebaseFirestore.getInstance().document("Farmer/"+f_id + "/product/" + p_id);
                        freference2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    String quantity2 = documentSnapshot.get("quantity").toString();
                                    String price2 = documentSnapshot.get("price").toString();
                                    if (parseInt(quantity2) < parseInt(cart_quantity)) {
//                                        remove cart item
                                        DocumentReference freference3;
                                        freference3= FirebaseFirestore.getInstance().document("Customer/" + userid + "/cart/" + c_id);
                                        freference3.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                    }
                                                });

                                    } else if (parseInt(quantity2) >= parseInt(cart_quantity)) {

                                        //cleancart ie. set price in shopping cart to price at farmer. so as to accomodate any changes on farmers side.
                                        HashMap<String, String> hashMap = new HashMap<String, String>();
                                        hashMap.put("price", price2);
                                        DocumentReference freference3;
                                        freference3= FirebaseFirestore.getInstance().document("Customer/" + userid + "/cart/" + c_id);
                                        freference3.set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                } else {

                                                }
                                            }
                                        });

                                    }
                                } else {

                                    //remove cart item
                                    DocumentReference freference3;
                                    freference3= FirebaseFirestore.getInstance().document("Customer/" + userid + "/cart/" + c_id);
                                    freference3.delete();

                                }
                            }
                        });

                    }
                }
            }
        });

    }


    //Empty shopping cart
    private void deleteCart(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        freference = FirebaseFirestore.getInstance().collection("Customer/"+userid + "/cart");
        freference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (QueryDocumentSnapshot documentID :
                            task.getResult()) {
                        DocumentReference freference3;
                        freference3= FirebaseFirestore.getInstance().document("Customer/" + userid + "/cart/" + documentID.getId());
                        freference3.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });;

                    }
                }
            }
        });


    }


}