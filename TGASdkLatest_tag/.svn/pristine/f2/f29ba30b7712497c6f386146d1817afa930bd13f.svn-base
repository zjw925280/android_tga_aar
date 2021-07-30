package sg.just4fun.tgasdk.web.banner;

import org.json.JSONObject;

import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;

/**
 * config with AppLovin Ad placement Id set
 */
public class AppLovinAdPlacementConfig implements TgaSdkJsonEntity {
    private String titleAd;
    private String rewardAd;
    private String bannerAd;

    public String getInterstitial() {
        return titleAd;
    }

    public void setInterstitial(String interstitial) {
        this.titleAd = titleAd;
    }

    public String getReward() {
        return rewardAd;
    }

    public void setReward(String rewardAd) {
        this.rewardAd = rewardAd;
    }

    public String getBanner() {
        return bannerAd;
    }

    public void setBanner(String bannerAd) {
        this.bannerAd = bannerAd;
    }

    @Override
    public JSONObject toJson() throws Exception {

        JSONObject jsonObject = new JSONObject();
            jsonObject.put("titleAd", titleAd);
            jsonObject.put("rewardAd", rewardAd);
            jsonObject.put("bannerAd", bannerAd);
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        try{
            this.titleAd = jsonObject.getString("titleAd");
        } catch(Exception e) {
        }
        try{
            this.rewardAd = jsonObject.getString("rewardAd");
        } catch(Exception e) {
        }
        try{
            this.bannerAd = jsonObject.getString("bannerAd");
        } catch(Exception e) {
        }
    }
}
