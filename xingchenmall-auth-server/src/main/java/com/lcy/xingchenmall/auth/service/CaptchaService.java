package com.lcy.xingchenmall.auth.service;

import java.awt.image.BufferedImage;

public interface CaptchaService {
    BufferedImage getCaptcha(String codeStr);
}
