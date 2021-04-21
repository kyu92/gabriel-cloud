package com.kyu.gabriel.core.file;

import com.kyu.gabriel.core.config.ConfigMap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MinioUtil {

    @Setter
    private MinioClient minioClient;
    @Getter
    @Setter
    private String endpoint;
    @Getter
    @Setter
    private String accessKey;
    @Getter
    @Setter
    private String secretKey;
    private ConfigMap configMap;

    private static MinioUtil minioUtil;

    @Deprecated
    public MinioUtil(MinioClient client){
        this.minioClient = client;
    }

    private MinioUtil(String endpoint, String accessKey, String secretKey){
        this.endpoint = endpoint;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.minioClient = new MinioClient.Builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
    }

    public static MinioUtil getInstance(ConfigMap configMap){
        String endpoint, accessKey, secretKey;
        try {
            endpoint = configMap.get("minioEndpoint");
            accessKey = configMap.get("minioAccessKey");
            secretKey = configMap.get("minioSecretKey");
        } catch (Exception ignore){
            endpoint = "http://127.0.0.1:9000";
            accessKey = "minio";
            secretKey = "minio";
        }
        if (minioUtil != null){
            String sourceEndpoint = minioUtil.getEndpoint();
            String sourceAccessKey = minioUtil.getAccessKey();
            String sourceSecretKey = minioUtil.getSecretKey();
            if (sourceEndpoint.equals(endpoint) && sourceAccessKey.equals(accessKey) && sourceSecretKey.equals(secretKey)){
                return minioUtil;
            }
        }
        minioUtil = new MinioUtil(endpoint, accessKey, secretKey);
        minioUtil.configMap = configMap;
        return minioUtil;
    }

    public void checkConfig(){
        String endpoint = configMap.get("minioEndpoint"),
                accessKey = configMap.get("minioAccessKey"),
                secretKey = configMap.get("minioSecretKey");
        if (!this.getEndpoint().equals(endpoint) || !this.getAccessKey().equals(accessKey) || !this.getSecretKey().equals(secretKey)){
            this.minioClient = new MinioClient.Builder().endpoint(endpoint).credentials(accessKey, secretKey).build();
        }
    }

    public boolean isBucketExists(String bucketName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        checkConfig();
        BucketExistsArgs args = BucketExistsArgs.builder().bucket(bucketName).build();
        return minioClient.bucketExists(args);
    }

    public void makeBucket(String name) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        checkConfig();
        if (isBucketExists(name)){
            return;
        }
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(name).build());
    }

    public void upload(String bucketName, String fileName, InputStream is, String contentType) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        makeBucket(bucketName);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(bucketName)
                .object(fileName)
                .contentType(contentType)
                .stream(is, -1, 1024 * 1024 * 10)
                .build();
        minioClient.putObject(args);
    }

    public GetObjectResponse download(String bucketName, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        checkConfig();
        GetObjectArgs args = GetObjectArgs.builder().bucket(bucketName).object(fileName).build();
        return minioClient.getObject(args);
    }

    public String download2URL(String bucketName, String fileName, Method method, int expire) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        checkConfig();
        GetPresignedObjectUrlArgs args = GetPresignedObjectUrlArgs.builder()
                .method(method)
                .expiry(expire, TimeUnit.SECONDS)
                .bucket(bucketName)
                .object(fileName)
                .build();
        return minioClient.getPresignedObjectUrl(args);
    }

    public void remove(String bucketName, String fileName) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        checkConfig();
        RemoveObjectArgs args = RemoveObjectArgs.builder().bucket(bucketName).object(fileName).build();
        minioClient.removeObject(args);
    }

    public boolean deleteBucket(String bucketName){
        checkConfig();
        try {
            if (!isBucketExists(bucketName)){
                return true;
            }
            RemoveBucketArgs args = RemoveBucketArgs.builder().bucket(bucketName).build();
            minioClient.removeBucket(args);
            return true;
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<String> listAllObjects(String bucketName){
        checkConfig();
        List<String> objectNames = new ArrayList<>();
        try {
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
            for (Result<Item> item: minioClient.listObjects(args)){
                objectNames.add(item.get().objectName());
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return objectNames;
    }

    public List<String> listAllObjects(String bucketName, String folder){
        checkConfig();
        List<String> objectNames = new ArrayList<>();
        try {
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).prefix(folder).build();
            for (Result<Item> item: minioClient.listObjects(args)){
                objectNames.add(item.get().objectName());
            }
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return objectNames;
    }

    public boolean deleteAllFile(String bucketName){
        checkConfig();
        try {
            if (!isBucketExists(bucketName)){
                return true;
            }
            ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).build();
            for (Result<Item> item : minioClient.listObjects(args)) {
                String name = item.get().objectName();
                if (name.endsWith("/")){
                    List<String> objectsName = listAllObjects(bucketName, name);
                    for (String obj: objectsName){
                        remove(bucketName, obj);
                    }
                }
                remove(bucketName, name);
            }
            return true;
        } catch (InvalidKeyException | NoSuchAlgorithmException | ErrorResponseException | InvalidResponseException | ServerException | InsufficientDataException | XmlParserException | InternalException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) throws IOException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
        MinioUtil minioUtil = new MinioUtil("http://localhost:9000", "minio", "kyu92.top");
//        System.out.println(minioUtil.listAllObjects("4a052408-f9d0-4f59-94a9-1327fa8c461d", "avatar/"));
        minioUtil.remove("4a052408-f9d0-4f59-94a9-1327fa8c461d", "avatar/1616333507541.jpg");
    }
}
