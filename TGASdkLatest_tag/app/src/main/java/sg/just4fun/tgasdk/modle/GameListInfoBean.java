package sg.just4fun.tgasdk.modle;

import java.util.List;

public class GameListInfoBean {
//  "desc" :"SUCCESS",
//          "data" : [{
//        "gameId":"游戏ID",
//                "gameCategoryId":"游戏归属",
//                "name":"游戏名称",
//                "imageSrc":"游戏图标",
//                "playUrl":"游戏地址",
//                "backUrl":"游戏背景颜色",
//                "remark":"游戏说明",
//                "totalPlayer":"总游戏人数",
//                "gameType":"游戏类型",
//                "isVIP":"是否是VIP专属游戏",
//                "isNew":"是否是新游戏",
//                "isHot":"是否是热门游戏"
//    },{
//                "gameId":"游戏ID",
//                "gameCategoryId":"游戏归属",
//                "name":"游戏名称",
//                "imageSrc":"游戏图标",
//                "playUrl":"游戏地址",
//                "backUrl":"游戏背景颜色",
//                "remark":"游戏说明",
//                "totalPlayer":"总游戏人数",
//                "gameType":"游戏类型",
//                "isVIP":"是否是VIP专属游戏",
//                "isNew":"是否是新游戏",
//                "isHot":"是否是热门游戏"
//    },
//            ...
//            ],
//            "totalCount" : 所有符合该条件的数据条数,
//            "itemCount" : 当前拉取结果长度,


    private String desc;
    private List<GameinfoBean>data;
    private int totalCount;//" : 所有符合该条件的数据条数,
    private int itemCount;//" : 当前拉取结果长度,

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<GameinfoBean> getData() {
        return data;
    }

    public void setData(List<GameinfoBean> data) {
        this.data = data;
    }

    public class GameinfoBean{
        private String gameId;//":"游戏ID",
        private String gameCategoryId;//":"游戏归属",
        private String name;//":"游戏名称",
        private String imageSrc;//":"游戏图标",
        private String playUrl;//":"游戏地址",
        private String backUrl;//":"游戏背景颜色",
        private String remark;//":"游戏说明",
        private String totalPlayer;//":"总游戏人数",
        private String gameType;//":"游戏类型",
        private String isVIP;//":"是否是VIP专属游戏",
        private String isNew;//":"是否是新游戏",
        private String isHot;//":"是否是热门游戏"

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public String getGameCategoryId() {
            return gameCategoryId;
        }

        public void setGameCategoryId(String gameCategoryId) {
            this.gameCategoryId = gameCategoryId;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImageSrc() {
            return imageSrc;
        }

        public void setImageSrc(String imageSrc) {
            this.imageSrc = imageSrc;
        }

        public String getPlayUrl() {
            return playUrl;
        }

        public void setPlayUrl(String playUrl) {
            this.playUrl = playUrl;
        }

        public String getBackUrl() {
            return backUrl;
        }

        public void setBackUrl(String backUrl) {
            this.backUrl = backUrl;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getTotalPlayer() {
            return totalPlayer;
        }

        public void setTotalPlayer(String totalPlayer) {
            this.totalPlayer = totalPlayer;
        }

        public String getGameType() {
            return gameType;
        }

        public void setGameType(String gameType) {
            this.gameType = gameType;
        }

        public String getIsVIP() {
            return isVIP;
        }

        public void setIsVIP(String isVIP) {
            this.isVIP = isVIP;
        }

        public String getIsNew() {
            return isNew;
        }

        public void setIsNew(String isNew) {
            this.isNew = isNew;
        }

        public String getIsHot() {
            return isHot;
        }

        public void setIsHot(String isHot) {
            this.isHot = isHot;
        }
    }

}
