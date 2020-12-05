package utils;

import org.apache.commons.lang3.StringUtils;

public class IllegalCharInNameHandler {

    private final String[] illegalChars = { "\\","/","：",":","，","*","?","\"","<",">","|"};
    private final String[] replaceChars = {"_","_","_","_","_","_","_","_","_","_","_"};

    public String legalizeName(String preName){
        return  StringUtils.replaceEach(preName,illegalChars,replaceChars);
    }
}
