package sg.just4fun.tgasdk.web;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.facebook.ads.InterstitialAd;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.adsdk.TgaAdSdkUtils;
import sg.just4fun.tgasdk.adsdk.TgaApiBean;
import sg.just4fun.tgasdk.adsdk.applovin.ApplovinApiBean;
import sg.just4fun.tgasdk.callback.TGACallback;
import sg.just4fun.tgasdk.modle.EncryptStrBean;
import sg.just4fun.tgasdk.modle.GooglePayInfo;
import sg.just4fun.tgasdk.modle.GooglePayInfoBean;
import sg.just4fun.tgasdk.modle.PriceBean;
import sg.just4fun.tgasdk.tga.base.HttpBaseResult;
import sg.just4fun.tgasdk.tga.base.JsonCallback;
import sg.just4fun.tgasdk.tga.global.AppUrl;
import sg.just4fun.tgasdk.tga.ui.home.HomeActivity;
import sg.just4fun.tgasdk.tga.ui.home.model.TgaSdkUserInFo;
import sg.just4fun.tgasdk.tga.utils.SpUtils;
import sg.just4fun.tgasdk.tga.utils.ToastUtil;
import sg.just4fun.tgasdk.tpsdk.facebook.FacebookTpBean;
import sg.just4fun.tgasdk.web.banner.AdConfigUtlis;
import sg.just4fun.tgasdk.web.banner.AppLovinAdPlacementConfig;
import sg.just4fun.tgasdk.web.goPage.GoPageUtils;
import sg.just4fun.tgasdk.web.pay.GoogleBillingUtil;
import sg.just4fun.tgasdk.web.pay.GooglePayWayInFo;
import sg.just4fun.tgasdk.web.pay.GooglePayWayUtils;
import sg.just4fun.tgasdk.web.pay.ResultBean;
import sg.just4fun.tgasdk.web.share.AppShareInFo;

import static com.lzy.okgo.utils.HttpUtils.runOnUiThread;

public class JavaScriptinterface implements PurchasesUpdatedListener{
    // The Activity that load the webview with this interface instance.

    Activity context;
    private String TGA="JavaScriptinterface";
    private GoogleBillingUtil googleBillingUtil;
    private MyOnPurchaseFinishedListener mOnPurchaseFinishedListener = new MyOnPurchaseFinishedListener();//购买回调接口
    private MyOnQueryFinishedListener mOnQueryFinishedListener = new MyOnQueryFinishedListener();//查询回调接口
    private MyOnStartSetupFinishedListener mOnStartSetupFinishedListener = new MyOnStartSetupFinishedListener();//启动结果回调接口

    String tgaUrl;
//    TTAdNative ttAdNative = null;
    private final String FB_PLACEMENT_ID= "503651173800345_658237148341746";
    private WebView webview;
    InterstitialAd interstitialAd = null;
    private BillingClient billingClient = null;
    private String payUuid;
    private static GooglePayWayInFo googlePayWayInFo;
    private String orderId;
    private String price;
    private static List<GooglePayInfo> infoList=new ArrayList<>();
    private static String ggOrder;
    private int isscu=0;
    private int cishu=5;
    private String metaDataStringApplication1;
    private OrientationEventListener mOrientationListener;
    private String TAG="JavaScriptinterface";

    //    public static WebView webView;
    public JavaScriptinterface(Activity context, String tgaUrl){
        this.context= context;
//      this.webview = webview;
        this.tgaUrl = tgaUrl;

    }
    private int pageId;

    public void registerWebview(@NonNull WebView webview) {
        webview.addJavascriptInterface(this, "TgaAndroid");
        this.webview = webview;
        TgaAdSdkUtils.registerTgaWebview(webview);
    }

    FacebookTpBean facebook;
    TgaApiBean vungle;
    TgaApiBean apploving;

