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
     * 保存收件实体（不覆盖式）
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
        dBHelper.writeToDatabase(keyString, new JSONObject(entitySaveUnit).toString(), false);
        dBHelper.closeDatabase();
    }

    /**
     * 保存收件实体（可选覆盖式）
     *
     * @param entityEmailReceive
     */
    public void saveEmailReceive(EntityEmailReceive entityEmailReceive, boolean overWrite) {
        /* 制作存储单元 */
        EntitySaveUnit entitySaveUnit = new EntitySaveUnit(1,
                new JSONObject(entityEmailReceive).toString());
        /* 获取存储key */
        String keyString = entityEmailReceive.getMessageIDString();
        /* 存储 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        dBHelper.writeToDatabase(keyString, new JSONObject(entitySaveUnit).toString(), overWrite);
        dBHelper.closeDatabase();
    }

    /**
     * 获取所有不重要/重要的邮件
     *
     * @param isImportant 的确是重要的
     * @return 邮件
     */
    public ArrayList<EntityEmailReceive> getEmailReceiveAll(Boolean isImportant) {
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
                EntityEmailReceive emailReceive = new EntityEmailReceive(entitySaveUnit.getSaveContentJSONString());
                if (isImportant && emailReceive.getMailType() == 1) {
                    ret.add(emailReceive);
                } else if (isImportant) {

                } else {
                    ret.add(emailReceive);
                }
            }
        }
        return ret;
    }

    /**
     * 获取所有收件
     */
    public ArrayList<EntityEmailReceive> getEmailReceiveAll() {
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
    public EntityEmailReceive getEmailReceiveByID(String messageIDString) {
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

    /**
     * 
     * @param messageIDString
     * @return 
     */
    public Boolean deleteEmailFromDBByMessageID(String messageIDString) {
        Boolean sucBoolean;
        /* 连接数据库 */
        DBHelper dBHelper = new DBHelper();
        dBHelper.openDatabase();
        try {
            sucBoolean = dBHelper.deleteFromDatabase(messageIDString);
        } catch (Exception e) {
            sucBoolean = Boolean.FALSE;
        }
        return sucBoolean;
    }

}
