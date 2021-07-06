package sg.just4fun.tgasdk.tpsdk.facebook;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.webkit.WebView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
//import com.tencent.smtt.sdk.WebView;

import sg.just4fun.tgasdk.adsdk.TgaAdSdkUtils;
import sg.just4fun.tgasdk.tpsdk.TgaTpBean;
import sg.just4fun.tgasdk.web.share.AppShareInFo;
import sg.just4fun.tgasdk.web.share.ShareUtils;

/**
 *
 * 设计逻辑如下：
 * 每一个webviewActivity都有自己可调用的各个模块的tpBean实例
 *
 *
 *
 */
public class FacebookTpBean implements TgaTpBean {

    public static final String TAG = "facebook";
    private final Activity context;
    private  String url;
    public static CallbackManager callbackManager;
    private ShareDialog shareDialog;
    private boolean initiated = false;

    private TgaTpBean coveredBean;
    private WebView webView;
    private String uuid;

    public FacebookTpBean(Activity context,String url) {
        this.context = context;
        this.url = url;

    }
    public FacebookTpBean(Activity context) {
        this.context = context;

    }
    @Override
    public void initSdk() {

//        if(TgaSdk.getBean(TAG) != null) {
//            coveredBean = TgaSdk.getBean(TAG);
//            return;
//        }
        //全局初始化后，本bean作为全局唯一的真实Bean。其他的bean都通过coveredBean调用
        FacebookSdk.sdkInitialize(context);
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.e("facebook分享","注册成功");
                try{
                    webView.loadUrl(TgaAdSdkUtils.appendParameterToUrl(url, "fbtoken", loginResult.getAccessToken().getToken()));
                }catch (Exception e) {
                    Log.e("TGA_URL", e.getMessage());
                }
            }

            @Override
            public void onCancel() {
                Log.e("facebook分享","注册取消");
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("facebook分享","注册失败");
            }
        });

        this.initiated = true;
        shareDialog = new ShareDialog(context);
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.e("facebook分享","成功");
                ShareUtils.shareEvents(webView,uuid,true);

            }
            @Override
            public void onCancel() {
                Log.e("facebook分享","取消");
                ShareUtils.shareEvents(webView,uuid,false);
            }

            @Override
            public void onError(FacebookException error) {
                ShareUtils.shareEvents(webView,uuid,false);
                Log.e("facebook分享","失败");
            }
        });

    }

    @Override
    public boolean isInitiated() {
        return initiated;
    }

    @Override
    public String getTag() {
        return TAG;
    }


    @Override
    public boolean doShare(Activity context, WebView webView, AppShareInFo sharInfo, String uuid) {
        this.webView=webView;
        this.uuid=uuid;
        Log.e("分享","地址=="+sharInfo.getUrl());
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(sharInfo.getUrl()))
                .build();
        shareDialog.show(content);
        return false;
    }


}