    public void init(@NonNull WebView webview) {
//        try{
//            vungle = new VungleApiBean(context, webview, tgaUrl);
//            vungle.initSdk();
//        } catch(Exception e){
//
//        }

        if (TgaSdk.appConfigbeanList==null){
            TgaSdk.getUserInfo(TgaSdk.appPaymentKey);
        }
        String metaDataStringApplication = Conctart.getMetaDataStringApplication(context,"com.facebook.sdk.ApplicationId", "");
         if (!metaDataStringApplication.equals("")){
             try{
                 facebook = new FacebookTpBean(context,tgaUrl);
                 facebook.initSdk();
             } catch(Exception e) {

             }
         }

          metaDataStringApplication1 = Conctart.getMetaDataStringApplication(context,"applovin.sdk.key", "");
        Log.d("tgasdk-js", "Apploving Init Begin");
        Log.d(TGA, "apploving="+metaDataStringApplication1);

        if (!metaDataStringApplication1.equals("")){
            AppLovinAdPlacementConfig appLovinAdPlacementConfig = new AppLovinAdPlacementConfig();
            try {
                if(TgaSdk.applovnIdConfig!=null){
                    JSONObject jsonObject = new JSONObject(TgaSdk.applovnIdConfig);
                    appLovinAdPlacementConfig.fromJson(jsonObject);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try{
                if(TgaSdk.applovnIdConfig!=null){
                    Log.e("apploving初始化","apploving初始化");
                    apploving = new ApplovinApiBean(context, webview, tgaUrl);
                    apploving.initSdk();
                }
            } catch(Exception e) {
                Log.e("tgasdk-js", "Apploving Init Error", e);

            }
        }

        registerWebview(webview);
    }


    public Map<String, String> waitingPayments = new HashMap<>();
    public Map<String, String> waitingPaymentsCallbacks = new HashMap<>();

    public void doGooglePlayPurchase(final String orderId, final String productId, final String callback) {
        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
        List<String> skuList = new ArrayList<>();
        skuList.add(productId);
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
        billingClient.querySkuDetailsAsync(params.build(), new SkuDetailsResponseListener() {
            @Override
            public void onSkuDetailsResponse(BillingResult billingResult, List<SkuDetails> list) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
                    for (SkuDetails skuDetails : list) {
                        String sku = skuDetails.getSku();
                        String price = skuDetails.getPrice();
                        if (productId.equals(sku)) {
                            BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                                    .setSkuDetails(skuDetails)
                                    .build();
                            BillingResult result = billingClient.launchBillingFlow((Activity) context, flowParams);
                            if(result.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                                waitingPayments.put(sku, orderId);
                                waitingPaymentsCallbacks.put(sku, callback);
                            }
                        }
                    }
                }
            }
        });
    }

    public void tryConnectToGooglePlayAndDoPurchase(final String orderId, final String productId, final String callback) {
        if(billingClient == null){
            final BillingClient nBillingClient = BillingClient.newBuilder(context).setListener(this).build();
            nBillingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                        billingClient = nBillingClient;
                        doGooglePlayPurchase(orderId, productId, callback);
                    }
                }
                @Override
                public void onBillingServiceDisconnected() {
                    if(nBillingClient == billingClient) {
                        billingClient = null;
                    }
                }
            });
        } else {
            doGooglePlayPurchase(orderId, productId, callback);
        }

    }

//    /**
//     * 与js交互时用到的方法，在js里直接调用的
//     */
//    @JavascriptInterface
//    public void getComming() {
//        Intent intent = new Intent(context,CommingActivity.class);
//        context.startActivity(intent);
//    }

    @JavascriptInterface
    public String getTgaSdkVersion(String type){
        return "0.1";
    }


    @JavascriptInterface
    public void facebookeLogin() {
        Log.d("FB","facebookeLogin");
        LoginManager.getInstance().logInWithReadPermissions(context, Arrays.asList("public_profile"));
    }
//
//    @JavascriptInterface
//    public void quitLogin(String uuid,Object s) {
//        Log.d("是不是","quitLogin");
//        if(TGASDK.listener != null) {
//            TGASDK.listener.quitLogin();
//        }
//        SpUtils.clean(TGASDK.mContext);
//        ActivityDele.finishAllActivity();
//        context.finish();
//    }


    @JavascriptInterface
    public void logout(String uuid,String options) {

        Log.d("是不是","logout");
        if(TgaSdk.listener != null) {
            TgaSdk.listener.quitLogin(context);
        }
        SpUtils.clean(TgaSdk.mContext);
        context.finish();
    }



    @JavascriptInterface
    public void appPayment(String orderId, String productId, String callback, String type) {
        Log.d( "INAPP PAY",  "orderId=" + orderId + ",productId=" + productId + "type=" + type);
        tryConnectToGooglePlayAndDoPurchase(orderId, productId, callback);
    }

