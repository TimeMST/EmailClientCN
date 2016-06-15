package controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Iterator;
import model.AccessEmailReceive;
import model.EntityEmailReceive;
import model.EntityUser;
import tools.CheckMethods;
import tools.MyBase64;

public class PopController {

    private final int POP_PORT = 110;
    private EntityUser entityUser;
    private EntityEmailReceive entityEmailReceive;
    private Socket pop;
    private BufferedReader pop_in;
    private PrintWriter pop_out;

    public static void main(String[] args) {

        try {
            PopController popController = new PopController();
            popController.login2PopServer();
            popController.getAllEmails();
//            ArrayList<String> emailHeaderArrayList = popController.receiveEmailHeaderBySN(String.valueOf(10));
//            String messageIDString = popController.getMessageIDParsingRes(emailHeaderArrayList);
//            CheckMethods.ShowProcess(messageIDString);
//
////            String mailContentString = popController.getEmailContentByMessageID(messageIDString);
////            CheckMethods.ShowProcess(mailContentString);
//            CheckMethods.ShowProcess(popController.findEmailSNByMessageID(messageIDString));
//            Address[] addresses = this.mimeMessage.getFrom();
//            return addresses != null && addresses.length != 0 ? ((InternetAddress) addresses[0]).getAddress() : null;
            popController.quitPop();
        } catch (Exception e) {
            CheckMethods.ShutdownProgress();
            CheckMethods.PrintDebugMessage(e.getMessage(), "pop");
        }
    }

    public PopController() {
        super();
        entityEmailReceive = new EntityEmailReceive();
//        entityUser = globalRecords.getEntityUser();
        entityUser = new EntityUser();
        entityUser.setEmailAddr("maokelong95@163.com");
        entityUser.setEmailPsw("test123");
        entityUser.setHostAddrPOP3("pop3.163.com");
    }

    /**
     * 获取所有信件并存储到数据库中去
     *
     * @throws Exception
     */
    public void getAllEmails() throws Exception {
        ArrayList<EntityEmailReceive> arrayList = new ArrayList<>();
        AccessEmailReceive accessEmailReceive = new AccessEmailReceive();
        int count = getCountsOfEmails();
        for (int i = 1; i <= count; i++) {
            accessEmailReceive.saveEmailReceive(getSingleEmailBySN(String.valueOf(i)));
            CheckMethods.ShowProcess("收信:正在同步第 " + i + "/" + count + " 封邮件的头部信息!");
        }
        CheckMethods.ShowProcess("收信:已将所有信件(仅头部)下载到本地数据库中!");
    }

    private EntityEmailReceive getSingleEmailBySN(String SNString) throws Exception {
        EntityEmailReceive entityEmailReceive = new EntityEmailReceive();
        ArrayList<String> receiveEmailHeaderBySN = receiveEmailHeaderBySN(SNString);
        entityEmailReceive.setSenderAddrString(getSenderAddrParsingRes(receiveEmailHeaderBySN));
        entityEmailReceive.setMailTitleString(getTitleParsingRes(receiveEmailHeaderBySN));
        entityEmailReceive.setMailDevDatesString(getDateParsingRes(receiveEmailHeaderBySN));
        entityEmailReceive.setMessageIDString(getMessageIDParsingRes(receiveEmailHeaderBySN));
        return entityEmailReceive;

    }

    /**
     * 根据序号删除一封邮件
     *
     * @param snString
     * @throws Exception
     */
    private void deleteSingleEmailFromServerBySN(String snString) throws Exception {
        String line = sendCommandAndReceiveOneLine("DELE " + snString);
        if (!line.startsWith("+OK")) {
            quitPop();
            throw new Exception("删件:删件失败!");
        }
    }

    /**
     * 根据ID删除一封邮件
     *
     * @param snString
     * @throws Exception
     */
    public void deleteSingleEmailFromServerByMessageID(String messageIDString) throws Exception {
        String findEmailSNByMessageID = findEmailSNByMessageID(messageIDString);
        deleteSingleEmailFromServerBySN(findEmailSNByMessageID);
    }

    /**
     *
     * @param snsString
     * @return
     * @throws Exception
     */
    private ArrayList<String> receiveEmailHeaderBySN(String snsString) throws Exception {
        ArrayList<String> arrayList = sendCommandAndReceiveMutiLines("top " + snsString + " 0");
        if (!arrayList.get(0).startsWith("+OK")) {
            throw new Exception("收件:获取目标邮件邮件头失败!");
        }
        return arrayList;
    }

    /**
     * 根据邮件的MessageID获取邮件的内容
     *
     * @param messageIDString
     * @return
     * @throws Exception
     */
    public String getEmailContentByMessageID(String messageIDString) throws Exception {
        //获取序号
        String sn = findEmailSNByMessageID(messageIDString);

        //获取该邮件信息
        ArrayList<String> arrayList = sendCommandAndReceiveMutiLines("RETR " + sn);
        if (!arrayList.get(0).startsWith("+OK")) {
            throw new Exception("删件:获取目标邮件失败!");
        }

        //截获邮件内容
//        return MyBase64.getFromBASE64(getContentParsingRes(arrayList));
        return getContentParsingRes(arrayList);
    }

