/*
 * Project      : TN_COMMON
 * File Name    : biz.trustnet.common.util.ZipUtil.java
 * Date         : 2010. 6. 20.
 * Version      : 1.0
 * Author       : ginaida@trustmate.net
 * Comment      :
 */

package biz.trustnet.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import biz.trustnet.common.io.FileUtil;

public class ZipUtil {


    private static final int COMPRESSION_LEVEL = 8;
    private static final int BUFFER_SIZE = 1024 * 10;

    /**
     * ������ ������ Zip ���Ϸ� �����Ѵ�.
     * @param sourcePath - ���� ��� ���丮
     * @param output - ���� zip ���� �̸�
     * @throws Exception
     */
    public static void zip(String sourcePath, String output) throws Exception {

        // ���� ���(sourcePath)�� ���丮�� ������ �ƴϸ� �����Ѵ�.
        File sourceFile = new File(sourcePath);
        if (!sourceFile.isFile() && !sourceFile.isDirectory()) {
            throw new Exception("���� ����� ������ ã�� ���� �����ϴ�.");
        }

        // output �� Ȯ���ڰ� zip�� �ƴϸ� �����Ѵ�.
        if (!output.endsWith("zip")) {
            throw new Exception("���� �� ���� ���ϸ��� Ȯ���ڸ� Ȯ���ϼ���");
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;

        try {
            fos = new FileOutputStream(output); // FileOutputStream
            bos = new BufferedOutputStream(fos); // BufferedStream
            zos = new ZipOutputStream(bos); // ZipOutputStream
            zos.setLevel(COMPRESSION_LEVEL); // ���� ���� - �ִ� ������� 9, ����Ʈ 8
            zipEntry(sourceFile, sourcePath, zos); // Zip ���� ����
            zos.finish(); // ZipOutputStream finish
        } finally {
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }

    public static void zipSingle(String sourceFile, String entryName,String output) throws Exception {

        // ���� ���(sourcePath)�� ���丮�� ������ �ƴϸ� �����Ѵ�.
        File source = new File(sourceFile);
        if (!source.isFile() && !source.isDirectory()) {
            throw new Exception("���� ����� ������ ã�� ���� �����ϴ�.");
        }


        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        BufferedInputStream bis = null;

        try {
            fos = new FileOutputStream(output); // FileOutputStream
            bos = new BufferedOutputStream(fos); // BufferedStream
            zos = new ZipOutputStream(bos); // ZipOutputStream
            zos.setLevel(COMPRESSION_LEVEL); // ���� ���� - �ִ� ������� 9, ����Ʈ 8
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            ZipEntry zentry = new ZipEntry(entryName);
            zentry.setTime(source.lastModified());
            zos.putNextEntry(zentry);

            byte[] buffer = new byte[BUFFER_SIZE];
            int cnt = 0;
            while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                zos.write(buffer, 0, cnt);
            }
            zos.closeEntry();
            zos.finish(); // ZipOutputStream finish
        } finally {
        	if (bis != null) {
        		bis.close();
            }
            if (zos != null) {
                zos.close();
            }
            if (bos != null) {
                bos.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }


    /**
     * ����
     * @param sourceFile
     * @param sourcePath
     * @param zos
     * @throws Exception
     */
    private static void zipEntry(File sourceFile, String sourcePath, ZipOutputStream zos) throws Exception {
        // sourceFile �� ���丮�� ��� ���� ���� ����Ʈ ������ ���ȣ��
        if (sourceFile.isDirectory()) {
            if (sourceFile.getName().equalsIgnoreCase(".metadata")) { // .metadata ���丮 return
                return;
            }
            File[] fileArray = sourceFile.listFiles(); // sourceFile �� ���� ���� ����Ʈ
            for (int i = 0; i < fileArray.length; i++) {
                zipEntry(fileArray[i], sourcePath, zos); // ��� ȣ��
            }
        } else { // sourcehFile �� ���丮�� �ƴ� ���
            BufferedInputStream bis = null;
            try {
                String sFilePath = sourceFile.getPath();
                String zipEntryName = sFilePath.substring(sourcePath.length() + 1, sFilePath.length());

                bis = new BufferedInputStream(new FileInputStream(sourceFile));
                ZipEntry zentry = new ZipEntry(zipEntryName);
                zentry.setTime(sourceFile.lastModified());
                zos.putNextEntry(zentry);

                byte[] buffer = new byte[BUFFER_SIZE];
                int cnt = 0;
                while ((cnt = bis.read(buffer, 0, BUFFER_SIZE)) != -1) {
                    zos.write(buffer, 0, cnt);
                }
                zos.closeEntry();
            } finally {
                if (bis != null) {
                    bis.close();
                }
            }
        }
    }

    /**
     * Zip ������ ������ Ǭ��.
     *
     * @param zipFile - ���� Ǯ Zip ����
     * @param targetDir - ���� Ǭ ������ �� ���丮
     * @param fileNameToLowerCase - ���ϸ��� �ҹ��ڷ� �ٲ��� ����
     * @throws Exception
     */
    public static void unzip(File zipFile, File targetDir, boolean fileNameToLowerCase) throws Exception {
        FileInputStream fis = null;
        ZipInputStream zis = null;
        ZipEntry zentry = null;

        try {
            fis = new FileInputStream(zipFile); // FileInputStream
            zis = new ZipInputStream(fis); // ZipInputStream

            while ((zentry = zis.getNextEntry()) != null) {
                String fileNameToUnzip = zentry.getName();
                if (fileNameToLowerCase) { // fileName toLowerCase
                    fileNameToUnzip = fileNameToUnzip.toLowerCase();
                }

                File targetFile = new File(targetDir, fileNameToUnzip);

                if (zentry.isDirectory()) {// Directory �� ���
                    new FileUtil().makeDirectory(targetFile.getAbsolutePath()); // ���丮 ����
                } else { // File �� ���
                    // parent Directory ����
                	 new FileUtil().makeDirectory(targetFile.getParent());
                    unzipEntry(zis, targetFile);
                }
            }
        } finally {
            if (zis != null) {
                zis.close();
            }
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * Zip ������ �� �� ��Ʈ���� ������ Ǭ��.
     *
     * @param zis - Zip Input Stream
     * @param filePath - ���� Ǯ�� ������ ���
     * @return
     * @throws Exception
     */
    protected static File unzipEntry(ZipInputStream zis, File targetFile) throws Exception {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(targetFile);

            byte[] buffer = new byte[BUFFER_SIZE];
            int len = 0;
            while ((len = zis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return targetFile;
    }


}
