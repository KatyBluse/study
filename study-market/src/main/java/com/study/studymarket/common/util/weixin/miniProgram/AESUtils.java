package com.study.studymarket.common.util.weixin.miniProgram;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;

/**
 * 微信小程序AES解密
 *
 *
 * */
public class AESUtils {

    static {
        Security.addProvider(new BouncyCastleProvider());//// 添加BouncyCastle作为安全提供
    }

    /**
     * 解密
     * 核心组件Cipher
     * @param base64Key
     * @param base64Iv
     * @param base64EncryptedData
     *
     * */
    public static byte[] decrypt(byte[] base64Key, byte[] base64Iv, byte[] base64EncryptedData) throws InvalidAlgorithmParameterException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");// 算法/模式/填充, 对称解密使用的算法为 AES-128-CBC，数据采用PKCS#7填充。
            Key sKeySpec = new SecretKeySpec(base64Key, "AES");//设置秘钥规则为AES
            AlgorithmParameters params = AlgorithmParameters.getInstance("AES");
            params.init(new IvParameterSpec(base64Iv));//设置加密参数规则为AES，返回的对象为AlgorithmParameters
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, params);//秘钥+算法参数 初始化cipher
            byte[] result = cipher.doFinal(base64EncryptedData);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }
}
