package com.makancompany.assistant.Kernel.Helper;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileManager {
    public static void copyFile(String src, String dest) throws IOException {
        FileUtils.copyFile(new File(src), new File(dest));
    }

    public static void moveFile(String src, String dest) throws IOException {
        File f1 = new File(src);
        File f2 = new File(dest);
        FileUtils.forceMkdir(f2.getParentFile());
        if (f2.exists())
            FileUtils.forceDelete(f2);
        FileUtils.moveFile(f1, f2);
    }

    public static void deleteIfExists(String path) throws IOException {
        File f = new File(path);
        if (f.exists())
            delete(path);
    }

    public static void delete(String path) throws IOException {
        File f = new File(path);
        FileUtils.forceDelete(f);
    }

    public static void mkDir(String path) throws IOException {
        File f = new File(path);
        FileUtils.forceMkdir(f);
    }

    public static void mkDirParent(String path) throws IOException {
        File f = new File(path);
        FileUtils.forceMkdirParent(f);
    }

    public static String hash(String path) throws IOException {
        File f = new File(path);
        return hash(f);
    }

    public static String hash(File f) throws IOException {
        try {
            int size = (int) f.length();
            byte[] bytes = new byte[size];
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(f));
            buf.read(bytes, 0, bytes.length);
            buf.close();
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(bytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
            }
            return sb.toString().toUpperCase();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    //todo write this method
    public static String getContentType(String filePath) {
        return null;
    }
}
