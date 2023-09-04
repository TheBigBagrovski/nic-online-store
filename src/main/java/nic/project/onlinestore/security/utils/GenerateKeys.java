package nic.project.onlinestore.security.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

/**
 * Вспомогательный класс для генерации секретных ключей для access и refresh токенов
 * при необходимости обновить/создать ключи - нужно раскоментировать строчки в функции main
 */
public class GenerateKeys {

    public static String generateKey() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }

}