//    @JavascriptInterface
//    public void showFullScreenAd(String userId) {
//        Log.i("INFO", "userId=" + userId);
//        TTAdSdk.getAdManager().requestPermissionIfNecessary(context);
//        if(ttAdNative == null) {
//            ttAdNative = TTAdSdk.getAdManager().createAdNative(context);
//        }
//        AdSlot adSlot = new AdSlot.Builder()
//                .setCodeId("945128590")
//                .setSupportDeepLink(true)
//                .setImageAcceptedSize(1080, 1920)
//                .setOrientation(TTAdConstant.VERTICAL)
//                .setUserID(userId)
//                .build();
//        TTAdNative mTTAdNative = ttAdNative;
//        mTTAdNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
//            @Override
//            public void onError(int i, String s) {
//                Log.e("ERROR", i+","+s);
//                Toast.makeText(context,  "FAILED TO LOAD AD:" + i, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
//                Toast.makeText(context,  "Loading...", Toast.LENGTH_SHORT).show();
//                ttFullScreenVideoAd.showFullScreenVideoAd((Activity) context);
//            }
//
//            @Override
//            public void onFullScreenVideoCached() {
//
//            }
//        });
//    }

    @JavascriptInterface
    public void showVungleAd(String uuid, String adType) {
        vungle.showAd(uuid, adType);
    }

    @JavascriptInterface
    public void showApplovinAd(String uuid, String adType) {
        Log.e("apploving初始化","showApplovinAd");
        Log.d(apploving.getTag(), "showApplovingAd(" + uuid + "," + adType + ")");
        Log.d("eZx4Pox", "showApplovingAd(" + uuid + "," + adType + ")");
        if(!metaDataStringApplication1.equals("")){
            Log.e("apploving初始化","metaDataStringApplication1");
            if (TgaSdk.applovnIdConfig!=null){
                apploving.showAd(uuid, adType);
                Log.e("apploving初始化","TgaSdk.applovnIdConfig");
            }
        }

    }
