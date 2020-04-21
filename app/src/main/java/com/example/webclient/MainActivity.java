package com.example.webclient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    WebView web;
    EditText site;
    ArrayList<String> urlList = new ArrayList<String>();
    String currentUrl = "http://www.google.fi"; //homepage
    int webIndex = 0;
    Boolean newPage = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        site = findViewById(R.id.editText);

        web = findViewById(R.id.webView);
        WebViewClient wvc = new WebViewClient(){
            @Override
            public void onPageStarted (WebView view, String url ,Bitmap favicon){
                currentUrl = url;
                site.setText(url);
                historyHandler(url);
                newPage=true;
            }

        };



        web.setWebViewClient(wvc);

        web.getSettings().setJavaScriptEnabled(true);

        web.loadUrl("http://www.google.fi");
        //urlList.add(currentUrl);
        //webIndex++;




        site.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    currentUrl = urlCompiler(site.getText().toString());
                    //historyHandler(currentUrl);
                    //urlList.add(currentUrl);
                    newPage = true;
                    web.loadUrl(currentUrl);
                    System.out.println("here we are mennään "+currentUrl);
                }
            }
        });




    }
    private void historyHandler(String s){
        if(urlList.size()>=10){ // eka vai vika?
            urlList.remove(0);
            webIndex--;
        }

        if(urlList.size()==0){ //adds the first element
            urlList.add(this.webIndex,s);
            this.webIndex++;
        }
        else if(!urlList.get(this.webIndex-1).equals(s)){ //after this.
            if(this.webIndex == urlList.size()){
                urlList.add(s);
                webIndex++;
            }else if (newPage==true){
                urlList.add(this.webIndex, s);

                ListIterator<String> it = urlList.listIterator(); //removes all the extra urls

                while (it.hasNext()) {
                    it.next();
                    if(it.nextIndex()>(webIndex+1)) {
                        it.remove();
                    }
                }
            }
        }

        System.out.println(urlList+ " "+ webIndex + " urlsize"+ urlList.size());
    }
    public void previousPage(View v){
        ListIterator<String> it = urlList.listIterator();
        while(it.hasNext() && (it.nextIndex()<(webIndex-1))){ // goes to current index position.
            Object element = it.next();
            System.out.println("Listan elementit "+ element);
        }
        if (it.hasPrevious()) {
            newPage = false;
            //it.previous();
            String element = it.previous();
            System.out.println("tänne mennään" + element+" web indexi on" + (webIndex-1));
            this.currentUrl = element;
            webIndex--;
            //historyHandler(currentUrl);
            //urlList.add(currentUrl);

            web.loadUrl(currentUrl);
            site.setText(currentUrl);
            System.out.println("here we are  "+currentUrl);
        }
    }
    public void nextPage(View v){
        ListIterator<String> it = urlList.listIterator();
        while(it.hasNext() && (it.nextIndex()<webIndex)){// goes to current index position.
            Object element = it.next();
            System.out.println("Listan elementit "+ element);
        }
        if (it.hasNext()) {
            newPage = false;
            String element = it.next();
            System.out.println("tänne mennään" + element+" web indexi on" + (webIndex+1));
            this.currentUrl = element;
            webIndex++;
            //historyHandler(currentUrl);
            //urlList.add(currentUrl);
            web.loadUrl(currentUrl);
            site.setText(currentUrl);
            System.out.println("here we are  "+currentUrl);
        }
    }

    private String urlCompiler(String s){
        if(s.contains("http://")){
            if(s.endsWith("/")){
                return s;
            }
            else{
                s=s.concat("/");
                return s;
            }
        } else if (s.equals("index.html")){
            s = "file:///android_asset/index.html";
            return s;
        }
        else{
            String k = "http://";
            k=k.concat(s);
            if(k.endsWith("/")){
                return k;
            }

            else{
                k=k.concat("/");
                return k;
            }

        }
    }

    public void refreshSite(View v){
        newPage = false;
        web.loadUrl(currentUrl);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void shoutOut(View v){
        web.evaluateJavascript("javascript:shoutOut()",null);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void initialize(View v){
        web.evaluateJavascript("javascript:initialize()",null);
    }



}
