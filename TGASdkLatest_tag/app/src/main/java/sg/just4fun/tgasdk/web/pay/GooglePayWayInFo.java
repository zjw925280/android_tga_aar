package sg.just4fun.tgasdk.web.pay;

import org.json.JSONObject;

import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;

public class GooglePayWayInFo implements TgaSdkJsonEntity {
    private String id;//商品id
    private String order;//支付单id
    private String appId;
    private String payType;
    private Boolean actived;//是否支持支付能力
    private Boolean enabled;//用sdk的支付还是用tga的支付
    private String change;//通知支付结果

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public boolean getActived() {
        return actived;
    }
    public void setActive(boolean actived) {
        this.actived = actived;
    }

    public GooglePayWayInFo() {
    }

    public GooglePayWayInFo(String  appId, boolean actived, boolean enabled) {
        this.appId = appId;
        this.actived = actived;//是否需要googlepay
        this.enabled = enabled;
    }
    public GooglePayWayInFo(String change) {
        this.change = change;
    }
    @Override
    public JSONObject toJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (appId!=null&&!appId.equals("")){
            jsonObject.put("appId", appId);
        }
        if (actived!=null){
            jsonObject.put("actived", actived);
        }
        if (enabled!=null){
            jsonObject.put("enabled", enabled);
        }
        if (change!=null&&!change.equals("")){
            jsonObject.put("change", change);
        }

        if (payType!=null&&!payType.equals("")){
            jsonObject.put("payType", payType);
        }
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {
        try{
            this.appId = jsonObject.getString("appId");
        } catch(Exception e) {
        }
        try{
            this.actived = jsonObject.getBoolean("actived");
        } catch(Exception e) {
        }   try{
            this.enabled = jsonObject.getBoolean("enabled");
        } catch(Exception e) {
        }
        try{
            this.change= jsonObject.getString("change");
        } catch(Exception e) {
        }
        try{
            this.payType = jsonObject.getString("payType");
        } catch(Exception e) {
        }


    }

}
