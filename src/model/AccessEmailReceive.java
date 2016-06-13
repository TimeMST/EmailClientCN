/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import tools.DBHelper;
import tools.StringHelper;
import tools.json.JSONObject;

/**
 * 收件的接口类,用于同数据库进行IO.
 *
 * @author maokelong
 */
public class AccessEmailReceive {

    /**
     * 保存收件实体（覆盖式）
     *
     * @param entityEmailReceive
     */
    public void saveEmailReceive(EntityEmailReceive entityEmailReceive) {
        /* 制作存储单元 */
        EntitySaveUnit entitySaveUnit = new EntitySaveUnit(1,
                new JSONObject(entityEmailReceive).toString());
        /* 获取存储key */
        String keyString = entityEmailReceive.getMessageIDString();
        /* 存储 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        dBHelper.writeToDatabase(keyString, new JSONObject(entitySaveUnit).toString(), true);
        dBHelper.closeDatabase();
    }

    /**
     * 获取所有收件
     */
    private ArrayList<EntityEmailReceive> getEmailReceiveAll() {
        /* 声明返回数据集合 */
        ArrayList<EntityEmailReceive> ret = new ArrayList<>();
        /* 连接数据库 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        Map<String, String> map = dBHelper.getEveryItem();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            /* 制作存储单元 */
            EntitySaveUnit entitySaveUnit = new EntitySaveUnit(value);
            if (entitySaveUnit.getSaveType() == 1) {
                /*添加项*/
                ret.add(new EntityEmailReceive(entitySaveUnit.getSaveContentJSONString()));
            }
        }
        return ret;
    }

    /**
     * 根据收件的ID获取该收件
     *
     * @param messageIDString
     * @return 收件实体（查找失败返回null）
     */
    private EntityEmailReceive getEmailReceiveByID(String messageIDString) {
        EntityEmailReceive entityEmailReceive;
        /* 连接数据库 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        try {
            String value = dBHelper.readFromDatabase(messageIDString);
            EntitySaveUnit entitySaveUnit = new EntitySaveUnit(value);
            entityEmailReceive = new EntityEmailReceive(entitySaveUnit.getSaveContentJSONString());
        } catch (Exception e) {
            entityEmailReceive = null;
        }
        return entityEmailReceive;
    }

}