//    切换横屏
    @JavascriptInterface
    public void HorizontalScreen(String uuid, String options) {
        Log.e("HorizontalScreen","横屏options="+options);
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        runOnUiThread(new Runnable() {
            public void run() {
                WebViewGameActivity.full(false,context);
            }
        });
        WebViewGameActivity.tv_stuasbar.setVisibility(View.GONE);

    }

    //    切换竖屏
    @JavascriptInterface
    public void VerticalScreen(String uuid, String options) {
        Log.e("VerticalScreen","竖屏options="+options);
        context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //切换竖屏

    }

    @JavascriptInterface
    public void showApplovinBannerAd(String uuid, String params) {
        Log.e("apploving初始化","showApplovinBannerAd");
        Log.d(apploving.getTag(), "showApplovinBannerAd(" + uuid + "," + params + ")");
//        Log.d("eZx4Pox", "showApplovinBannerAd(" + uuid + "," + params + ")");
//        apploving.showBannerAd(uuid, "banner", params);
        if(!metaDataStringApplication1.equals("")){
            if (TgaSdk.applovnIdConfig!=null){
                apploving.showAd(uuid, "banner");
            }
        }
    }

    @JavascriptInterface
    public void hideApplovingBannerAd(String uuid, String options) {
        Log.e("hideApplovingBannerAd","广告"+options);
        if(!metaDataStringApplication1.equals("")){
            if (TgaSdk.applovnIdConfig!=null){
                apploving.hideBannerAd(uuid,"banner");
            }
        }
    }

    @JavascriptInterface
    public void showVungleBannerAd(String uuid, String params) {
        vungle.showBannerAd(uuid, "banner", params);
    }

    @JavascriptInterface
    public void hideVungleBannerAd(String uuid, String options) {
        vungle.hideBannerAd(uuid,"");

    }
    @JavascriptInterface
    public void openBrowser(String uuid, String options) {
        Uri uri = Uri.parse(options);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @JavascriptInterface
    public void goPage(String uuid,  String options) {
        pageId++;
        Log.e("goPage","goPage"+options+" uuid="+uuid+" pageId="+pageId);
        GoPageUtils.jumpGame(pageId,webview,context,uuid,options);
    }

    @JavascriptInterface
    public void finishPage(String uuid,  String options) {
        Log.e("goPage","关闭");
        GoogleBillingUtil.cleanListener();
        context.finish(); //返回键点击


    }
    @JavascriptInterface
    public void getPayAppid(String uuid,  String options) {
        Log.e("googlePayWay","getPayAppid"+options);
        GooglePayWayUtils.getAppIdEvents(webview,uuid);
    }
//sdk支付
    @JavascriptInterface
    public void sdkPay(String uuid,  String options) {
        Log.e("googlePayWay","options="+options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            this.payUuid=uuid;
            Gson gson = new Gson();
             googlePayWayInFo = gson.fromJson(options, GooglePayWayInFo.class);
            Log.e("googlePayWay","商品id="+googlePayWayInFo.getId());
             orderId = googlePayWayInFo.getOrder();
             Log.e("googlePayWay","orderId="+orderId);
            if (googlePayWayInFo.getPayType().equals("googlePay")){
                if(TgaSdk.infoList!=null&&TgaSdk.infoList.size()>0){
                    for (int a=0;a<TgaSdk.infoList.size();a++){
                        GooglePayInfo googlePayInfo = TgaSdk.infoList.get(a);
                        if (googlePayInfo.getWareId().equals(googlePayWayInFo.getId())){
                            Log.e("googlePayWay","googlePayInfo.getWareId()="+googlePayInfo.getWareId());
                            Log.e("googlePayWay","商品id"+googlePayWayInFo.getId());
                            googlePayWaypay(googlePayInfo.getThirdWareId());
                        }
                    }
                }else {
                    Log.e("googlePayWay","TgaSdk.infoList=null");
                    getGooglePayInfo(context,TgaSdk.appId);

                }
            }else {
                ToastUtil.showLongToastCenter("正在开发中.....");

            }

        }
    }
//    app自身支付
    @JavascriptInterface
    public void appPay(String uuid,  String options) {
        Log.e("appPay","options"+options);
    }

    //游客支付回调
    @JavascriptInterface
    public void payBacll(String uuid,  String options) {

    }
    @JavascriptInterface
    public void inAppShare(String uuid,  String options) {
        try {
            Objects.requireNonNull(TgaSdk.listener);
            Gson gson = new Gson();
            Log.e("分享","inAppShare="+options);
            AppShareInFo shareInFo = gson.fromJson(options, AppShareInFo.class);

            if (TgaSdk.listener!=null){
//            if (shareInFo.getType()==0){//分享
                TgaSdkUserInFo userInFo = new TgaSdkUserInFo();
                String userInfo = SpUtils.getString(context, "userInfo", "");
                Gson gson1 = new Gson();
                if (!userInfo.equals("")){
                    TgaSdkUserInFo shareInFo1 = gson1.fromJson(userInfo, TgaSdkUserInFo.class);
                    String link=shareInFo.getUrl()+"&txn-id="+shareInFo1.getUserId();
                    TgaSdk.listener.onInAppShare(context,uuid,shareInFo.getIcon(),link,shareInFo.getTitle(),"sdk");
                    JSONObject jsonObject = new JSONObject(userInfo);
                    userInFo.fromJson(jsonObject);
                }

//            }else if (shareInFo.getType()==1){//邀请游戏
//
//
//            }
            }
        } catch (Exception e) {
            e.printStackTrace();
            TgaSdk.shared(uuid, false); //TODO: 以后需要改成onEvent一个Error或者在shared中添加失败原因
        }

    }

    @JavascriptInterface
    public void thirdPartyShare(String uuid,  String options) {
        Log.e("第三方分享","facebook分享"+options);
        try {
            Objects.requireNonNull(TgaSdk.listener);
            Gson gson = new Gson();
            AppShareInFo shareInFo = gson.fromJson(options, AppShareInFo.class);

//            TgaSdk.listener.onInAppShare(context,uuid,shareInFo.getIcon(),shareInFo.getUrl(),shareInFo.getTitle());
            if(shareInFo.getCode().equals("facebook")){
                facebook.doShare(context,webview,shareInFo,uuid);
            }
        } catch (Exception e) {
            e.printStackTrace();
            TgaSdk.shared(uuid, false); //TODO: 以后需要改成onEvent一个Error或者在shared中添加失败原因
        }

    }
//获取广告配置表
    @JavascriptInterface
    public void getAppConfig(String uuid, String options) {
        Log.e("获取广告配置表","getBannerConfig");
        AdConfigUtlis.getBannerConfigEvents(webview,uuid);
    }

//    @JavascriptInterface
//    public void showFacebookAd(String uuid, String adType) {
//        facebook.showAd(uuid, adType);
//    }
    @JavascriptInterface
    public void goMyFragment(String uuid, String options) {
         TgaSdk.listener.goMyFragemnt(context);
    }


    @JavascriptInterface
    public String checkEvent(String uuid,String options) {
        return TgaAdSdkUtils.checkEvent(uuid);
    }

    @JavascriptInterface
    public void releaseEvent(String uuid,String options) {
        TgaAdSdkUtils.releaseEvent(uuid);
    }

    @JavascriptInterface
    public void releaseAllEvents(String uuid,String options) {
        TgaAdSdkUtils.clearCaches();
    }
    //调起app端登录界面
    @JavascriptInterface
    public void goLogin(String uuid, String options) {
        Log.e("去登陆","options="+options);
        if ( TgaSdk.gameCenterCallback!=null){
            TgaSdk.gameCenterCallback.openUserLogin(uuid);
        }
    }


    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> list) {
        Log.d("purchasesUpdated",billingResult.getDebugMessage());
        if(billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && list != null) {
            for(Purchase purchase:list){
                URLConnection conn = null;
                OutputStreamWriter osw = null;
                BufferedReader reader = null;
                try {
                    String productId = purchase.getOrderId();
                    String callback = waitingPayments.get(productId);
                    String orderId = waitingPayments.get(productId);
                    String token = purchase.getPurchaseToken();
                    String data = "productId=" + URLEncoder.encode(productId, "UTF-8")
                             + "&orderId=" + URLEncoder.encode(orderId, "UTF-8")
                            + "&purchaseToken=" + URLEncoder.encode(token, "UTF-8");
                    conn = new URL(callback).openConnection();
                    conn.setDoOutput(true);
                    osw = new OutputStreamWriter(conn.getOutputStream());
                    osw.write(data);
                    osw.flush();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while((line = reader.readLine()) != null){
                        sb.append(line + "\n");
                    }
                    String resText = sb.toString();
                    Log.d("Callback", resText);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void test() {
        this.apploving.showAd(UUID.randomUUID().toString().replace("-",""), "fullscreen");
    }

    @JavascriptInterface
    public void tryFlushEvents(String uuid, String options) {
        Log.d("javascriptInterface", "tryFlushEvents " + uuid + " " + options);
        TgaAdSdkUtils.flushAllEvents(webview);
    }


//        public void onEvent(String event, JSONObject params) {
//        webview.evaluateJavascript("tgasdk.nativeEvent(\"" +event + "\", " + params.toString(), null);
//    }
    public void googlePayWaypay(String id){
        Log.e("googlePayWay","进来了"+id);
          googleBillingUtil = GoogleBillingUtil.getInstance(id)
                .setOnPurchaseFinishedListener(mOnPurchaseFinishedListener)
                .setOnQueryFinishedListener(mOnQueryFinishedListener)
                .setOnStartSetupFinishedListener(mOnStartSetupFinishedListener)
                .build(context);
        googleBillingUtil.setIsAutoConsumeAsync(true);
    }

    //查询商品信息回调接口
    private class MyOnQueryFinishedListener implements GoogleBillingUtil.OnQueryFinishedListener
    {
        @Override
        public void onQuerySuccess(String skuType,List<SkuDetails> list) {
            //查询成功，返回商品列表，
            //skuDetails.getPrice()获得价格(文本)
            //skuDetails.getType()获得类型 sub或者inapp,因为sub和inapp的查询结果都走这里，所以需要判断。
            //googleBillingUtil.getSubsPositionBySku(skuDetails.getSku())获得当前subs sku的序号
            //googleBillingUtil.getInAppPositionBySku(skuDetails.getSku())获得当前inapp suk的序号
            if (list.size()>0){
                String price1 = list.get(0).getPrice();
                String number = getNumber(price1);
                String[] split = number.split("\\.");
                int i = split[1].length();
                String replace = number.replace("\\.", "");
                long pow = (long) Math.pow(10, i);
                double i1 =Double.parseDouble(replace)*pow;
                String replace2 = price1.replace(number, "");
                try {
                    Log.e("googlePayWay","list.size()>0="+list.get(0).toString());
                   PriceBean priceBean = new PriceBean(String.valueOf(i1) , String.valueOf(pow), replace2);
                    Log.e("googlePayWay","得到值price1="+list.get(0).toString());
                    price = priceBean.toJson().toString();
                } catch (Exception e) {
                    Log.e("googlePayWay","访问服务端="+e.getMessage());
                    e.printStackTrace();
                }
                googleBillingUtil.purchaseInApp(context,list.get(0));
            }else {
                GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-2");
                EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
                try {
                    Log.e("googlePayWay","list.size()<=0==="+list.get(0).toString());
                    String encryptStr1 = encryptStrBean.toJson().toString();
                    String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                    googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);
                } catch (Exception e) {
                    Log.e("googlePayWay","访问服务端="+e.getMessage());
                    e.printStackTrace();
                }
            }

        }
        @Override
        public void onQueryFail(int responseCode) {
            isscu=0;
            //查询失败
            Log.e("googlePayWay","查询失败="+responseCode);
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-2");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                Log.e("googlePayWay","查询失败");
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);
            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }
        }
        @Override
        public void onQueryError() {
            //查询错误
            isscu=0;
            Log.e("googlePayWay","查询错误");
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-2");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                Log.e("googlePayWay","onQueryError");
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);

            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }
        }
    }
    //服务初始化结果回调接口
    private class MyOnStartSetupFinishedListener implements GoogleBillingUtil.OnStartSetupFinishedListener
    {
        @Override
        public void onSetupSuccess() {
            Log.e("googlePayWay","初始化成功");
        }

        @Override
        public void onSetupFail(int responseCode) {
            isscu=0;
            Log.e("googlePayWay","初始化失败="+responseCode);
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-4");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);
            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }

        }

        @Override
        public void onSetupError() {
            isscu=0;
            Log.e("googlePayWay","初始化错误");
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-4");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);

            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }

        }
