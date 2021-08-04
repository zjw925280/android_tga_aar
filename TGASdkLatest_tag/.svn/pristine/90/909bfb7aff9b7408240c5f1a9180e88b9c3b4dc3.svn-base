package sg.just4fun.tgasdk.modle;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;
import sg.just4fun.tgasdk.web.banner.AppLovinAdPlacementConfig;

public class UserInFoBean {
    public UserInFoBean() {

    }

    private String appId;
    private String packageName;
    private String appConfig;
    private String iconpath;
    private String appName;

    public String  getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getIconpath() {
        return iconpath;
    }

    public void setIconpath(String iconpath) {
        this.iconpath = iconpath;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(String appConfig) {
        this.appConfig = appConfig;
    }

    public class AppNameBean{
        private String lang;
        private String values;

        public String getLang() {
            return lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public String getValues() {
            return values;
        }

        public void setValues(String values) {
            this.values = values;
        }
    }



    public class AppConfig implements TgaSdkJsonEntity {
        private List<AdConfigBean> ad;
        private List<PayConfigBean> payMentList;
        private ShareBean share;
        private String gameCentreUrl;
        private String appId;


        public AppConfig(List<AdConfigBean> ad, List<PayConfigBean> payMentList, ShareBean share, String gameCentreUrl, String appId){
            this.ad=ad;
            this.payMentList=payMentList;
            this.share=share;
            this. gameCentreUrl=gameCentreUrl;
            this. appId=appId;
        }



        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public List<AdConfigBean> getAd() {
            return ad;
        }

        public void setAd(List<AdConfigBean> ad) {
            this.ad = ad;
        }

        public ShareBean getShare() {
            return share;
        }

        public void setShare(ShareBean share) {
            this.share = share;
        }

        public String getGameCentreUrl() {
            return gameCentreUrl;
        }

        public void setGameCentreUrl(String gameCentreUrl) {
            this.gameCentreUrl = gameCentreUrl;
        }

        public List<PayConfigBean> getPayMentList() {
            return payMentList;
        }

        public void setPayMentList(List<PayConfigBean> payMentList) {
            this.payMentList = payMentList;
        }


        @Override
        public JSONObject toJson() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ad", ad);
            jsonObject.put("payMentList", payMentList);
            jsonObject.put("share", share);
            jsonObject.put("appId", appId);
            jsonObject.put("gameCentreUrl", gameCentreUrl);
            return jsonObject;
        }

        @Override
        public void fromJson(JSONObject jsonObject) throws Exception {

        }
    }


    public class ShareBean {
        public boolean allEnabled;//总开关
        public List<ShareConfigBean> list;

        public ShareBean() {

        }

        public boolean isAllEnabled() {
            return allEnabled;
        }

        public void setAllEnabled(boolean allEnabled) {
            this.allEnabled = allEnabled;
        }

        public List<ShareConfigBean> getList() {
            return list;
        }

        public void setList(List<ShareConfigBean> list) {
            this.list = list;
        }

//        @Override
//        public JSONObject toJson() throws Exception {
//
//            return null;
//        }
//
//        @Override
//        public void fromJson(JSONObject jsonObject) throws Exception {
//
//        }
    }

    public class ShareConfigBean {
        public String code;
        public String title;
        public boolean enabled;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }


    public class AdConfigBean  {
        public AdConfigBean() {
        }

        public String moduleAppId;//模块自身appid

        private String channelName;//渠道名

        private double weight;//权重

        private boolean enabled;//模块开关 enabled  actived
        private AppLovinAdPlacementConfig config;

        public AppLovinAdPlacementConfig getConfig() {
            return config;
        }

        public void setConfig(AppLovinAdPlacementConfig config) {
            this.config = config;
        }

        public String getModuleAppId() {
            return moduleAppId;
        }

        public void setModuleAppId(String moduleAppId) {
            this.moduleAppId = moduleAppId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public boolean getEnabled() {
            return enabled;
        }

        public void setNeed(boolean enabled) {
            this.enabled = enabled;
        }

        public AdConfigBean(String channelName, double weight, boolean enabled, String moduleAppId) {
            this.channelName = channelName;
            this.weight = weight;
            this.enabled = enabled;
            this.moduleAppId = moduleAppId;
        }

//        @Override
//        public JSONObject toJson() throws Exception {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("channelName", channelName);
//            jsonObject.put("weight", weight);
//            jsonObject.put("enabled", enabled);
//            jsonObject.put("moduleAppId", moduleAppId);
//            return jsonObject;
//        }
//
//        @Override
//        public void fromJson(JSONObject jsonObject) throws Exception {
//
//        }
    }

    public class PayConfigBean {

        public String moduleAppId;//模块自身appid
        private String channelName;//渠道名
        private boolean enabled;//模块开关 enabled  actived
        private boolean actived;//是否支持支付能力

        public boolean getActived() {
            return actived;
        }

        public void setActived(boolean actived) {
            this.actived = actived;
        }

        public String getModuleAppId() {
            return moduleAppId;
        }

        public void setModuleAppId(String moduleAppId) {
            this.moduleAppId = moduleAppId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public PayConfigBean(String channelName, boolean actived, boolean enabled, String moduleAppId) {
            this.channelName = channelName;
            this.enabled = enabled;
            this.moduleAppId = moduleAppId;
            this.actived = actived;
        }
    }
}
