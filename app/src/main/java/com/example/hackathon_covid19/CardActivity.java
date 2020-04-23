package com.example.hackathon_covid19;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

import static android.widget.Toast.LENGTH_LONG;

public class CardActivity extends BaseNavigationActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    //bottom navigation method
    @Override
    public int getContentViewId() {
        return R.layout.activity_card;
    }

    @Override
    public int getNavigationMenuItemId() {
        return R.id.home;
    }

    LinearLayout mainView,infoLayout,secLayout;
    ImageButton infoBtn;
    CardView moh_cad,dashboard_card,covid_faq,who_page,state_data_card,twitter_card;
    TextView activeCaseTv,curedCasesTv,deathCaseTv;
    String active,cured,death;
    Button reloadBtn;
    String pConcatenated="";
    String qC="";
    String rC="";
    WebView webView;
    ProgressBar pbar;

    private ListView list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_card);

        mainView=findViewById(R.id.mainView);
        twitter_card=findViewById(R.id.twitter_card);
        secLayout=findViewById(R.id.secLayout);
        moh_cad=findViewById(R.id.moh_cad);
        //state_data_card=findViewById(R.id.state_data);
        who_page=findViewById(R.id.who_page);
        covid_faq=findViewById(R.id.covid_faq);
        dashboard_card=findViewById(R.id.dashboard);
        activeCaseTv=findViewById(R.id.active_case);
        curedCasesTv=findViewById(R.id.cured_cases);
        deathCaseTv=findViewById(R.id.death);

        list = (ListView) findViewById(R.id.listview);
        arrayList = new ArrayList<String>();

        // Adapter: You need three parameters 'the context, id of the layout (it will be where the data is shown),
        // and the array that contains the data
        adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item, arrayList);

        // Here, you set the data in your ListView
        list.setAdapter(adapter);

        requestPermission();
       // getData();
        moh_cad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLayout.setVisibility(View.GONE);
                startActivity(new Intent(CardActivity.this,MohWeb.class));
                finish();
            }
        });
