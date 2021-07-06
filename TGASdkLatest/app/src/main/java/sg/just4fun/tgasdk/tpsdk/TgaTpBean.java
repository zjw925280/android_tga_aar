package sg.just4fun.tgasdk.tpsdk;

import android.app.Activity;
import android.webkit.WebView;

//import com.tencent.smtt.sdk.WebView;

import sg.just4fun.tgasdk.web.share.AppShareInFo;

/**
 * 广告以外的第三方平台apiBean集合。当前版本主要是分享功能
 */
public interface TgaTpBean {
    void initSdk();
    boolean isInitiated();
    String getTag();
    boolean doShare(Activity context, WebView webView, AppShareInFo sharInfo, String uuid);

}
