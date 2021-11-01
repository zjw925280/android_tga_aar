package sg.just4fun.tgasdk.callback;


import android.content.Context;

import sg.just4fun.tgasdk.modle.UserInFoBean;

public class TGACallback {
    public static ShareCallback listener;
    public static LangCallback langListener;
    public static FightGameCallback fightGameListener;

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

}
