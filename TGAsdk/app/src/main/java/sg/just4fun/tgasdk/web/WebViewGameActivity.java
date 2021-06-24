package sg.just4fun.tgasdk.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.adsdk.TgaAdSdkUtils;
import sg.just4fun.tgasdk.callback.TGACallback;
import sg.just4fun.tgasdk.conctart.SdkActivityDele;
import sg.just4fun.tgasdk.tga.ui.home.model.TgaSdkUserInFo;
import sg.just4fun.tgasdk.tga.utils.SpUtils;
import sg.just4fun.tgasdk.tga.view.MyWebView;
import sg.just4fun.tgasdk.tpsdk.facebook.FacebookTpBean;
import sg.just4fun.tgasdk.web.goPage.GoPageUtils;
import sg.just4fun.tgasdk.web.pay.GoogleBillingUtil;
import sg.just4fun.tgasdk.web.share.ShareUtils;

public class WebViewGameActivity extends AppCompatActivity implements TGACallback.ShareCallback{
    private static String TGA="WebViewGameActivity";
    public static MyWebView add_view;
    private String url;
   private int isFrist=0;
    private MyWebView newWebView;
    private String lang1;
    private ImageView img_loading;
    public static RelativeLayout rl_loading;
    private int change;
    private FacebookTpBean facebook;
    private int gopag;

