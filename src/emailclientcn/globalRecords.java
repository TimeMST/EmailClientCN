/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package emailclientcn;

import model.EntityUser;

/**
 * 记载一些全局量
 * @author maokelong
 */
public class globalRecords {

    private static EntityUser entityUser;

    public static EntityUser getEntityUser() {
        return entityUser;
    }

    public static void setEntityUser(EntityUser entityUser) {
        globalRecords.entityUser = entityUser;
    }

}
