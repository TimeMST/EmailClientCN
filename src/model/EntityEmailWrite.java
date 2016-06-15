/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import tools.CheckMethods;
import tools.json.JSONObject;

/**
 * 写信的实体类,用以暂时保存单个发件的信息.
 * @author maokelong
 */
public class EntityEmailWrite {

    private String senderAddr;  //发件人地址
    private String receiverAddr;//收件人地址
    private String mailTitle;   //邮件标题
    private String mailContent; //邮件正文

    public EntityEmailWrite() {
        this.senderAddr = "";
        this.receiverAddr = "";
        this.mailTitle = "";
        this.mailContent = "";
    }

    public EntityEmailWrite(String senderAddr, String receiverAddr, String mailTitle, String mailContent) {
        this.senderAddr = senderAddr;
        this.receiverAddr = receiverAddr;
        this.mailTitle = mailTitle;
        this.mailContent = mailContent;
    }

    public EntityEmailWrite(String jsonObjectString) {
        JSONObject jSONObject = new JSONObject(jsonObjectString);
        try {
            this.senderAddr = jSONObject.getString("senderAddr");
            this.receiverAddr = jSONObject.getString("receiverAddr");
            this.mailTitle = jSONObject.getString("mailTitle");
            this.mailContent = jSONObject.getString("mailContent");
        } catch (Exception e) {
            CheckMethods.PrintErrorMessage("试图在发件实体中解析非发件json！","entity");
        }
    }

    public void setMailContent(String mailContent) {
        this.mailContent = mailContent;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setReceiverAddr(String receiverAddr) {
        this.receiverAddr = receiverAddr;
    }

    public void setSenderAddr(String senderAddr) {
        this.senderAddr = senderAddr;
    }

    public String getMailContent() {
        return mailContent;
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getReceiverAddr() {
        return receiverAddr;
    }

    public String getSenderAddr() {
        return senderAddr;
    }

}
