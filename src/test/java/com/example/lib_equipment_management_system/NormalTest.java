package com.example.lib_equipment_management_system;

import org.junit.jupiter.api.Test;

import java.util.UUID;

public class NormalTest {

    @Test
    void test1() {
        System.out.println(UUID.randomUUID().toString().length());

    }

    @Test
    void test2() {
        String s = "1211";
        System.out.println(s.matches("[0-9]+"));
    }




}
/*
MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJs67NUzR_NgkTKjtm_C-P3S
MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIbYFx7SnCLvvuP7Xj
MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAKIr_mF2KYeMf1wwMbI_ZX0
MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIU3AbJ-pHj0BDKsSyOXUO
 */