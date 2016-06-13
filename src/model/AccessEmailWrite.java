/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import tools.DBHelper;
import tools.json.JSONObject;

/**
 * 发件的接口类,用于同数据库进行IO.
 * @author maokelong
 */
public class AccessEmailWrite {

    /**
     * 保存发件实体
     *
     * @param entityEmailWritee
     */
    public void saveEmailWrite(EntityEmailWrite entityEmailWritee) {
        /* 制作存储单元 */
        EntitySaveUnit entitySaveUnit = new EntitySaveUnit(0,
                new JSONObject(entityEmailWritee).toString());
        /* 生成存储key */
        String keyString = String.valueOf(new Date().hashCode());
        /* 存储 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        dBHelper.writeToDatabase(keyString, new JSONObject(entitySaveUnit).toString(), true);
        dBHelper.closeDatabase();
    }

    /**
     * 获取所有发件
     */
    private ArrayList<EntityEmailWrite> getEmailWriteAll() {
        /* 声明返回数据集合 */
        ArrayList<EntityEmailWrite> ret = new ArrayList<>();
        /* 连接数据库 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        Map<String, String> map = dBHelper.getEveryItem();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            /* 制作存储单元 */
            EntitySaveUnit entitySaveUnit = new EntitySaveUnit(value);
            if (entitySaveUnit.getSaveType() == 0) {
                /*添加项*/
                ret.add(new EntityEmailWrite(entitySaveUnit.getSaveContentJSONString()));
            }
        }
        return ret;
    }
}
