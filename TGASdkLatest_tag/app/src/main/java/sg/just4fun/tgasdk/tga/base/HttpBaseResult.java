package sg.just4fun.tgasdk.tga.base;

import java.util.Map;

public class HttpBaseResult <T> {

    private int stateCode;
    private Map<String,Object> resultInfo;

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }


    public Map<String,Object> getResultInfo() {
        return resultInfo;
    }

    public void setResultInfo( Map<String,Object> resultInfo) {
        this.resultInfo = resultInfo;
    }

    @Override
    public String toString() {
        return "HttpBaseResult{" +
                "stateCode=" + stateCode +
                ", resultInfo=" + resultInfo +
                '}';
    }
}