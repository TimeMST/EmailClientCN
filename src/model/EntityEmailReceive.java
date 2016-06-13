/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import tools.CheckMethods;
import tools.json.JSONObject;

/**
 * 收件的实体类,用以暂时保存单封收件的信息.
 * @author maokelong
 */
public class EntityEmailReceive {

    private String messageIDString;     //Message-ID
    private String senderNameString;    //发件人的姓名  
    private String senderAddrString;    //发件人的地址
    private String receiverAddrString;  //收件人的地址
    private String mailTitleString;     //邮件主题  
    private String mailDevDatesString;  //邮件发送日期 
    private String mailContentsString;  //邮件正文内容
    private int mailType;               //邮件分类,0为普通邮件,1为重要邮件
    private boolean alreadyDownload;    //已经完全下载

    public EntityEmailReceive() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd HH:mm");
        this.messageIDString = "";
        this.senderNameString = "";
        this.senderAddrString = "";
        this.receiverAddrString = "";
        this.mailTitleString = "";
        this.mailDevDatesString = simpleDateFormat.format(new Date());
        this.mailContentsString = "";
        this.mailType = 0;
        this.alreadyDownload = Boolean.FALSE;
    }

    public EntityEmailReceive(String messageIDString, String senderNameString, String senderAddrString, String receiverAddrString, String mailTitleString, String mailDevDatesString, String mailContentsString, int mailType, boolean alreadyDownload) {
        this.messageIDString = messageIDString;
        this.senderNameString = senderNameString;
        this.senderAddrString = senderAddrString;
        this.receiverAddrString = receiverAddrString;
        this.mailTitleString = mailTitleString;
        this.mailDevDatesString = mailDevDatesString;
        this.mailContentsString = mailContentsString;
        this.mailType = mailType;
        this.alreadyDownload = alreadyDownload;
    }

    public EntityEmailReceive(String jsonObjectString) {
        JSONObject jSONObject = new JSONObject(jsonObjectString);
        try {
            this.messageIDString = jSONObject.getString("messageIDString");
            this.senderNameString = jSONObject.getString("senderNameString");
            this.senderAddrString = jSONObject.getString("senderAddrString");
            this.receiverAddrString = jSONObject.getString("receiverAddrString");
            this.mailTitleString = jSONObject.getString("mailTitleString");
            this.mailDevDatesString = jSONObject.getString("mailDevDatesString");
            this.mailContentsString = jSONObject.getString("mailContentsString");
            this.mailType = jSONObject.getInt("mailType");
            this.alreadyDownload = jSONObject.getBoolean("alreadyDownload");
        } catch (Exception e) {
            CheckMethods.PrintErrorMessage("试图在收件实体中解析非收件json！");
        }
    }

    public String getMailContentsString() {
        return mailContentsString;
    }

    public String getMailDevDatesString() {
        return mailDevDatesString;
    }

    public String getMailTitleString() {
        return mailTitleString;
    }

    public int getMailType() {
        return mailType;
    }

    public String getMessageIDString() {
        return messageIDString;
    }

    public String getReceiverAddrString() {
        return receiverAddrString;
    }

    public String getSenderAddrString() {
        return senderAddrString;
    }

    public String getSenderNameString() {
        return senderNameString;
    }

    public void setAlreadyDownload(boolean alreadyDownload) {
        this.alreadyDownload = alreadyDownload;
    }

    public void setMailContentsString(String mailContentsString) {
        this.mailContentsString = mailContentsString;
    }

    public void setMailDevDatesString(String mailDevDatesString) {
        this.mailDevDatesString = mailDevDatesString;
    }

    public void setMailTitleString(String mailTitleString) {
        this.mailTitleString = mailTitleString;
    }

    public void setMailType(int mailType) {
        this.mailType = mailType;
    }

    public void setMessageIDString(String messageIDString) {
        this.messageIDString = messageIDString;
    }

    public void setReceiverAddrString(String receiverAddrString) {
        this.receiverAddrString = receiverAddrString;
    }

    public void setSenderAddrString(String senderAddrString) {
        this.senderAddrString = senderAddrString;
    }

    public void setSenderNameString(String senderNameString) {
        this.senderNameString = senderNameString;
    }

}
