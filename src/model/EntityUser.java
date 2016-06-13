/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import tools.json.JSONObject;

/**
 *
 * @author maokelong
 */
public class EntityUser {
    
    private Boolean access;     //已保存
    private String hostAddr;    //POP3主机地址
    private String emailAddr;   //邮箱地址
    private String emailPsw;    //邮箱密码

    public EntityUser() {
        access = Boolean.FALSE;
        emailAddr = null;
        emailPsw = null;
    }
    
    public EntityUser(Boolean access, String hostAddr, String emailAddr, String emailPsw) {
        this.access = access;
        this.hostAddr = hostAddr;
        this.emailAddr = emailAddr;
        this.emailPsw = emailPsw;
    }
    
    public EntityUser(String jsonObjectString) {
        JSONObject jSONObject = new JSONObject(jsonObjectString);
        this.access = jSONObject.getBoolean("access");
        this.hostAddr = jSONObject.getString("hostAddr");
        this.emailAddr = jSONObject.getString("emailAddr");
        this.emailPsw = jSONObject.getString("emailPsw");
    }

    /**
     * 已保存.为true代表可以直接使用其中的emailAddr与emailPsw.
     *
     * @return
     */
    public Boolean getAccess() {
        return access;
    }
    
    public String getEmailAddr() {
        return emailAddr;
    }
    
    public String getEmailPsw() {
        return emailPsw;
    }
    
    public void setAccess(Boolean access) {
        this.access = access;
    }
    
    public void setEmailAddr(String emailAddr) {
        this.emailAddr = emailAddr;
    }
    
    public void setEmailPsw(String emailPsw) {
        this.emailPsw = emailPsw;
    }
    
    public void setHostAddr(String hostAddr) {
        this.hostAddr = hostAddr;
    }
    
    public String getHostAddr() {
        return hostAddr;
    }
    
}
