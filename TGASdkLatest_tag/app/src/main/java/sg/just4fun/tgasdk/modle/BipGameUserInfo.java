package sg.just4fun.tgasdk.modle;

public class BipGameUserInfo {
//    {
//        "stateCode":1, "resultInfo":{
//        "accessToken":"at-ff585ae346184e9e9d58b9bed20f49e2-tga",
//        "tokenType":"Bearer",
//         "user":{
//            "id":22256,
//            "appId":"47b1181148da4063b5ef17759dcaee25",
//            "txnId":"32",
//            "name":"",
//            "header":"",
//            "coin":0,
//            "energy":0,
//            "first":true
//        },
//        "desc":"SUCCESS",
//        "refreshToken":"rt-8ccbb8cf9c174362a7f010f3504a3642-tga"
//    }
//    }

    private String desc;



    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

        private String tokenType;//":"token类型",
        private String accessToken;//":"用户Token",
        private String refreshToken;//":"刷新Token",
        private BipGameUserUser user;

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public BipGameUserUser getUser() {
            return user;
        }

        public void setUser(BipGameUserUser user) {
            this.user = user;
        }

    public class BipGameUserUser {
        private int id;//":"用户的第三方ID",
        private String appId;//":"BIP的APPID",
        private String txnId;//":"BIP的唯一ID"
        private String header;//":"用户头像",
        private String name;//":"用户昵称",
        private String coin;//":用户金币数,
        private String energy;//":用户体力,
        private boolean first;//":true/false-是否首次登录,

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getTxnId() {
            return txnId;
        }

        public void setTxnId(String txnId) {
            this.txnId = txnId;
        }

        public String getHeader() {
            return header;
        }

        public void setHeader(String header) {
            this.header = header;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCoin() {
            return coin;
        }

        public void setCoin(String coin) {
            this.coin = coin;
        }

        public String getEnergy() {
            return energy;
        }

        public void setEnergy(String energy) {
            this.energy = energy;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }
    }
}
