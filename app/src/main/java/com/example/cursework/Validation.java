package com.example.cursework;

import androidx.annotation.NonNull;

public class Validation {
    public static boolean isStrongLogin(@NonNull String value){
        if(value.length() < 5) return false;
        return true;
    }
    public static boolean isStrongPassword(@NonNull String value){
        if(value.length() < 7) return false;
        if(!value.matches(".*[A-Z].*")) return false;
        if(!value.matches(".*[0-9].*")) return false;
        return true;
    }
    public static boolean isRightName(@NonNull String value) {
        if(value.length() < 1) return false;
        return true;
    }
    public static boolean isRightEmail(@NonNull String value) {
        if(value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) return true;
        else return false;
    }
    public static boolean isRightPhone(@NonNull String value){
        if(value.length() < 11) return false;
        if(!value.matches(".*[0-9].*")) return false;
        return true;
    }
    public static boolean isRightCompanyNumber(@NonNull String value){
        if(value.length() != 13) return false;
        if(!value.matches(".*[0-9].*")) return false;
        return true;
    }
    public static boolean isRightPassport(@NonNull String value){
        if(value.length() != 10) return false;
        if(!value.matches(".*[0-9].*")) return false;
        return true;
    }
    public static boolean isInt(String value){
        if(!value.matches("^[0-9]+$")) return false;
        return true;
    }
    public static boolean isFloat(String value){
        if(!value.matches("^[0-9]*[.,]?[0-9]+$")) return false;
        return true;
    }
}
