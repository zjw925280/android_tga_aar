package sg.just4fun.tgasdk.web.share;

import org.json.JSONObject;

import sg.just4fun.tgasdk.adsdk.TgaSdkJsonEntity;

/**
 *
 * 备注：
 * 内嵌的h5页面调用bridge时传的options即该类格式的json字符串
 * id 初始化时为 null， 取到对应的uuid时赋值
 *
 *
 */
public class AppShareInFo implements TgaSdkJsonEntity {
    public AppShareInFo() {
    }
    private String code;
    private String id;
    private String icon;
    private String title;
    private String url;
    private String hidebar;
    private int type;
    private int number;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getHidebar() {
        return hidebar;
    }

    public void setHidebar(String hidebar) {
        this.hidebar = hidebar;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public AppShareInFo(String id, String icon, String title, String url) {
        this.id = id;
        this.icon = icon;
        this.title = title;
        this.url = url;
    }

    @Override
    public JSONObject toJson() throws Exception {
        JSONObject jsonObject = new JSONObject();
        if (id!=null){

            jsonObject.put("id", this.id);
        }
        jsonObject.put("icon", this.icon);
        jsonObject.put("title", this.title);
        jsonObject.put("url", this.url);
        jsonObject.put("url", this.url);
        jsonObject.put("hidebar", this.hidebar);

        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject) throws Exception {

        try{
            this.id = jsonObject.getString("id");
        } catch(Exception e) {
        }
        try{
            this.icon = jsonObject.getString("icon");
        } catch(Exception e) {
        }
        try{
            this.title = jsonObject.getString("title");
        } catch(Exception e) {
        }
        try{
            this.url = jsonObject.getString("url");
        } catch(Exception e) {
        }
        try{
            this.type = jsonObject.getInt("type");
        } catch(Exception e) {
        }
        try{
            this.hidebar = jsonObject.getString("hidebar");
        } catch(Exception e) {
        }

    }
}