//...;
    }
    //购买商品回调接口
    private class MyOnPurchaseFinishedListener implements GoogleBillingUtil.OnPurchaseFinishedListener
    {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onPurchaseSuccess(List<Purchase> list) {
            isscu=1;
            //内购或者订阅成功,可以通过purchase.getSku()获取suk进而来判断是哪个商品
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"1");
             ggOrder = list.get(0).getOrderId();
            EncryptStrBean encryptStrBean = new EncryptStrBean(JavaScriptinterface.this.orderId,price,list.get(0).getPurchaseTime(),googlePayWayInFo.getId());
            try {
                String encryptStr1 = encryptStrBean.toJson().toString();
                Log.e("googlePayWay","try=encryptStr1"+encryptStr1+TgaSdk.appPaymentKey);
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                Log.e("googlePayWay","try=encryptStr"+encryptStr);
                googlePayResult(TgaSdk.appId,context,list.get(0).getOrderId(),encryptStr,"inapp",TgaSdk.appId,1);
            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onPurchaseFail(int responseCode) {
            isscu=0;
            Log.e("googlePayWay","购买失败码="+responseCode);
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-1");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);
            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }
        }

        @Override
        public void onPurchaseError() {
            isscu=0;
            Log.e("googlePayWay","购买错误码=");
            GooglePayWayUtils.googlePayWayEvents(webview,payUuid,"-1");
            EncryptStrBean encryptStrBean = new EncryptStrBean(orderId,price,0,googlePayWayInFo.getId());
            try {
                String encryptStr1 = encryptStrBean.toJson().toString();
                String encryptStr = DesEncryptUtils.encrypt(encryptStr1, TgaSdk.appPaymentKey);
                googlePayResult(TgaSdk.appId,context,"",encryptStr,"inapp",TgaSdk.appId,0);
            } catch (Exception e) {
                Log.e("googlePayWay","访问服务端="+e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private void googlePayResult(String appId, Context mContext, String orderId, String encryptStr, String payType, String callbackKey, int state) {
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try {
            jsonObject.put("appId", appId);
            jsonObject.put("orderId", orderId);
            jsonObject.put("encryptStr", encryptStr);
            jsonObject.put("payType", payType);
            jsonObject.put("callbackKey", callbackKey);
            jsonObject.put("state", state);
            data = jsonObject.toString();
            Log.e("googlePayWay","进来了data="+data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        OkGo.<HttpBaseResult<ResultBean>>post(AppUrl.GET_GOOGLEPAY_RESULT)
                .tag(mContext)
                .upRequestBody(body)
                .execute(new JsonCallback<HttpBaseResult<ResultBean>>(context) {
                    @Override
                    public void onSuccess(Response<HttpBaseResult<ResultBean>> response) {
                   Log.e("googlePayWay","通知成功"+response.body().getResultInfo());



                    }
                    @Override
                    public void onError(Response<HttpBaseResult<ResultBean>> response) {
                        Log.e("googlePayWay","googlePay/PayFinish通知失败"+response.getException().toString());
                        cishu--;
                        if (isscu==1){
                            if (response.body().getStateCode()!=1){
                                if (cishu>0){
                                try {
                                    Thread.sleep(2000);
                                    googlePayResult(TgaSdk.appId,context,ggOrder,encryptStr,"inapp",TgaSdk.appId,1);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                }else {
                                    ToastUtil.showShortToastCenter(mContext.getResources().getString(R.string.payerror));
                                }
                            }
                        }
                    }
                });
    }

    public static String getNumber(String str){
        // 控制正则表达式的匹配行为的参数(小数)
        Pattern p = Pattern.compile("(\\d+\\.\\d+)");
        //Matcher类的构造方法也是私有的,不能随意创建,只能通过Pattern.matcher(CharSequence input)方法得到该类的实例.
        Matcher m = p.matcher(str);
        //m.find用来判断该字符串中是否含有与"(\\d+\\.\\d+)"相匹配的子串
        if (m.find()) {
            //如果有相匹配的,则判断是否为null操作
            //group()中的参数：0表示匹配整个正则，1表示匹配第一个括号的正则,2表示匹配第二个正则,在这只有一个括号,即1和0是一样的
            str = m.group(1) == null ? "" : m.group(1);
        } else {
            //如果匹配不到小数，就进行整数匹配
            p = Pattern.compile("(\\d+)");
            m = p.matcher(str);
            if (m.find()) {
                //如果有整数相匹配
                str = m.group(1) == null ? "" : m.group(1);
            } else {
                //如果没有小数和整数相匹配,即字符串中没有整数和小数，就设为空
                str = "";
            }
        }
        return str;
    }
    private void getGooglePayInfo(Context context, String appId) {
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try {
            jsonObject.put("appId", appId);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        Log.e("初始化","body="+body.toString());
        OkGo.<HttpBaseResult<GooglePayInfoBean>>post(AppUrl.GET_GOOGLEPAY_INFO)
                .tag(context)
                .upRequestBody(body)
                .execute(new JsonCallback<HttpBaseResult<GooglePayInfoBean>>(context) {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(Response<HttpBaseResult<GooglePayInfoBean>> response) {
                        if (response.body().getStateCode() == 1) {
                            infoList = response.body().getResultInfo().getData();
                            if (infoList.size()==0){
                                Log.e("googlePayWay","googlepay配置表商品为0");
                                return;
                            }
                            for (int a=0;a<infoList.size();a++){
                               GooglePayInfo googlePayInfo = TgaSdk.infoList.get(a);
                                if (googlePayInfo.getWareId().equals(googlePayWayInFo.getId())){
                                    googlePayWaypay(googlePayInfo.getThirdWareId());
                                }
                            }
                        }
                    }
                    @Override
                    public void onError(Response<HttpBaseResult<GooglePayInfoBean>> response) {
                        Log.e("googlePayWay","获取配置表商品失败"+response.getException().toString());

                    }
                });

    }



}
