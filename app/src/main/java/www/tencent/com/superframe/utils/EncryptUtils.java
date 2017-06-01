package www.tencent.com.superframe.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 加密工具类
 *
 * @author
 * @description
 * @created 2013年10月31日
 */
public class EncryptUtils {

    private static final String TAG = EncryptUtils.class.getSimpleName();

    public static String encryptSHA1(String srcString) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("sha1");
            md.update(srcString.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    public static String encryptPW(String passWord) {
        if (TextUtils.isEmpty(passWord))
            return "";
        return EncryptUtils.encryptMD5(passWord + "+^_^+Mars_V5");
    }

    /**
     * @param strOrigin
     * @return
     */
    public static String encryptMD5(String strOrigin) {
        /**
         * 原始字符串UTF-8编码
         */
        byte[] byteUTF8 = null;
        try {
            byteUTF8 = strOrigin.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        if (byteUTF8 == null)
            return "";

        try {
            /**
             * MD5加密
             */
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(byteUTF8);
            byte[] byteMD5 = md.digest();

            /**
             * MD5字节数组转字符串
             */
            StringBuffer strMd5Buffer = new StringBuffer();
            for (int i = 0; i < byteMD5.length; i++) {
                String strHex = Integer.toHexString(byteMD5[i] & 0xff);
                //补齐2位16进制数
                if (strHex.length() == 0) {
                    strMd5Buffer.append("00");
                } else if (strHex.length() == 1) {
                    strMd5Buffer.append("0");
                }
                strMd5Buffer.append(strHex);
            }

            return strMd5Buffer.toString();
        } catch (Exception e) {
        }
        return "";
    }


    public static String encryptBASE64(String srcString) {
        if (!TextUtils.isEmpty(srcString)) {
            try {
                byte[] encode = srcString.getBytes("UTF-8");
                return new String(Base64.encode(encode, 0, encode.length,
                        Base64.DEFAULT), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    public static String decryptBASE64(String destString) {
        if (!TextUtils.isEmpty(destString)) {
            try {
                byte[] encode = destString.getBytes("UTF-8");
                return new String(Base64.decode(encode, 0, encode.length,
                        Base64.DEFAULT), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    public static byte[] encryptGZIP(String srcString) {
        if (!TextUtils.isEmpty(srcString)) {
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                GZIPOutputStream gzip = new GZIPOutputStream(baos);
                gzip.write(srcString.getBytes("UTF-8"));
                gzip.close();
                byte[] encode = baos.toByteArray();
                baos.flush();
                baos.close();
                return encode;
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    public static String decryptGZIP(String destString) {
        if (!TextUtils.isEmpty(destString)) {
            try {
                byte[] decode = destString.getBytes("UTF-8");
                ByteArrayInputStream baInStream = new ByteArrayInputStream(
                        decode);
                GZIPInputStream gzipInStream = new GZIPInputStream(baInStream);
                int BUFFER_SIZE = 8 * 1024;
                byte[] buf = new byte[BUFFER_SIZE];
                int len = 0;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = gzipInStream.read(buf, 0, BUFFER_SIZE)) != -1) {
                    baos.write(buf, 0, len);
                }
                gzipInStream.close();
                baos.flush();
                decode = baos.toByteArray();
                baos.close();
                return new String(decode, "UTF-8");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
        }
        return null;
    }

    /**
     * 获取文件的MD5
     *
     * @param filePath
     * @return
     */
    public static String getFileMD5(String filePath) {
        if (!isFileExists(filePath)) {
            throw new IllegalArgumentException("file path is empty!");
        }

        MessageDigest md = null;
        FileInputStream fis = null;
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            md = MessageDigest.getInstance("MD5");
            fis = new FileInputStream(new File(filePath));
            while ((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }
        } catch (Exception e) {
            return "";
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        BigInteger bigInt = new BigInteger(1, md.digest());
        return bigInt.toString(16);
    }

    private EncryptUtils() {
    }

    ;

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;

        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    //SHA1 加密实例
    public static String encryptToSHA(String info) {
        byte[] digesta = null;
        try {
            // 得到一个SHA-1的消息摘要
            MessageDigest alga = MessageDigest.getInstance("SHA-1");
            // 添加要进行计算摘要的信息
            alga.update(info.getBytes());
            // 得到该摘要
            digesta = alga.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        // 将摘要转为字符串
        String rs = byte2hex(digesta);
        return rs;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }
        return hs;
    }

    public static String hexSHA1(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(value.getBytes("utf-8"));
            byte[] digest = md.digest();
            return byteToHexString(digest);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String byteToHexString(byte[] bytes) {
//        return String.valueOf(Hex.encodeHex(bytes));
        return "";
    }

    /**
     * rsa 加密
     *
     * @param context
     * @param data
     * @return
     */
    public static String encryptToRSA(Context context, String data) {

        String afterdata = "";
        try {
            InputStream inPublic = context.getResources().getAssets().open("rsa_public_key.pem");
            PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
            byte[] encryptByte = RSAUtils.encryptData(data.getBytes(), publicKey);
            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
            afterdata = Base64Utils.encode(encryptByte);
            LogUtils.e("afterdata:" + afterdata);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterdata;

    }

    /**
     * rsa 解密
     *
     * @param context
     * @param data
     * @return
     */
    public static String decryptToRSA(Context context, String data) {

        String afterdata = "";
        try {
            InputStream inPrivate = context.getResources().getAssets().open("pkcs8_private_key.pem");
            PrivateKey privateKey = RSAUtils.loadPrivateKey(inPrivate);
            byte[] decryptByte = RSAUtils.decryptData(Base64Utils.decode(data), privateKey);
            afterdata = new String(decryptByte);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return afterdata;

    }


}
