package com.lcy.xingchenmall.member.exception;

public class UsernameExsitException extends RuntimeException{
    public UsernameExsitException() {
        super("手机号存在");
    }
}
