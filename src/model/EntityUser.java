/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import tools.CheckMethods;
import tools.json.JSONObject;

/**
 * 用户的实体类,用以暂时保存单个用户的信息.
 *
 * @author maokelong
 */
public class EntityUser {

    private Boolean access;     //已保存
    private String hostAddrSMTP;//SMTP主机地址
    private String hostAddrPOP3;//POP3主机地址
    private String emailAddr;   //邮箱地址
    private String emailPsw;    //邮箱密码

    public EntityUser() {
        access = Boolean.FALSE;
        emailAddr = null;
        emailPsw = null;
    }

    public EntityUser(Boolean access, String hostAddrSMTP, String hostAddrPOP3, String emailAddr, String emailPsw) {
        this.access = access;
        this.hostAddrSMTP = hostAddrSMTP;
        this.hostAddrPOP3 = hostAddrPOP3;
        this.emailAddr = emailAddr;
        this.emailPsw = emailPsw;
    }

    public EntityUser(String jsonObjectString) {
        JSONObject jSONObject = new JSONObject(jsonObjectString);
        try {
            this.access = jSONObject.getBoolean("access");
            this.hostAddrSMTP = jSONObject.getString("hostAddrSMTP");
            this.hostAddrPOP3 = jSONObject.getString("hostAddrPOP3");
            this.emailAddr = jSONObject.getString("emailAddr");
            this.emailPsw = jSONObject.getString("emailPsw");
        } catch (Exception e) {
            CheckMethods.PrintErrorMessage("试图在用户实体中解析非用户json！", "entity");
        }
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

    public String getHostAddrPOP3() {
        return hostAddrPOP3;
    }

    public String getHostAddrSMTP() {
        return hostAddrSMTP;
    }

    public void setHostAddrPOP3(String hostAddrPOP3) {
        this.hostAddrPOP3 = hostAddrPOP3;
    }

    public void setHostAddrSMTP(String hostAddrSMTP) {
        this.hostAddrSMTP = hostAddrSMTP;
    }

}
