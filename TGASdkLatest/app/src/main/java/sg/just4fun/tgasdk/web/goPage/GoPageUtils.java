package sg.just4fun.tgasdk.web.goPage;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
//import com.tencent.smtt.sdk.WebView;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import sg.just4fun.tgasdk.tga.ui.home.model.TgaSdkUserInFo;
import sg.just4fun.tgasdk.web.TgaSdk;
import sg.just4fun.tgasdk.web.WebViewGameActivity;


public class GoPageUtils {
    private static String uuid;
    private static WebView tgawebView;
    private static Map<Integer, GoPageInfo> caches = new ConcurrentHashMap<>();
    private static Activity context;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void  jumpGame(int page,WebView tgawebView,Activity context, String uuid, String url) {
        GoPageUtils.tgawebView=tgawebView;
        GoPageUtils.uuid=uuid;
        GoPageUtils.context=context;
        String userInfo = TgaSdk.listener.getUserInfo();
        Gson gson = new Gson();
        TgaSdkUserInFo tgaSdkUserInFo = gson.fromJson(userInfo, TgaSdkUserInFo.class);
//        url=url+"&txnid="+tgaSdkUserInFo.getId()+"&appId="+ TgaSdk.appId;
        Log.e("链接","链接="+url);
        Intent intent = new Intent(context, WebViewGameActivity.class);
        intent.putExtra("url",url);
        intent.putExtra("uuid",uuid);
        intent.putExtra("gopag",1);
        context.startActivity(intent);
            try {
                GoPageInfo goPageInfo1 = new GoPageInfo(uuid, "36");
                String s = goPageInfo1.toJson().toString();
                String cache = toJavaScriptCode("null",s);
                LinkedList<String> singleBean = new LinkedList<>();
                singleBean.add(cache);
                GoPageInfo goPageInfo = new GoPageInfo(uuid, singleBean);
                caches.put(page,goPageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    public static String toJavaScriptCode(String error,String string )  {
        return "TgaSdk.onEvent("+error+", "+string+")";
    }

    public static void finishActivityEvents(WebView webView) {
        for (int page : caches.keySet()) {
            try {
                GoPageInfo goPageInfo = caches.remove(page);
                LinkedList<String> info = goPageInfo.getInfo();
                    if (goPageInfo.getUuid().equals(uuid)){
                        for (String scriptCode : info) {
                            Log.e("执行了回调", "执行了回调"+scriptCode);
                            webView.post( new ScriptCodeRunnable(scriptCode, webView));
                        }
                    }
            } catch (Exception e) {

            }
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
