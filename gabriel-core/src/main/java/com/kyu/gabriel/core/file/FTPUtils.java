package com.kyu.gabriel.core.file;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

public class FTPUtils {

    private final String host;
    private final int port;
    private final String username;
    private final String password;
    private static FTPClient ftpClient;
    private final String encoding;
    private boolean isConnected = false;

    private FTPUtils(String host, int port, String username, String password, String encoding) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.encoding = encoding;
        ftpClient = new FTPClient();
    }

    public static FTPUtils getInstance(String host, int port, String username, String password){
        FTPUtils ftpUtils = new FTPUtils(host, port, username, password, "UTF-8");
        try {
            ftpUtils.connect();
            return ftpUtils;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static FTPUtils getInstance(String host, int port, String username, String password, String encoding){
        FTPUtils ftpUtils = new FTPUtils(host, port, username, password, encoding);
        try {
            ftpUtils.connect();
            return ftpUtils;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean connect() throws IOException {
        System.out.println(MessageFormat.format("开始与{0}:{1}服务器建立连接...", this.host, this.port));
        ftpClient.connect(host, port);
        int replyCode = ftpClient.getReplyCode();
        ftpClient.login(username, password);
        System.out.println("当前状态码为: " + replyCode);
        if (!FTPReply.isPositiveCompletion(replyCode)){
            System.out.println("连接失败, 状态码为: " + replyCode);
            return false;
        }
        System.out.println("成功建立连接!");
        this.isConnected = true;
        return true;
    }

    public void close(){
        if (ftpClient != null && ftpClient.isConnected()){
            try {
                System.out.println("正在注销...");
                ftpClient.logout();
                System.out.println("正在关闭服务器连接...");
                ftpClient.disconnect();
                System.out.println("连接已关闭!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean saveFile(String path, String fileName, InputStream file){
        try {
            boolean flag;
            System.out.println("检查路径中...");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlEncoding(encoding);
            ftpClient.setFileType( FTPClient.BINARY_FILE_TYPE );
            flag = createDirectory(path);
            if (flag) {
                System.out.println("开始上传文件...");
                ftpClient.makeDirectory(path);
                ftpClient.changeWorkingDirectory(path);
                ftpClient.storeFile(fileName, file);
            }
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public boolean download(String path, String fileName, OutputStream os){
        try {
            boolean flag = false;
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlEncoding(encoding);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            System.out.println("开始下载文件...");
            if (!ftpClient.printWorkingDirectory().equalsIgnoreCase(path)){
                ftpClient.changeWorkingDirectory(path);
            }
            FTPFile []files = ftpClient.listFiles();
            for (FTPFile file: files){
                if (file.getName().equals(fileName)){
                    flag =true;
                }
            }
            if (!flag){
                System.out.println("未检索到该文件");
                return false;
            }
            ftpClient.retrieveFile(fileName, os);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean delete(String path, String fileName){
        try {
            ftpClient.enterLocalPassiveMode();
            ftpClient.setControlEncoding(encoding);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            System.out.println("开始删除文件...");
            ftpClient.changeWorkingDirectory(path);
            ftpClient.dele(fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    //创建多层目录文件，如果有ftp服务器已存在该文件，则不创建，如果无，则创建
    public boolean createDirectory(String remote) throws IOException {
        String directory = remote + "/";
        // 如果远程目录不存在，则递归创建远程服务器目录
        if (!directory.equalsIgnoreCase("/") && !changeWorkingDirectory(directory)) {
            int start = 0;
            int end = 0;
            if (directory.startsWith("/")) {
                start = 1;
            }
            end = directory.indexOf("/", start);
            String path = "";
            do {
                String subDirectory = new String(remote.substring(start, end).getBytes("GBK"), StandardCharsets.ISO_8859_1);
                path = path + "/" + subDirectory;
                if (!existFile(path)) {
                    if (makeDirectory(subDirectory)) {
                        changeWorkingDirectory(subDirectory);
                    } else {
                        return false;
                    }
                } else {
                    changeWorkingDirectory(subDirectory);
                }
                start = end + 1;
                end = directory.indexOf("/", start);
                // 检查所有目录是否创建完毕
            } while (end > start);
        }
        return true;
    }

    //改变目录路径
    public boolean changeWorkingDirectory(String directory) {
        boolean flag = true;
        try {
            if (directory.equalsIgnoreCase(ftpClient.printWorkingDirectory())){
                return true;
            }
            flag = ftpClient.changeWorkingDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean existFile(String path) throws IOException {
        boolean flag = false;
        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
        if (ftpFileArr.length > 0) {
            flag = true;
        }
        return flag;
    }

    public boolean makeDirectory(String dir) throws IOException {
        boolean flag = true;
        flag = ftpClient.makeDirectory(dir);
        if (flag) {
            System.out.println("创建文件夹: " + dir + " 成功!");
        } else {
            System.out.println("创建文件夹: " + dir + " 失败!");
        }
        return flag;
    }

    public boolean doUpload(String path, String fileName, File file) throws FileNotFoundException {
        return doUpload(path, fileName, new FileInputStream(file));
    }

    public boolean doUpload(String path, String fileName, InputStream file){
        if(!isConnected){
            try {
                if (!connect()){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean flag = saveFile(path, fileName, file);
        if (flag){
            System.out.println("上传成功!");
        } else {
            System.out.println("上传失败!");
        }
        return flag;
    }

    public boolean doDownload(String remotePath, String fileName, File localFile) throws FileNotFoundException {
        return doDownload(remotePath, fileName, new FileOutputStream(localFile));
    }

    public boolean doDownload(String remotePath, String fileName, OutputStream outputStream){
        if(!isConnected){
            try {
                if (!connect()){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean flag = download(remotePath, fileName, outputStream);
        if (flag){
            System.out.println("下载成功!");
        } else {
            System.out.println("下载失败!");
        }
        return flag;
    }

    public boolean doDelete(String remotePath, String fileName){
        if(!isConnected){
            try {
                if (!connect()){
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        boolean flag = delete(remotePath, fileName);
        if (flag){
            System.out.println("删除成功!");
        } else {
            System.out.println("删除失败!");
        }
        return flag;
    }

    public static void main(String[] args) throws FileNotFoundException {
        FTPUtils ftpUtils = FTPUtils.getInstance("218.93.206.10", 21, "administrator", "928495870irisu!@", "GBK");
        File file = new File("C:\\Users\\92849\\Desktop\\event.png");
        System.out.println(ftpUtils.doDelete("/", "app.jpg"));
    }
}