    /**
     * 根据邮件ID查找邮件序号
     *
     * @param messageIDString
     * @return
     * @throws Exception
     */
    private String findEmailSNByMessageID(String messageIDString) throws Exception {
        if (messageIDString.length() == 0) {
            throw new Exception("POP:该邮件因未提供Message-ID而无法删除!");
        }
        int nunMails = getCountsOfEmails();
        int i;
        for (i = 1; i <= nunMails; i++) {
            ArrayList<String> arrayList = receiveEmailHeaderBySN(String.valueOf(i));
            String messageIDTemp = getMessageIDParsingRes(arrayList);
            if (messageIDTemp.length() == 0) {
                continue;
            }
            if (messageIDString.length() >= messageIDTemp.length()
                    && messageIDString.startsWith(messageIDTemp)) {
                break;
            }
        }
        if (i > nunMails) {
            throw new Exception("删除失败:查无对应MessageID邮件!");
        }
        return String.valueOf(i);
    }

    private String getSenderAddrParsingRes(ArrayList<String> arrayList) {
        return getNeedHeaderPartByKey(arrayList, "From:");
    }

    private String getMessageIDParsingRes(ArrayList<String> arrayList) {
        return getNeedHeaderPartByKey(arrayList, "Message-ID:");
    }

    private String getDateParsingRes(ArrayList<String> arrayList) {
        return getNeedHeaderPartByKey(arrayList, "Date:");
    }

    private String getTitleParsingRes(ArrayList<String> arrayList) {
        return getNeedHeaderPartByKey(arrayList, "Subject:");
    }

    private String getContentParsingRes(ArrayList<String> arrayList) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<String> iterator = arrayList.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if (stringBuilder.length() == 0 && next.length() == 0) {
                stringBuilder.append(" ");
            } else if (stringBuilder.length() != 0) {
                stringBuilder.append(next);
            }
        }
        return stringBuilder.toString().trim();
    }

    /**
     * 通过关键字获取邮件头中的相应部分
     *
     * @param arrayList 返回结果
     * @param keyString 关键字
     * @return 所需关键字或者空串
     */
    private String getNeedHeaderPartByKey(ArrayList<String> arrayList, String keyString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Iterator<String> iterator = arrayList.iterator(); iterator.hasNext();) {
            String next = iterator.next();
            if (next.toLowerCase().startsWith(keyString.toLowerCase())) {
                stringBuilder.append(next.substring(keyString.length() + 1));
            } else if (stringBuilder.length() != 0 && next.startsWith("\t")) {
                stringBuilder.append(next.substring(next.indexOf("\t") + 1));
            } else if (stringBuilder.length() != 0) {
                break;
            }
        }
        return stringBuilder.toString().trim();
    }

    public int getCountsOfEmails() throws Exception {
        ArrayList<String> arrayList = sendCommandAndReceiveMutiLines("LIST");
        int ret = 0;
        if (!arrayList.get(0).startsWith("+OK")) {
            quitPop();
            throw new Exception("收件:获取邮件序号集失败!");
        } else {
            ret = arrayList.size() - 1;
        }
        return ret;
    }

    /**
     * 退出pop3对话结束
     */
    public void quitPop() throws Exception {
        pop_out.print("QUIT" + "\r\n");
        pop.close();
    }

    /**
     * 连接到服务器
     */
    public void login2PopServer() throws Exception {
        //建立套接字
        try {
            pop = new Socket(entityUser.getHostAddrPOP3(), POP_PORT);
        } catch (Exception e) {
            throw new Exception("发信:建立套接字失败,请检查POP服务器地址!");
        }

        //输入流BufferedReader，以便一次读一行来自服务器的应答报文
        pop_in = new BufferedReader(new InputStreamReader(pop.getInputStream()));

        //输出流PrintWriter，以便一次输出一行报文到服务器
        pop_out = new PrintWriter(pop.getOutputStream(), Boolean.TRUE);

        // 读取登陆回复
        String res = pop_in.readLine();
        CheckMethods.PrintResponse(res);
        //  如果传回的回复不是 + OK ....
        if (!res.startsWith("+OK")) {
            pop.close();
            throw new Exception("接收失败:POP服务器无响应!");
        } else {
            CheckMethods.ShowProcess("收件:成功连接发件服务器!");
        }
        if (!sendCommandAndReceiveOneLine("USER " + entityUser.getEmailAddr()).startsWith("+OK")
                || !sendCommandAndReceiveOneLine("PASS " + entityUser.getEmailPsw()).startsWith("+OK")) {
            quitPop();
            throw new Exception("收件失败:邮箱帐号/密码错误!");
        } else {
            CheckMethods.ShowProcess("收件:身份验证成功!");
        }
    }

    /**
     * 发送命令,并接收多行回复(包括执行信息行)
     *
     * @param command 命令
     * @return 回复
     */
    private ArrayList<String> sendCommandAndReceiveMutiLines(String command) throws Exception {
        ArrayList<String> retArrayList = new ArrayList<>();

        // 发送命令
        pop_out.print(command + "\r\n");
        pop_out.flush();
        CheckMethods.PrintSeneding(command);

        // 读取回复
        String res = pop_in.readLine();
        retArrayList.add(res);
        CheckMethods.PrintResponse(res);

        if (!res.startsWith("+OK")) {
            // 执行失败,返回空
            retArrayList = null;
        } else {
            //读取并返回有效行
            while (Boolean.TRUE) {
                String resString = pop_in.readLine();
                CheckMethods.PrintResponse(resString);
                if (resString.equals(".")) {
                    break;
                } else {
                    retArrayList.add(resString);
                }
            }
        }
        return retArrayList;
    }

    /**
     * 发送命令,并接收一行回复(执行信息行)
     *
     * @param command 命令
     * @return 回复
     */
    private String sendCommandAndReceiveOneLine(String command) throws Exception {
        String res = null;
        pop_out.print(command + "\r\n");
        pop_out.flush();
        CheckMethods.PrintSeneding(command);
        res = pop_in.readLine();
        CheckMethods.PrintResponse(res);
        return res;
    }
}
