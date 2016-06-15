/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import tools.CheckMethods;
import tools.json.JSONObject;

/**
 * 基本存储单元的实体类,用以暂时保存单次存储value信息.
 * @author maokelong
 */
public class EntitySaveUnit {

    int saveType;                   //存储类型,0为写信,1为收信
    String saveContentJSONString;   //存储内容

    /**
     * 存储单元构造方法
     *
     * @param saveType 存储类型,0为写信,1为收信
     * @param saveContentJSONString 存储内容(JSON)
     */
    public EntitySaveUnit(int saveType, String saveContentJSONString) {
        this.saveType = saveType;
        this.saveContentJSONString = saveContentJSONString;
    }

    public EntitySaveUnit(String jsonObjectString) {
        JSONObject jSONObject = new JSONObject(jsonObjectString);
        try {
            this.saveType = jSONObject.getInt("saveType");
            this.saveContentJSONString = jSONObject.getString("saveContentJSONString");
        } catch (Exception e) {
            CheckMethods.PrintErrorMessage("试图在存储单元实体中解析非存储单元json！","entity");
        }
    }

    public void setSaveContentJSONString(String saveContentJSONString) {
        this.saveContentJSONString = saveContentJSONString;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }

    public String getSaveContentJSONString() {
        return saveContentJSONString;
    }

    public int getSaveType() {
        return saveType;
    }

}
