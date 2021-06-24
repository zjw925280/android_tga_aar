package sg.just4fun.tgasdk.web.share;

import org.json.JSONObject;

import sg.just4fun.tgasdk.adsdk.TgaAdSdkUtils;
import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;

public class ShareCallblackInfo implements TgaSdkJsonEntity {

    private String method = "onShared";
    private boolean success;

    public ShareCallblackInfo(boolean success) {
        this.success = success;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", this.success);
        jsonObject.put("method", this.method);
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {

    }
}
