package sg.just4fun.tgasdk.web;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.Call;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.callback.TGACallback;
import sg.just4fun.tgasdk.conctart.Conctant;
import sg.just4fun.tgasdk.modle.GooglePayInfoBean;
import sg.just4fun.tgasdk.modle.UserInFoBean;
import sg.just4fun.tgasdk.tga.base.HttpBaseResult;
import sg.just4fun.tgasdk.tga.base.JsonCallback;
import sg.just4fun.tgasdk.tga.global.AppUrl;
import sg.just4fun.tgasdk.tga.global.Global;
import sg.just4fun.tgasdk.tga.ui.home.model.TgaSdkUserInFo;

public class TgaSdk {
    public static Context mContext;
    public static  TGACallback.TgaEventListener listener;
    public static List<UserInFoBean.AdConfigBean> appConfigbeanList=new ArrayList<UserInFoBean.AdConfigBean>();
    public static String appKey;
    public static String appId;
    public static String appPaymentKey;
    public static List<GooglePayInfoBean.GooglePayInfo> infoList=new ArrayList<>();
    public static  String appConfigList;

    public static TGACallback.initCallback initCallback;
    public static String applovnIdConfig;
    public static String gameCentreUrl;
    private static String TGA="TgaSdk";
    private static String sdkPkName;
    private static int isSuccess=0;
    public static String iconpath;
    public static String packageName;
    public static String schemeUrl;

    private TgaSdk() {

    }
    public static String urlEncode(String text) {
        try{
            return URLEncoder.encode(text, "utf-8");
        } catch(Exception e) {
            return text;
        }
    }


    public static void init(Context context,String appKey,String schemeUrl,String appPaymentKey,TGACallback.TgaEventListener listener,TGACallback.initCallback initCallback) {
        mContext = context.getApplicationContext();
        TgaSdk.appKey= appKey;
        TgaSdk.schemeUrl= schemeUrl;
        TgaSdk.listener = listener;
        Log.e(TGA,"listener是不是空了="+listener);
        TgaSdk.initCallback=initCallback;
        Log.e(TGA,"linitCallback是不是空了="+initCallback);
        TgaSdk.appPaymentKey = appPaymentKey;
        Log.e(TGA,"appPaymentKey是不是空了="+appPaymentKey);
//       获取用户配置表
        getUserInfo(appKey);
    }


    private static void getGooglePayInfo(String appId) {
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
        OkGo.<HttpBaseResult<GooglePayInfoBean>>post(AppUrl.GET_GOOGLEPAY_INFO)
                .tag(mContext)
                .upRequestBody(body)
                .execute(new JsonCallback<HttpBaseResult<GooglePayInfoBean>>() {
                    @Override
                    public void onSuccess(Response<HttpBaseResult<GooglePayInfoBean>> response) {
                        if (response.body().getStateCode() == 1) {
                           infoList = response.body().getResultInfo().getData();
                           Log.e(TGA,"google支付配置="+infoList);
                        }
                    }
                    @Override
                    public void onError(Response<HttpBaseResult<GooglePayInfoBean>> response) {
                     Log.e(TGA,"google支付配置失败="+response.message());
                    }
                });

    }

    public static Context getContext() {
        return mContext;
    }

