package com.bilgeadam.utility;

import java.util.UUID;

public class CodeGenerator {
    public static String genarateCode(){
        String code= UUID.randomUUID().toString();
              String [] data =code.split("-"); // Bu String array donecek
              String newCode="";
              for(String  string :data){
                 newCode+=string.charAt(0);
              }
            return newCode;
    }
}
