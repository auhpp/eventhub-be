package com.auhpp.event_management.entity.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@Converter
public class AttributeEncryptor implements AttributeConverter<String, String> {

    private static String SIGNER_KEY;

    @Value("${app.encrypt.encrypt-key-32-bytes}")
    public void setSignerKey(String signerKey) {
        SIGNER_KEY = signerKey;
    }

    private static final String AES = "AES";

    private SecretKeySpec generateKey(String secret) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = sha.digest(secret.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key, AES);
    }

    // Encode data before save to db
    @Override
    public String convertToDatabaseColumn(String attribute) {
        if (attribute == null) return null;
        try {
            SecretKeySpec key = generateKey(SIGNER_KEY);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }

    //Decode when select from DB
    @Override
    public String convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        try {
            SecretKeySpec key = generateKey(SIGNER_KEY);
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
