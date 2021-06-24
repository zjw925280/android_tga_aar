package sg.just4fun.tgasdk.web.share;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.tencent.smtt.sdk.WebView;

import sg.just4fun.tgasdk.adsdk.TgaSdkEventDataBean;


public class ShareUtils {


    public static String toJavaScriptCode(String error,String string )  {
        return "TgaSdk.onEvent("+error+", "+string+")";
    }

        public static void shareEvents(WebView webView, String uuid, boolean success) {
            ShareCallblackInfo shareInfo = new ShareCallblackInfo(success);
            TgaSdkEventDataBean tgaSdkEventDataBean = new TgaSdkEventDataBean(uuid, shareInfo);
            try {
                String scriptCode = toJavaScriptCode("null", tgaSdkEventDataBean.toJsCode());
                Log.e("回调","share="+scriptCode+"  run on="+webView);
                webView.post(new ScriptCodeRunnable(scriptCode, webView));
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    public static class ScriptCodeRunnable implements Runnable {
        private final String scriptCode;
        private final WebView webView;
        public ScriptCodeRunnable(String scriptCode, WebView webView) {
            this.scriptCode = scriptCode;
            this.webView = webView;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void run() {
            Log.d("执行了回调", "JSON="+scriptCode +" run on " +
                    webView.getUrl());
            webView.evaluateJavascript(scriptCode, null);
        }
    }

}