package com.lcy.xingchenmall.member.exception;

public class PhoneExsitException extends RuntimeException{
    public PhoneExsitException() {
        super("用户名存在");
    }
}
