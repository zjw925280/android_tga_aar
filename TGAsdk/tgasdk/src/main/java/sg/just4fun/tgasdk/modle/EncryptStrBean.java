package sg.just4fun.tgasdk.modle;

import org.json.JSONObject;

import sg.just4fun.tgasdk.adsdk.TgaAdSdkUtils;
import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;

public class EncryptStrBean implements TgaSdkJsonEntity {

    private String payId;
    private String price;
    private long payTime;
    private String wareId;

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("payId", payId);
        jsonObject.put("price",price);
        jsonObject.put("payTime",payTime);
        jsonObject.put("wareId",wareId);
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {

    }

    public EncryptStrBean() {
    }

    public EncryptStrBean(String payId, String price, long payTime, String wareId) {
        this.payId = payId;
        this.price = price;
        this.payTime = payTime;
        this.wareId = wareId;
    }

    public static class PriceBean  implements TgaSdkJsonEntity{
        private String cnt;
        private String ratio;
        private String  unit;

        public String getCnt() {
            return cnt;
        }

        public void setCnt(String cnt) {
            this.cnt = cnt;
        }

        public String getRatio() {
            return ratio;
        }

        public void setRatio(String ratio) {
            this.ratio = ratio;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        @Override
        public JSONObject toJson() throws Exception {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("cnt", cnt);
            jsonObject.put("ratio",ratio);
            jsonObject.put("unit",unit);
            return jsonObject;
        }

        @Override
        public void fromJson(JSONObject jsonObject) throws Exception {

        }

        public PriceBean() {
        }

        public PriceBean(String cnt, String ratio, String unit) {
            this.cnt = cnt;
            this.ratio = ratio;
            this.unit = unit;
        }
    }
}