    public static String urlEncode(String text) {
        try{
            return URLEncoder.encode(text, "utf-8");
        } catch(Exception e) {
            return text;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_game);
        SdkActivityDele.addActivity(WebViewGameActivity.this);
        img_loading = findViewById(R.id.img_loading);
        rl_loading = findViewById(R.id.rl_loading);
        add_view = findViewById(R.id.add_view1);

        Intent intent = getIntent();
        url = intent.getStringExtra("url");
         gopag = intent.getIntExtra("gopag", -1);

        if (gopag==1){
            upWebview(gopag,"",add_view);
        }else {
            int change = SpUtils.getInt(this, "change", -1);
            if (change==1){
                String string = SpUtils.getString(this, "changelang", "");
                upWebview(gopag,string,add_view);
            }else {
                if (TgaSdk.listener!=null){
                    String lang = TgaSdk.listener.getLang();
                    if(lang == null || lang.trim().equals("")) {
                        String local = Locale.getDefault().toString();
                        lang1= Conctart.toStdLang(local);
                        upWebview(gopag,lang1,add_view);
                    } else {
                        //TODO: 需要开发标准化处理lang。这里暂时假定客户的APP给的已经是标准化的了
                        String s = Conctart.toStdLang(lang);
                        upWebview(gopag,s,add_view);
                    }
                }else {
                    String local = Locale.getDefault().toString();
                    lang1= Conctart.toStdLang(local);
                    upWebview(gopag,lang1,add_view);
                }
            }
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacebookTpBean.callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void upWebview(int pag,String lang, MyWebView webView) {
        Glide.with(WebViewGameActivity.this).load(R.mipmap.gif)
                .into(img_loading);
        rl_loading.setVisibility(View.VISIBLE);
        if(TgaSdk.listener!=null){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable(){
                        @Override
                        public void run() {
                            String info = TgaSdk.listener.getUserInfo();
                            SpUtils.putString(WebViewGameActivity.this,"userInfo",info);
                            TgaSdkUserInFo userInFo = new TgaSdkUserInFo();
                            try {
                                JSONObject jsonObject = new JSONObject(info);
                                userInFo.fromJson(jsonObject);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(pag==0){
                                url=url+"&lang="+lang;
                            }
                            Log.e(TGA,"lang="+lang+"   url="+url);
                            initWebView(add_view);
                            Log.e(TGA,"地址"+url);
                            webView.loadUrl(url);
                            webView.setWebViewClient(new WebViewClient() {
                                @Override
                                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                                    super.onPageStarted(view, url, favicon);
                                    rl_loading.setVisibility(View.GONE);
                                    Log.e("执行","onPageStarted="+url);
                                    if (!url.startsWith(url) && !url.contains("paypal")) {
//                    alertDialog.show();
//                    alertDialog.getWindow().setBackgroundDrawableResource(R.color.translucent_background);
//                    alertDialog.getWindow().setLayout(ScreenUtils.getScreenWidth(HomeActivity.this), ScreenUtils.getScreenHeight(HomeActivity.this));
                                    }
                                }
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                    Log.e("执行","shouldOverrideUrlLoading="+url);
                                    //                if (Uri.parse(url).getHost().equals("data.just4fun.sg")) {
                                    return false;
                                }

                                @Override
                                public void onPageFinished(WebView webView, String url) {
                                    super.onPageFinished(webView, url);
                                    Log.e("执行","onPageFinished="+url);
                                    Log.d("TGA_URL", "url fininshed : " + url + ", webview.orgurl=" + webView.getOriginalUrl() +", webview.url = " + webView.getUrl());
                                }
                            });

                            webView.setWebChromeClient(new WebChromeClient() {
                                                           @Override
                                                           public void onCloseWindow(WebView window) {
                                                               super.onCloseWindow(window);
                                                               if (newWebView != null) {


                                                               }
                                                           }

                                                           @Override
                                                           public boolean onCreateWindow(WebView view, boolean dialog, boolean userGesture, Message resultMsg) {

                                                               Log.e("webviewOpen","onCreateWindow=");
                                                               newWebView = new MyWebView(WebViewGameActivity.this);//新创建一个webview

                                                               initWebView(newWebView);//初始化webview

                                                               WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;//以下的操作应该就是让新的webview去加载对应的url等操作。

                                                               transport.setWebView(newWebView);

                                                               resultMsg.sendToTarget();

                                                               return true;
                                                           }
                                                       }
                            );

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {////h5谷歌调试
                                add_view.setWebContentsDebuggingEnabled(true);
                            }
                            JavaScriptinterface tgaBridge = new JavaScriptinterface(WebViewGameActivity.this, url);
                            tgaBridge.init(add_view); //初始化所有需要BRIDGE的SDK
                        }
                    });

                }
            }).start();

        }
    }


    private void initWebView(WebView webView ) {
        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(true);
        webSetting.setUseWideViewPort(true);
        webSetting.setSupportMultipleWindows(true);
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
        webSetting.setAppCachePath(getDir("appcache", 0).getPath());
        webSetting.setDatabasePath(getDir("databases", 0).getPath());
        webSetting.setGeolocationDatabasePath(getDir("geolocation", 0).getPath());
//        webSetting.en
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT) {
            add_view.setWebContentsDebuggingEnabled(true);
        }
    }





    @Override
    protected void onStart() {
        super.onStart();
        isFrist++;
        TGACallback.setLangCallback(new TGACallback.LangCallback() {
            @Override
            public void getLang(String lang) {
                if (!lang.equals("")){
                    change=1;
                    Log.e(TGA,"语言="+lang);
                    SpUtils.putInt(WebViewGameActivity.this,"change",change);
                    String s = Conctart.toStdLang(lang);
                    SpUtils.putString(WebViewGameActivity.this,"changelang",s);
                    upWebview(gopag,s,add_view);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TGACallback.setShareCallback(this);
        if (isFrist>1){
            if(this.add_view != null) { //每次唤醒都要重新注册当前webview到广告控件
                TgaAdSdkUtils.registerTgaWebview(this.add_view);
            }
            TgaAdSdkUtils.flushAllEvents(this.add_view);
            GoPageUtils.finishActivityEvents(add_view);

        }
    }


    @Override
    public void shareCall(String uuid, boolean success) {
        Log.e(TGA,"webvigame="+uuid+" 成功=="+success);
        ShareUtils.shareEvents(add_view,uuid,success);
    }
    //如果下个页面或者上个页面没有使用到googleBuillingUtil.getInstance()，那么就需要在finish或者startActivity之前调用cleanListener()方法，来清除接口。
//可以尝试这样
    @Override
    public void onBackPressed() {
        Log.e(TGA,"onBackPressed");
        GoogleBillingUtil.cleanListener();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       if(add_view!=null){
           add_view.stopLoading();
           add_view.removeAllViews();
           add_view.destroy();
           add_view = null;
       }
    }
}