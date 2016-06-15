/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import view.FrameProcessShow;

/**
 *
 * @author maokelong
 */
public class CheckMethods {

    private static FrameProcessShow frameProcessShow = new FrameProcessShow();

    public static void PrintDebugMessage(String messageString, String moduleNameString) {
        System.out.println("Debug::" + moduleNameString + "::" + messageString);
    }

    public static void PrintErrorMessage(String messageString, String moduleNameString) {
        System.out.println("Error::" + moduleNameString + "::" + messageString);
    }

    public static void ShowResponseByDiaglog(String str) {
        PrintResponse(str);
    }

    public static void ShowProcess(String str) {
        frameProcessShow.setVisible(Boolean.TRUE);
        frameProcessShow.disableQuitButton();
        frameProcessShow.addNewProgress(str);
        System.out.println(str);
    }

    public static void ShutdownProgress() {
        frameProcessShow.enableQuitButton();
    }

    public static void PrintResponse(String str) {
        System.out.println("从服务器接收消息 <--- " + str);
    }

    public static void PrintSeneding(String str) {
        System.out.println("向服务器发送消息 ---> " + str);
    }
}
