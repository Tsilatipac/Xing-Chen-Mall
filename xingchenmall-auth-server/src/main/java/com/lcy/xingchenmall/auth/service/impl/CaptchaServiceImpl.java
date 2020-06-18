package com.lcy.xingchenmall.auth.service.impl;

import com.lcy.xingchenmall.auth.service.CaptchaService;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Override
    public BufferedImage getCaptcha(String codeStr) {
        // 创建一张图片
        // 单位：像素
        BufferedImage image = new BufferedImage(200, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D gra = image.createGraphics();
        gra.setColor(Color.WHITE);
        gra.fillRect(0, 0, 200, 100);

//        List<Integer> randList = new ArrayList<Integer>();
        Random random = new Random();
//        for (int i = 0; i < 4; i++) {
//            randList.add(random.nextInt(10));
//        }
        List<Character> randList= new ArrayList<>();
        char[] chars = codeStr.toCharArray();
        for (char c : chars) {
            randList.add(c);
        }
        gra.setColor(Color.BLACK);
        gra.setFont(new Font("宋体", Font.BOLD | Font.BOLD, 40));
        Color[] colors = new Color[] { Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN, Color.PINK, Color.GRAY };
        for (int i = 0; i < randList.size(); i++) {
            gra.setColor(colors[random.nextInt(colors.length)]);
            gra.drawString(randList.get(i) + "", i * 40, 70 + (random.nextInt(21) - 10));
        }
        for (int i = 0; i < 2; i++) {
            gra.setColor(colors[random.nextInt(colors.length)]);
            gra.drawLine(0, random.nextInt(101), 200, random.nextInt(101));
        }
        String code = ""+randList.get(0)+randList.get(1)+randList.get(2)+randList.get(3);
        return image;
    }
}
