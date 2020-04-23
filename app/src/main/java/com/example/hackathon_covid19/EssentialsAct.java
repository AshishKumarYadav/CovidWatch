package com.example.hackathon_covid19;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

import static android.Manifest.permission_group.STORAGE;

public class EssentialsAct extends BaseNavigationActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    //bottom navigation method
    @Override
    public int getContentViewId() {
        return R.layout.activity_essentials;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.essentials;
    }


    TextView banner;
    String TAG=EssentialsAct.class.getSimpleName();
    FloatingActionButton floatingActionButton;
    StringBuilder result;
    String store_address,store_name="abc";
    Essentials_issue_Adapter adapter;
    public ArrayList<Essen_issuePOJO> essen_issuePOJOS;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef,getMyRef;
    String Username,userEmailId,UserMessage;
    String getMessage,fetchMessage,getName;
    String getMAil;
    String getAddress;
    String getTime;
    String SubLocality="";

    RecyclerView recyclerView;
    String NetworkError="<b> <font color='#F44336'>Please connect to the Internet</font></b>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG,"onCreate_called");

        banner=findViewById(R.id.banner);
        floatingActionButton=findViewById(R.id.fab);
        // editText=findViewById(R.id.edit_store_name);
        essen_issuePOJOS= new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        initRecyclerView();

        preferences =getSharedPreferences(STORAGE, Context.MODE_PRIVATE);
        editor = preferences.edit();


        Username=preferences.getString("User_name","");
        userEmailId=preferences.getString("Email_id","");
        SubLocality=preferences.getString("SubLocality","");

        Log.e("userInfo",Username+" ,"+userEmailId+","+SubLocality);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                showForgotDialog(EssentialsAct.this);

//                 Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                 startActivityForResult(takePicture, 0);//zero can be replaced with any action code (called requestCode)

            }
        });

        if (isNetworkAvailable()){
            SubLocality=preferences.getString("SubLocality","");
            if (!SubLocality.isEmpty()){
                pullData();
            }else {

                String tx="No data found in your "+"<b> <font color='#F44336'>"+SubLocality+"</font></b>"+" area.";
                banner.setText(Html.fromHtml(tx), TextView.BufferType.SPANNABLE);

            }


        }else banner.setText(Html.fromHtml(NetworkError), TextView.BufferType.SPANNABLE);

        //Toasty.error(EssentialsAct.this, "Please, connect to Internet", Toast.LENGTH_SHORT, true).show();


    }

    @Override
    protected void onResume() {
        super.onResume();

      //  SubLocality=preferences.getString("SubLocalityName","");
        String txt="Issues faced by people in your "+"<b> <font color='#F44336'>"+SubLocality+"</font></b>"+" area.";
        String str="No issue found in your "+"<b> <font color='#F44336'>"+SubLocality+"</font></b>"+" area.If there is any,kindly report it here.";
        SubLocality=preferences.getString("SubLocality","");
        if (SubLocality.isEmpty()){

            banner.setText(Html.fromHtml(str), TextView.BufferType.SPANNABLE);

        }else{

            banner.setText(Html.fromHtml(txt), TextView.BufferType.SPANNABLE);



            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    // progressDialog.dismiss();

                    if (isNetworkAvailable()){
                        // SubLocality=preferences.getString("SubLocality","");

                        pullData();

                    }else banner.setText(Html.fromHtml(NetworkError), TextView.BufferType.SPANNABLE);

                }
            }, 1000);
        }

        if (!isNetworkAvailable()){
            banner.setText(Html.fromHtml(NetworkError), TextView.BufferType.SPANNABLE);
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void initRecyclerView() {



        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //recyclerView.setItemViewCacheSize(5);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);






    }

    private void showForgotDialog(Context c) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter your message")
                .setMessage(" Type message")
                .setView(taskEditText)
                .setCancelable(false)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserMessage = String.valueOf(taskEditText.getText());
                        Log.e("estoreName",UserMessage);
                        if (UserMessage.length()>1)
                        {
                            sentDataTofb();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
        dialog.show();
    }
    void sentDataTofb(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
        String datetime = dateformat.format(c.getTime());

        final String path=datetime+"/";
        myRef=database.getReference().child("user_issue").child(SubLocality).child(path);

//        StorePOJO stP=new StorePOJO();
//
//        stP.setImage(imageEncoded);
//        stP.setLocation(store_address);
//        stP.setTime(datetime);
//        stP.setUploader(username);
//        myRef.setValue(stP);

        myRef.child("Username").setValue(Username);
        myRef.child("Email_id").setValue(userEmailId);
        myRef.child("Time").setValue(datetime);
        myRef.child("Address").setValue(store_address);
        myRef.child("message").setValue(UserMessage);
       // myRef.child("store_name").setValue(getStoreName);




        Log.d(TAG,"Data sent to fb");

    }
    void pullData(){
        getMyRef=database.getReference().child("user_issue").child(SubLocality);
//        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference =    mFirebaseDatabase.getReference().child("data");
        getMyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                essen_issuePOJOS.removeAll(essen_issuePOJOS);

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.v(TAG,"key"+ childDataSnapshot.getKey()); //displays the key for the node
                    Log.v(TAG,"child"+ childDataSnapshot);
                    //gives the value for given keyname

                    getName= (String) childDataSnapshot.child("Username").getValue();
                    getMAil= (String) childDataSnapshot.child("Email_id").getValue();
                    getAddress= (String) childDataSnapshot.child("Address").getValue();
                    getTime= (String) childDataSnapshot.child("Time").getValue();
                    getMessage= (String) childDataSnapshot.child("message").getValue();


                    essen_issuePOJOS.add(new Essen_issuePOJO(getName,getMAil,getMessage,getTime,getAddress));
                    // addToList();
                    Log.e("datasnap", String.valueOf(dataSnapshot));
                    Log.e("fetchInfo",getName+" "+getMAil+" "+getTime+" "+getAddress+" "+getMessage);
                    Log.d(TAG,"fetching_from_fb");
                    // Log.e("storeName",fetchStoreName);
                  //  Log.d(TAG,"storName"+fetchStoreName);
                    //Log.d(TAG,"fetching"+storedata);
                }
                Log.e("fetchInfo",getName+" "+getMAil);

                adapter = new Essentials_issue_Adapter(essen_issuePOJOS, getApplication());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }


    @Override
    public void onBackPressed() {

        BackToHome();
        finishAffinity();
        // super.onBackPressed();

    }

    public void BackToHome()
    {
        Intent intent = new Intent(EssentialsAct.this,CardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
