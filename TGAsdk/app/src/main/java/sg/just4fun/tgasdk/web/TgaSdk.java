package sg.just4fun.tgasdk.web;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tencent.smtt.sdk.QbSdk;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import sg.just4fun.tgasdk.R;
import sg.just4fun.tgasdk.callback.TGACallback;
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
        TgaSdk.initCallback=initCallback;
        TgaSdk.appPaymentKey = appPaymentKey;
//       获取用户配置表
        getUserInfo(appKey);
    }
     public static void initSDKWeb(Context context){
         Handler  handler=new Handler();
         Runnable runnable = new Runnable() {
             @Override
             public void run() {
                 initTBS(context);
             }
         };
         handler.postDelayed(runnable,0);
     }


    public static void initTBS(Context context) {

//        add_view.getCrashExtraMessage(this);
        //视频为了避免闪屏和透明问题
//        mContext.getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //避免输入法界面弹出后遮挡输入光标的问题
//        mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        //非wifi情况下，主动下载x5内核
        QbSdk.setDownloadWithoutWifi(true);
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.i(TGA,"腾讯 X 5 初始化:" + arg0);
                Log.i(TGA,arg0 == true ? "腾讯 X5 初始化成功！" : "腾讯 X5 初始化失败！");
                if(arg0==false){
                    initTBS(context);
                }
            }
            @Override
            public void onCoreInitFinished() {

            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(context.getApplicationContext(), cb);

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
                           Log.e("崩了","崩了="+infoList);
                        }
                    }
                    @Override
                    public void onError(Response<HttpBaseResult<GooglePayInfoBean>> response) {
                     Log.e("初始化","失败="+response.message());
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
                    if (isSuccess==1){
                        if (TgaSdk.listener!=null){
                            String url="";
                            String userInfo = TgaSdk.listener.getUserInfo();
                            if(userInfo==null||userInfo.equals("")){
                                url= TgaSdk.gameCentreUrl+"?appId="+ TgaSdk.appId;//无底部
                                Intent intent = new Intent(context, WebViewGameActivity.class);
                                intent.putExtra("url",url);
                                intent.putExtra("gopag",0);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }else {
                                Gson gson = new Gson();
                                TgaSdkUserInFo userInFo = gson.fromJson(userInfo, TgaSdkUserInFo.class);
                                if (TgaSdk.gameCentreUrl!=null&&!TgaSdk.gameCentreUrl.equals("")){
                                    if (schemeQuery!=null&&!schemeQuery.equals("")){
                                        url= TgaSdk.gameCentreUrl+ "?txnid="+ userInFo.getUserId()+"&nickname="+urlEncode(userInFo.getNickname())+"&"+schemeQuery+"&head="+urlEncode(userInFo.getAvatar())+"&appId="+ TgaSdk.appId;//无底部
                                    }else {

                                        url= TgaSdk.gameCentreUrl+ "?txnid="+ userInFo.getUserId()+"&nickname="+urlEncode(userInFo.getNickname())+"&head="+urlEncode(userInFo.getAvatar())+"&appId="+ TgaSdk.appId;//无底部
                                    }
                                    Intent intent = new Intent(context, WebViewGameActivity.class);
                                    intent.putExtra("url",url);
                                    intent.putExtra("gopag",0);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    context.startActivity(intent);
                                }else {
                                    initCallback.initError(mContext.getResources().getString(R.string.sdkerror));
                                }
                            }

                            return;
                      }else {
                            initCallback.initError(mContext.getResources().getString(R.string.sdkerror));
                            return;
                        }
                    }else {
                        initCallback.initError(mContext.getResources().getString(R.string.sdkerror));
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
    public static void goGameCenter(Context context,String schemeQuery) {
        goPage(context, "",true,schemeQuery);
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
        PostRequest<HttpBaseResult<UserInFoBean>> post = OkGo.<HttpBaseResult<UserInFoBean>>post(AppUrl.TGA_SDK_INFO);
        post.tag(mContext);
        post.upRequestBody(body);
        post.execute(new JsonCallback<HttpBaseResult<UserInFoBean>>() {
            @Override
            public void onSuccess(Response<HttpBaseResult<UserInFoBean>> response) {
                try{
                    if (response.body().getStateCode() == 1) {
                        if (listener!=null){
                            String pkName = mContext.getPackageName();
                            sdkPkName = response.body().getResultInfo().getPackageName();
                            if (sdkPkName!=null&&!sdkPkName.equals("")){
                                if (sdkPkName.equals(pkName)){//包名相等
                                    isSuccess=1;
                                    initCallback.initSucceed();
                                     appId = response.body().getResultInfo().getAppId();
                                     iconpath = response.body().getResultInfo().getIconpath();
                                     packageName = response.body().getResultInfo().getPackageName();
                                    appConfigList = response.body().getResultInfo().getAppConfig();
                                    Log.e("初始化", "配置表==" +appConfigList);
                                    if(appConfigList!=null&&!appConfigList.equals("")){
                                        Gson gson = new Gson();
                                        UserInFoBean.AppConfig adConfigBean = gson.fromJson(appConfigList, UserInFoBean.AppConfig.class);
                                        try{
                                            gameCentreUrl = Objects.requireNonNull(requireNotBlankString(adConfigBean.getGameCentreUrl()));
                                        }catch (Exception e){
                                            gameCentreUrl= Global.YUMING + "/h5tga";
                                        }
                                        List<UserInFoBean.AdConfigBean> adList = adConfigBean == null ? null : adConfigBean.getAd();

                                        if(adList == null || adList.isEmpty()) {
                                            applovnIdConfig = null;
                                            Toast.makeText(mContext,"广告模块没有配置",Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.e("tgasdk", "ad配置表==" + adList.size());
                                            try{
                                                applovnIdConfig = adList.get(0).getConfig().toJson().toString();
                                            } catch (Exception e2) {
                                                applovnIdConfig = null;
                                            }
                                            Log.e("tgasdk", "ad配置表==" + applovnIdConfig);
                                        }
                                    }else {
                                        gameCentreUrl= Global.YUMING + "/h5tga";
                                    }

                                    getGooglePayInfo(appId);
                                    Log.e("tgasdk", "ad配置表==" + applovnIdConfig);
                                }else {
                                    isSuccess=0;
                                    initCallback.initError(mContext.getResources().getString(R.string.packagename));
                                }
                            }else {
                                isSuccess=0;
                                initCallback.initError(mContext.getResources().getString(R.string.packagename));
                            }

                        }

                    } else {
                        isSuccess=0;
                        initCallback.initError("errorCode=" + response.body().getStateCode());
                    }
                } catch(Exception e) {
                    isSuccess = 0;
                    initCallback.initError("errorCode=" + -5);
                    Log.e("tgasdk", "initiate failed", e);
                }
            }

            @Override
            public void onError(Response<HttpBaseResult<UserInFoBean>> response) {
                isSuccess=0;
                Log.e("初始化", "充值失败=" + response.getException().getMessage());
                initCallback.initError(response.getException().getMessage());
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
