package com.example.cursework;

import junit.framework.TestCase;

public class ValidationTest extends TestCase {

    public void testIsStrongLogin() {
        assertFalse(Validation.isStrongLogin("1111"));
        assertTrue(Validation.isStrongLogin("Login"));
    }

    public void testIsStrongPassword() {
        assertFalse(Validation.isStrongPassword("A1"));
        assertFalse(Validation.isStrongPassword("Pass12"));
        assertTrue(Validation.isStrongPassword("StUdeNT15"));
    }

    public void testIsRightName() {
        assertFalse(Validation.isRightName(""));
        assertTrue(Validation.isRightName("1111"));
        assertTrue(Validation.isRightName("Антон"));
        assertTrue(Validation.isRightName("антон"));
        assertTrue(Validation.isRightName("Anton"));
        assertTrue(Validation.isRightName("Anton"));
        assertTrue(Validation.isRightName("Имя12"));
    }

    public void testIsRightEmail() {
        assertFalse(Validation.isRightEmail("1111"));
        assertFalse(Validation.isRightEmail("почта12"));
        assertTrue(Validation.isRightEmail("example@mail.ru"));
    }

    public void testIsRightPhone() {
        assertFalse(Validation.isRightPhone("1111"));
        assertFalse(Validation.isRightPhone("phone12"));
        assertTrue(Validation.isRightPhone("89091534357"));
    }

    public void testIsRightCompanyNumber() {
        assertFalse(Validation.isRightCompanyNumber("1111"));
        assertFalse(Validation.isRightCompanyNumber("numb12"));
        assertTrue(Validation.isRightCompanyNumber("1234567890123"));
    }

    public void testIsRightPassport() {
        assertFalse(Validation.isRightPassport("1111"));
        assertFalse(Validation.isRightPassport("numb12"));
        assertTrue(Validation.isRightPassport("1234567890"));
    }

    public void testIsRightInt() {
        assertFalse(Validation.isInt("-1111"));
        assertFalse(Validation.isInt("1.1"));
        assertFalse(Validation.isInt("1f"));
        assertTrue(Validation.isInt("1"));
    }
    public void testIsRightFloat() {
        assertFalse(Validation.isFloat("-1111"));
        assertFalse(Validation.isFloat("1f"));
        assertTrue(Validation.isFloat("1.1"));
        assertTrue(Validation.isFloat("1"));
    }
}