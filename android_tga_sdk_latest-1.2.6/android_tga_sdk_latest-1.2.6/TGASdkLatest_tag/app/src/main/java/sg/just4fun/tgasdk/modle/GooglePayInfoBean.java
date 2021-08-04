package sg.just4fun.tgasdk.modle;

import java.util.List;

public class GooglePayInfoBean {
    private String desc;
    public List<GooglePayInfo> data;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<GooglePayInfo> getData() {
        return data;
    }

    public void setData(List<GooglePayInfo> data) {
        this.data = data;
    }

    public class GooglePayInfo {

        public String wareId;//":"商品ID",
        public String thirdWareId;//":"谷歌支付的商品ID"

        public String getWareId() {
            return wareId;
        }

        public void setWareId(String wareId) {
            this.wareId = wareId;
        }

        public String getThirdWareId() {
            return thirdWareId;
        }

        public void setThirdWareId(String thirdWareId) {
            this.thirdWareId = thirdWareId;
        }
    }

}
