package com.kep.portal.model.entity.customer;

import org.jasypt.encryption.StringEncryptor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Named("CustomerTranslator")
public class CustomerTranslator {

//    @Resource(name = "defaultEncryptor")
    @Resource(name = "fixedEncryptor")
    private StringEncryptor defaultEncryptor;

    @Named("Encryptor")
    public String encryptor(String value){
        try {
            return defaultEncryptor.encrypt(value);
        } catch (Exception e){

        }
        return "";

    }

    @Named("Decrypt")
    public String decrypt(String value){
        try {
            return defaultEncryptor.decrypt(value);
        } catch (Exception e){

        }
        return "";

    }
}
