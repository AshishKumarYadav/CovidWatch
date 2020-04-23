package com.example.hackathon_covid19;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


public abstract class BaseNavigationActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sh_prefs;

    FloatingActionButton floatingActionButton;
    protected BottomNavigationView bottomNavigationView;
    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        mContext = BaseNavigationActivity.this;

         bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottomNav);
//         floatingActionButton=findViewById(R.id.fab);



        bottomNavigationView.clearAnimation();
        bottomNavigationView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setItemIconTintList(null);






    }



    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){

                case R.id.home:
                    startActivity(new Intent(this,CardActivity.class));
                    //finish();

                    break;
                case R.id.store:
                    startActivity(new Intent(this,MainActivity.class));
                    finish();

                    break;

                case R.id.essentials:
                    startActivity(new Intent(this, EssentialsAct.class));
                    finish();
                    break;

                case R.id.chat:
                    startActivity(new Intent(this, Apollo_Risk_Scan.class));
                    finish();
                    break;

            }

//            Intent main_activity_intent1 = new Intent(this, MDAMainActivity.class);
//            main_activity_intent1.putExtra("message_screen","launch_message_screen");
//            startActivity(main_activity_intent1);
                // finish();
//          Toast.makeText(getContext(), "base_attach", Toast.LENGTH_SHORT).show();

        return true;


    }
    @Override
    protected void onStart() {
        super.onStart();
        updateNavigationBarState();
    }

    public void updateNavigationBarState() {
        int actionId = getNavigationMenuItemId();
        selectBottomNavigationBarItem(actionId);
    }

    void selectBottomNavigationBarItem(int itemId) {
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0, size = menu.size(); i < size; i++) {
            MenuItem item = menu.getItem(i);
            boolean shouldBeChecked = item.getItemId() == itemId;
            if (shouldBeChecked) {
                item.setChecked(true);
                break;
            }
        }
    }

    public abstract int getContentViewId();

    public abstract int getNavigationMenuItemId();

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //DebugLog.debug(TAG, "onCreateOptionsMenu Called. ");
//
//
//         //   getMenuInflater().inflate(R.menu.menu, menu);
//            return true;
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.appolo:
//                Intent settings_intent_one = new Intent(getApplicationContext(),
//                        Apollo_Risk_Scan.class);
//                startActivity(settings_intent_one);
//                return true;
//            case R.id.moh:
//                Intent intent_one = new Intent(getApplicationContext(),
//                        CardActivity.class);
//                startActivity(intent_one);
//                return true;
//
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