//        state_data_card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String URL="https://www.mohfw.gov.in/pdf/DistrictWiseList324.pdf";
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));
//
//
//            }
//        });

        dashboard_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getData();
            }
        });

        covid_faq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLayout.setVisibility(View.GONE);
              String URL="https://www.mohfw.gov.in/pdf/FAQ.pdf";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));


            }
        });

        who_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLayout.setVisibility(View.GONE);
                startActivity(new Intent(CardActivity.this,ChatBot.class));
                finish();
            }
        });
        twitter_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String URL="https://twitter.com/MoHFW_INDIA";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(URL)));

            }
        });

        //https://www.mohfw.gov.in/pdf/DistrictWiseList324.pdf"   "https://twitter.com/MoHFW_INDIA" twitter
        new MyAyncTask().execute();

        webView=findViewById(R.id.tnp_webview);
        pbar=findViewById(R.id.progressBar1);



        //pbar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setVerticalScrollBarEnabled(true);
        webView.setHorizontalScrollBarEnabled(true);
        webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        webView.setWebViewClient(new WebViewClient() {
//
//            @Override
//            public void onPageFinished(WebView view, String url)
//            {
//
//                webView.loadUrl("javascript:(function() { " +
//                    "document.getElementsByTagName('header')[0].style.display='none'; " +
//                    "document.getElementsByClassName('footer-section')[0].style.display='none'; " +
//                    "document.getElementById('map').style.display='none'; " +
//                   "document.getElementsByClassName('barchart')[0].style.display='block'; " +
//                    "document.getElementsByClassName('col-md-12 ')[0].style.display='none'; " +
//                    "})()");
//
//
//            }
//        });
       // webView.loadUrl("https://www.mohfw.gov.in/dashboard/index.php#_ABSTRACT_RENDERER_ID_1");


        if (isNetworkAvailable())
        {
            webView.loadUrl("https://www.mohfw.gov.in/dashboard/index.php#_ABSTRACT_RENDERER_ID_1");

        }
        else
        {
            //  Snackbar.make(Apollo_Risk_Scan.this,"Please connect to internet...",Snackbar.LENGTH_LONG).show();
            Toast.makeText(this,"Please connect to internet!",LENGTH_LONG).show();
        }

        infoLayout = (LinearLayout)findViewById(R.id.infoLayout);
        infoBtn=findViewById(R.id.infoBtn);
        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
       final Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);

        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(infoLayout.getVisibility()==View.GONE){

                    infoLayout.startAnimation(slideUp);
                    infoLayout.setVisibility(View.VISIBLE);
                    webView.setAlpha(.5f);
                }else
                {
                    infoLayout.startAnimation(slideDown);
                    infoLayout.setVisibility(View.GONE);
                    webView.setAlpha(1);
                }
            }
        });




    }

    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub

            pbar.setVisibility(View.VISIBLE);
            webView.setAlpha(0.1f);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            webView.loadUrl("javascript:(function() { " +
                    "document.getElementsByTagName('header')[0].style.display='none'; " +
                    "document.getElementsByClassName('footer-section')[0].style.display='none'; " +
                    "document.getElementById('map').style.display='none'; " +
                    "document.getElementsByClassName('barchart')[0].style.display='block'; " +
                    "document.getElementsByClassName('col-md-12 ')[0].style.display='none'; " +
                    "})()");
            pbar.setVisibility(View.GONE);
            webView.setAlpha(1);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        //getData();

        if (!isNetworkAvailable()){

            activeCaseTv.setText("00");
            curedCasesTv.setText("00");
            deathCaseTv.setText("00");

           // Snackbar.make(mainView,"INTERNET disabled, Please connect to the Internet", BaseTransientBottomBar.LENGTH_INDEFINITE);
           // Snackbar.make(findViewById(android.R.id.content), "INTERNET disabled, Please connect to the Internet!", Snackbar.LENGTH_INDEFINITE).setAction("OK",);
            final Snackbar snackBar = Snackbar.make(findViewById(android.R.id.content), "Internet disabled, Please connect to the Internet!",Snackbar.LENGTH_INDEFINITE);

            snackBar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Call your action method here
                    snackBar.dismiss();
                }
            });
            //snackBar.setBackgroundTint(getResources().getColor(R.color.yellow));
            snackBar.show();
        }

        blickInfoIcon();

        secLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(infoLayout.getVisibility()==View.VISIBLE) {
                    infoLayout.setVisibility(View.GONE);
                    webView.setAlpha(1);
                }

            }
        });

    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    void getData(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                try {
                    String url="https://www.mohfw.gov.in/";//your website url
                    Document doc = Jsoup.connect(url).get();

                    Element body = doc.body();
                    builder.append(body.text());
                    Elements links = doc.select("a[href]");
                    Elements media = doc.select("[src]");
                    Elements imports = doc.select("link[href]");
                    Elements divs = doc.select("div#main-content");

                    Elements activeCases = doc.getElementsByClass("bg-blue");
                    Elements curedCases = doc.getElementsByClass("bg-green");
                    Elements deathCase = doc.getElementsByClass("bg-red");

                    Elements p= activeCases.tagName("strong");
                    Elements q= curedCases.tagName("strong");
                    Elements r= deathCase.tagName("strong");



                    String pConcatenated="";
                    for (Element x: p) {
                        pConcatenated+= x.text();
                    }
                    String qC="";
                    for (Element x: q) {
                        qC+= x.text();
                    }
                    String rC="";
                    for (Element x: r) {
                        rC+= x.text();
                    }

                    active= pConcatenated.replaceAll("[^0-9]", "");
                    activeCaseTv.setText(active);
                    activeCaseTv.setSelected(true);

                    cured= qC.replaceAll("[^0-9]", "");
                    curedCasesTv.setText(cured);

                    death= rC.replaceAll("[^0-9]", "");
                    deathCaseTv.setText(death);




//                     curedCasesTv.setSelected(true);

//                     deathCaseTv.setSelected(true);


                    Log.e("active", active);
                    Log.e("cured", cured);
                    Log.e("death", death);
                    Log.e("p", pConcatenated+"  "+qC+"  "+rC);



                    for (Element div : divs)
                        System.out.println(div.text());
                    Log.e("div",divs.text());

                    Log.e("media ", String.valueOf(media));
                    Log.e("links ", String.valueOf(links));




                } catch (Exception e) {
                    builder.append("Error : ").append(e.getMessage()).append("\n");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //result.setText(builder.toString());
                        Log.e("response",builder.toString());

                    }
                });
            }
        }).start();
    }

    void requestPermission(){
        if (Build.VERSION.SDK_INT >= 23) {

            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED &&checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");

            } else {

                //  Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            // Log.v(TAG,"Permission is granted");

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0:
                boolean isPerpermissionForAllGranted = false;
                if (grantResults.length > 0 && permissions.length==grantResults.length) {
                    for (int i = 0; i < permissions.length; i++){
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                            isPerpermissionForAllGranted=true;
                        }else{
                            isPerpermissionForAllGranted=false;
                        }
                    }

                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    isPerpermissionForAllGranted=false;
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                if(isPerpermissionForAllGranted){
                    //shoro();
                    // startLocation();


                }
                break;
        }
    }

    public class MyAyncTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            // Here you can show progress bar or something on the similar lines.
            // Since you are in a UI thread here.
            activeCaseTv.setText("");
            curedCasesTv.setText("");
            deathCaseTv.setText("");
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            // After completing execution of given task, control will return here.
            // Hence if you want to populate UI elements with fetched data, do it here.

            active= pConcatenated.replaceAll("[^0-9]", "");
            activeCaseTv.setText(active);
           // activeCaseTv.setSelected(true);

            cured= qC.replaceAll("[^0-9]", "");
            curedCasesTv.setText(cured);

            death= rC.replaceAll("[^0-9]", "");
            deathCaseTv.setText(death);

            Log.e("active", active);
            Log.e("cured", cured);
            Log.e("death", death);
            Log.e("p", pConcatenated+"  "+qC+"  "+rC);

            arrayList.add(active);
            arrayList.add(cured);
            arrayList.add(death);
            adapter.notifyDataSetChanged();

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            // You can track you progress update here
        }

        @Override
        protected Void doInBackground(Void... params) {
            // Here you are in the worker thread and you are not allowed to access UI thread from here.
            // Here you can perform network operations or any heavy operations you want.

            final StringBuilder builder = new StringBuilder();

            try {
                String url="https://www.mohfw.gov.in/";//your website url
                Document doc = Jsoup.connect(url).get();

                Element body = doc.body();
                builder.append(body.text());
                Elements links = doc.select("a[href]");
                Elements media = doc.select("[src]");
                Elements imports = doc.select("link[href]");
                Elements divs = doc.select("div#main-content");


                Elements stateData = doc.getElementsByClass("table table-striped");
               // String x=stateData.attr()
                Elements rows = doc.select("tr");
                for(org.jsoup.nodes.Element row :rows)
                {
                    org.jsoup.select.Elements columns = row.select("td");
                    for (org.jsoup.nodes.Element column:columns)
                    {
                        System.out.print(column.text());
                        Log.e("tDAta", String.valueOf(column.text()));
                    }
                    System.out.println();
                }

                StringBuilder str = new StringBuilder();
                Element tableHeader = doc.select("tr").get(1);
                for( Element element : tableHeader.children() )
                {
                    // Here you can do something with each element
                    System.out.println(element.text());
                    Log.e("dDAta", String.valueOf(element.text()));


                }
                Log.e("stateDAta", String.valueOf(stateData));

                Elements activeCases = doc.getElementsByClass("bg-blue");
                Elements curedCases = doc.getElementsByClass("bg-green");
                Elements deathCase = doc.getElementsByClass("bg-red");

                Elements p= activeCases.tagName("strong");
                Elements q= curedCases.tagName("strong");
                Elements r= deathCase.tagName("strong");




                for (Element x: p) {
                    pConcatenated+= x.text();
                }

                for (Element x: q) {
                    qC+= x.text();
                }

                for (Element x: r) {
                    rC+= x.text();
                }


                for (Element div : divs)
                    System.out.println(div.text());
                Log.e("div",divs.text());

                Log.e("media ", String.valueOf(media));
                Log.e("links ", String.valueOf(links));




            } catch (Exception e) {
                builder.append("Error : ").append(e.getMessage()).append("\n");
            }
            return null;
        }
    }

    void blickInfoIcon(){

        Animation animation = new AlphaAnimation(1, 0); //to change visibility from visible to invisible
        animation.setDuration(1000); //1 second duration for each animation cycle
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE); //repeating indefinitely
        animation.setRepeatMode(Animation.REVERSE); //animation will start from end point once ended.
        infoBtn.startAnimation(animation); //t
    }



}
