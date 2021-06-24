package sg.just4fun.tgasdk.adsdk;

import sg.just4fun.tgasdk.web.share.AppShareInFo;

/**
 *
 * EveryApiRequire a bean to keep live
 *
 */
public interface TgaApiBean {

    void initSdk();
    void showAd(final String uuid, String adTypeName);
    void showBannerAd(final String uuid, String adTypeName, String paramsJson);
    void hideBannerAd(final String uuid);
    boolean isInitiated();
    String getTag();

}
