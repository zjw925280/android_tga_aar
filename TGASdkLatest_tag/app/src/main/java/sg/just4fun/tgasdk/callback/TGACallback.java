package sg.just4fun.tgasdk.callback;


import android.content.Context;

import sg.just4fun.tgasdk.modle.UserInFoBean;

public class TGACallback {
    public static ShareCallback listener;
    public static LangCallback langListener;
    public static FightGameCallback fightGameListener;
    public static CloseCallback closeListener;


    public interface initCallback {
        void initSucceed();

        void initError(String error);
    }

    public interface TgaEventListener {
        default void quitLogin(Context context) {

        }

        String getAuthCode();

        void onInAppShare(Context context, String uuid, String iconUrl, String link, String title, String type);
//      void onInAppPay(Context context);

        default void goMyFragemnt(Context context) {

        }

        String getLang();
        void onPageClosed();

    }


    public interface LangCallback {
        void getLang(String lang);
    }

    public static void setLangCallback(LangCallback langListener) {
        TGACallback.langListener = langListener;
    }


    public interface ShareCallback {
        void shareCall(String uuid, boolean success);
    }

    public static void setShareCallback(ShareCallback listener) {
        TGACallback.listener = listener;
    }


    public interface FightGameCallback {
        void fightGameCall();
    }

    public static void setFightGameCallback(FightGameCallback fightGameListener) {
        TGACallback.fightGameListener = fightGameListener;
    }
    public interface CloseCallback {
        void closeCall();
    }

    public static void setCloseCallback(CloseCallback closeListener) {
        TGACallback.closeListener = closeListener;
    }
}