    public static void goPage(Context context,final String url,boolean autoToken,String schemeQuery) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (url==null||url.equals("")){
                    String version = Conctant.getVersion(mContext);
                    if (isSuccess==1){
                        Log.e(TGA,"isSuccess==1");
                        if (TgaSdk.listener!=null){
                            Log.e(TGA,"TgaSdk.listener不为空");
                            String url="";
                            String userInfo = TgaSdk.listener.getUserInfo();
                            if(userInfo==null||userInfo.equals("")){
                                Log.e(TGA,"用户信息为空");
                                url= TgaSdk.gameCentreUrl+"?appId="+ TgaSdk.appId;//无底部
                                Intent intent = new Intent(context, WebViewGameActivity.class);
                                intent.putExtra("url",url);
                                intent.putExtra("gopag",0);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else {
                                Log.e(TGA,"用户信息不为空");
                                Gson gson = new Gson();
                                TgaSdkUserInFo userInFo = gson.fromJson(userInfo, TgaSdkUserInFo.class);
                                Log.e(TGA,"游戏中心列表="+TgaSdk.gameCentreUrl);
                                if (TgaSdk.gameCentreUrl==null||TgaSdk.gameCentreUrl.equals("")){
                                    TgaSdk.gameCentreUrl= Global.TEST_MOREN;
                                }
                                if (schemeQuery!=null&&!schemeQuery.equals("")){
                                    url= TgaSdk.gameCentreUrl+ "?txnid="+ userInFo.getUserId()+"&"+schemeQuery+"&appId="+ TgaSdk.appId+"&nickname="+urlEncode(userInFo.getNickname())+"&msisdn="+userInFo.getUserId()+"&appversion="+version+"&head="+urlEncode(userInFo.getAvatar());//无底部
                                }else {

                                    url= TgaSdk.gameCentreUrl+ "?txnid="+ userInFo.getUserId()+"&appId="+ TgaSdk.appId+"&nickname="+urlEncode(userInFo.getNickname())+"&msisdn="+userInFo.getUserId()+"&appversion="+version+"&head="+urlEncode(userInFo.getAvatar());//无底部
                                }
                                Intent intent = new Intent(context, WebViewGameActivity.class);
                                intent.putExtra("url",url);
                                intent.putExtra("gopag",0);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                            return;
                      }else {
                            Log.e(TGA,"TgaSdk.listener=null");
                            initCallback.initError(mContext.getResources().getString(R.string.sdkiniterror));
                            return;
                        }
                    }else {
                        Log.e(TGA,"初始化.isSuccess=0");
                        initCallback.initError(mContext.getResources().getString(R.string.sdkiniterror));
                        return;
                    }
                }
                Intent intent = new Intent(context, WebViewGameActivity.class);
                intent.putExtra("url",url);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }).start();

    }
    public static String buildUserInfo(String userId, String nickName, String headImg) {
     TgaSdkUserInFo  userInFo = new TgaSdkUserInFo(userId, nickName, headImg);
        try {
            return userInFo.toJson().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public static String getSchemeQuery (String query) {
        TgaSdkUserInFo  userInFo = new TgaSdkUserInFo(query);
        try {
            return userInFo.toJson().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



    //跳转游戏中心
    public static void goPage(Context context,String url,String gameid) {
       goPage(context, url, true,gameid);
    }
    //跳转游戏中心
    public static void goPage(Context context) {
        goPage(context, "",true,"");
    }

    //跳转游戏中心
    public static void goLink(Context context,String url) {
        goPage(context, url,true,"");
    }

    public static void shareSuccess(String uuid) {
        shared(uuid, true);
    }

    public static void shareError(String uuid) {
        shared(uuid, false);
    }

    public static void shared(String uuid, boolean success) {
        TGACallback.listener.shareCall(uuid, success);
    }

    public static void lang(String lang) {
        TGACallback.langListener.getLang(lang);
    }

    public static void shared(String uuid, int successCount) {
        shared(uuid, successCount > 0);
    }

    public static String requireNotBlankString(String value) {
        if(value == null || value.trim().equals("")) {
            return null;
        }
        return value;
    }

    public static void getUserInfo(String appKe){
        JSONObject jsonObject = new JSONObject();
        String data = "{}";
        try {
            jsonObject.put("appSdkKey", appKe);
            data = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, data);
        PostRequest post = OkGo.post(AppUrl.TGA_SDK_INFO);
        post.tag(mContext);
        post.upRequestBody(body);
        post.execute(new JsonCallback<String>() {
            @Override
            public void onSuccess(Response response) {
                String s1 = response.body().toString();
//                s1="{\"stateCode\":1,\"resultInfo\":{\"appConfig\":\"{\\\"ad\\\":[{\\\"moduleAppId\\\":\\\"1\\\",\\\"channelName\\\":\\\"applovin\\\",\\\"weight\\\":1,\\\"enabled\\\":true,\\\"config\\\":{\\\"interstitial\\\":\\\"25ddb36d401152a9\\\",\\\"titleAd\\\":\\\"365315c423c6a029\\\",\\\"rewardAd\\\":\\\"f200862f970382ea\\\",\\\"bannerAd\\\":\\\"97cc5a65435d808f\\\"}}]}\",\"appName\":null,\"appId\":\"3d2c434a87c94b1ebe3765c4f924c52c\",\"packageName\":\"com.aleta.pacpay\",\"iconpath\":null,\"desc\":\"SUCCESS\"}}";
                Log.e(TGA,"初始化成功的="+s1);
                Gson gson = new GsonBuilder()
                        .serializeNulls()
                        .create();
                HttpBaseResult httpBaseResult = gson.fromJson(s1, HttpBaseResult.class);
                Log.e(TGA,"初始化成功的="+httpBaseResult.getStateCode());
                Log.e(TGA,"resultInfo内容="+gson.toJson(httpBaseResult.getResultInfo()));
                try{
                    if (httpBaseResult.getStateCode() == 1) {
                        Log.e(TGA,"初始化成功的=");
                        if (listener!=null){
                            Log.e(TGA,"listener是不是空了="+gson.toJson(httpBaseResult.getResultInfo()));
//                            String resultInfo1 = gson.toJson(response.body().getResultInfo()) ;
                            String s = gson.toJson(httpBaseResult.getResultInfo());
                            UserInFoBean resultInfo = gson.fromJson(s, UserInFoBean.class);
                            Log.e(TGA,"listener是不是空了resultInfo1"+gson.toJson(httpBaseResult.getResultInfo()));
//                            JSONObject jsonObject1 = new JSONObject(resultInfo1);
                            String pkName = mContext.getPackageName();
//                            if (jsonObject1.has("packageName")){
//                                packageName = (String)jsonObject1.get("packageName");
//                            }
                             packageName = resultInfo.getPackageName();
                            if (packageName !=null&&!packageName.equals("")){
                                if (packageName.equals(pkName)){//包名相等
                                    isSuccess=1;
                                    initCallback.initSucceed();
//                                    appId = (String)jsonObject1.get("appId");
                                     appId = resultInfo.getAppId();
//                                    if (jsonObject1.has("iconpath")){
//                                        iconpath = (String)jsonObject1.get("iconpath");
//                                    }
                                     iconpath = resultInfo.getIconpath();
//                                    if (jsonObject1.has("appConfig")){
//                                        appConfigList = (String)jsonObject1.get("appConfig");
//                                    }
                                    appConfigList = resultInfo.getAppConfig();
                                    if(appConfigList!=null&&!appConfigList.equals("")&&!appConfigList.equals("{}")){
                                        UserInFoBean.AppConfig adConfigBean = gson.fromJson(appConfigList, UserInFoBean.AppConfig.class);
                                        try{
                                            gameCentreUrl = Objects.requireNonNull(requireNotBlankString(adConfigBean.getGameCentreUrl()));
                                        }catch (Exception e){
                                            gameCentreUrl= Global.TEST_MOREN;
                                        }
                                        if (adConfigBean!=null){
                                            List<UserInFoBean.AdConfigBean> adList =  adConfigBean.getAd();
                                            if(adList == null || adList.isEmpty()) {
                                                applovnIdConfig = null;
                                            } else {
                                                Log.e("tgasdk", "ad配置表==" + adList.size());
                                                try{
                                                    applovnIdConfig = adList.get(0).getConfig().toJson().toString();
                                                } catch (Exception e2) {
                                                    applovnIdConfig = null;
                                                }
                                                Log.e("tgasdk", "ad配置表==" + applovnIdConfig);
                                            }
                                        }


                                    }else {
                                        gameCentreUrl= Global.TEST_MOREN;
                                    }
                                    getGooglePayInfo(TgaSdk.appId);
                                    Log.e("tgasdk", "ad配置表==" + applovnIdConfig);
                                }else {
                                    Log.e(TGA,"包名不一致=");
                                    isSuccess=0;
                                    initCallback.initError(mContext.getResources().getString(R.string.packagename));
                                }
                            }else {
                                Log.e(TGA,"包名不一致=");
                                isSuccess=0;
                                initCallback.initError(mContext.getResources().getString(R.string.packagename));
                            }

                        }else {
                            isSuccess=0;
                            initCallback.initError("TgaEventListener接口为空");
                        }

                    } else {
                        isSuccess=0;
                        initCallback.initError("服务端errorCode=" + httpBaseResult.getStateCode());
                    }
                } catch(Exception e) {
                    isSuccess = 0;
                    initCallback.initError("errorCode=" + -5);
                    Log.e("tgasdk", "initiate failed", e);
                }
            }

            @Override
            public void onError(Response response) {
                isSuccess=0;
                initCallback.initError("errorCode=" + -5);
                Log.e("初始化", "充值失败=" + response.getException().getMessage());
            }
        });
    }



   public static void fromScheme(Uri schemeUri){
        if (schemeUri!=null||!schemeUri.equals("")){
            try{
                String query = schemeUri.getQuery();
                goPage(mContext, "",true,query);
            }catch (Exception e){
                initCallback.initError("schemeUri存在异常");
            }
        }else {
            goPage(mContext);
        }
   }

}
