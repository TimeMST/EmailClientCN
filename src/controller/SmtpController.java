package controller;

import emailclientcn.globalRecords;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import model.EntityEmailWrite;
import model.EntityUser;

import tools.MyBase64;
import tools.CheckMethods;

public class SmtpController {

    final int SMTP_PORT = 25;
    private EntityUser entityUser;
    Socket smtp;
    BufferedReader smtp_in;
    PrintWriter smpt_out;

//    public static void main(String[] args) {
//        try {
//            SmtpController smtpController = new SmtpController();
//            EntityEmailWrite emailWrite1 = new EntityEmailWrite();
//            emailWrite1.setMailContent("hellosdfaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//            emailWrite1.setSenderAddr("maokelong95@163.com");
//            emailWrite1.setReceiverAddr("974985526@qq.com");
//            emailWrite1.setMailTitle("title");
//            smtpController.Send(emailWrite1);
//        } catch (Exception e) {
//            CheckMethods.PrintDebugMessage(e.getMessage());
//        }
//    }
    public SmtpController() throws Exception {
        super();
        entityUser = globalRecords.getEntityUser();
//        entityUser = new EntityUser();
//        entityUser.setEmailAddr("maokelong95@163.com");
//        entityUser.setEmailPsw("test123");
//        entityUser.setHostAddrSMTP("smtp.163.com");
    }

    /**
     * 执行认证到服务器
     *
     * @throws java.lang.Exception
     */
    public void loginSmtpServer() throws Exception {
        //建立一个到邮件服务器的连接，端口号25
        try {
            smtp = new Socket(entityUser.getHostAddrSMTP(), SMTP_PORT);
        } catch (Exception e) {
            throw new Exception("发信:建立套接字失败,请检查SMTP服务器地址!");
        }

        //输入流BufferedReader，以便一次读一行来自服务器的应答报文
        smtp_in = new BufferedReader(new InputStreamReader(smtp.getInputStream()));

        //输出流PrintWriter，以便一次输出一行报文到服务器
        smpt_out = new PrintWriter(smtp.getOutputStream(), Boolean.TRUE);

        //读取来自服务器的第一行应答，并显示在屏幕上
        CheckMethods.PrintResponse(smtp_in.readLine());

        //say HELO
        if (!sendCommandAndGetResult("EHLO " + InetAddress.getLocalHost().getHostName())) {
            quitSmtpServer();
            throw new Exception("发送失败:SMTP服务器无响应!");
        } else {
            CheckMethods.ShowProcess("发件:成功连接发件服务器!");
            for (int i = 0; i < 5; i++) {
                //读入来自服务器的5行应答，并显示在屏幕上
                CheckMethods.PrintResponse(smtp_in.readLine());
            }
        }

        //get AUTH
        if (!sendCommandAndGetResult("auth login")
                || !sendCommandAndGetResult(MyBase64.getBASE64(entityUser.getEmailAddr()), "334")
                || !sendCommandAndGetResult(MyBase64.getBASE64(entityUser.getEmailPsw()), "334")) {
            quitSmtpServer();
            throw new Exception("发送失败:邮箱帐号/密码错误!");
        } else {
            CheckMethods.ShowProcess("发件:身份验证成功!");
        }

        CheckMethods.PrintResponse(smtp_in.readLine());
    }

    /**
     * 执行退出连接
     */
    public void quitSmtpServer() throws Exception {
        sendCommandAndGetResult("QUIT", "221");
        smtp.close();
    }

    /**
     * 执行发送
     *
     * @param mail
     * @param to_list
     * @return
     * @throws IOException(需要在CheckMethods.ShowProcess中显示并打开关闭按钮)
     */
    public void Send(EntityEmailWrite emailWrite) throws Exception {
        if (!sendCommandAndGetResult("MAIL FROM: <" + emailWrite.getSenderAddr() + ">")
                || !sendCommandAndGetResult("RCPT TO: <" + emailWrite.getReceiverAddr() + ">")) {
            quitSmtpServer();
            throw new Exception("发送失败:非法/无效邮件头！");
        } else {
            CheckMethods.ShowProcess("发件：邮件头信息通过验证！");
        }
        sendCommandAndGetResult("DATA", "354");

        String content
                = "From: " + emailWrite.getSenderAddr() + "\n" //可选项
                + "To: " + emailWrite.getReceiverAddr() + "\n" //可选项
                + "Subject: " + emailWrite.getMailTitle() + "\n\n" //可选项
                + emailWrite.getMailContent() + '\n';
        sendCommandWithoutGetResult(content);
        if (!sendCommandAndGetResult(".")) {
            quitSmtpServer();
            throw new Exception("发送失败:服务器根据邮件内容拒绝发送！");
        } else {
            CheckMethods.ShowProcess("发件：邮件已加入服务器发送队列！");
        }
        CheckMethods.ShutdownProgress();
    }

    /**
     * 发送命令并返回执行结果
     *
     * @param command 命令
     * @return 成功执行
     * @throws IOException
     */
    private boolean sendCommandAndGetResult(String command) throws IOException {
        smpt_out.print(command + "\r\n");
        smpt_out.flush();
        CheckMethods.PrintSeneding(command);
        String res = smtp_in.readLine();
        CheckMethods.PrintResponse(res);
        return res.startsWith("250");
    }

    /**
     * 发送命令并返回反馈结果
     *
     * @param command 命令
     * @param requireResponse 正确应答码
     * @return 成功执行
     * @throws IOException
     */
    private boolean sendCommandAndGetResult(String command, String requireResponse) throws IOException {
        smpt_out.print(command + "\r\n");
        //flush不需要,因auto
        smpt_out.flush();
        CheckMethods.PrintSeneding(command);
        String res = smtp_in.readLine();
        CheckMethods.PrintResponse(res);
        return res.startsWith(requireResponse);
    }

    /**
     * 发送命令而不获取反馈结果
     *
     * @param command
     * @throws IOException
     */
    private void sendCommandWithoutGetResult(String command) throws IOException {
        smpt_out.print(command + "\r\n");
        smpt_out.flush();
        CheckMethods.PrintSeneding(command);
    }
}
