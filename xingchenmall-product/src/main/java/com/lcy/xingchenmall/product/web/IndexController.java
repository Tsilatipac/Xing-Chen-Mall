package com.lcy.xingchenmall.product.web;

import com.lcy.xingchenmall.product.entity.CategoryEntity;
import com.lcy.xingchenmall.product.service.CategoryService;
import com.lcy.xingchenmall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class IndexController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    RedissonClient redisson;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @GetMapping({"/", "/index.html"})
    public String indexPage(Model model) {
        //TODO 1、查出所有的1级分类
        List<CategoryEntity> categoryEntities = categoryService.getLevel1Categorys();
        model.addAttribute("categorys", categoryEntities);
        return "index";
    }

    @ResponseBody
    @GetMapping("/index/catalog.json")
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        Map<String, List<Catelog2Vo>> catelogJson = categoryService.getCatelogJson();
        return catelogJson;
    }

    @ResponseBody
    @GetMapping("/hello")
    public String hello() {
        // 1、获取一把锁，只要锁的名字一样，就是一把锁
        RLock lock = redisson.getLock("lock");

        // 2、加锁
        lock.lock(); //阻塞式等待，默认的加锁是30s时间
        //1）、锁的自动续期，如果业务时间超长，运行期间自动给锁续上新的30s时间，不用担心业务时间长，锁自动过期会被删掉
        //2）、加锁的业务只要运行完成，就不会给当前业务续期，即使不手动解锁，锁默认在30s以后删除
//        lock.lock(10, TimeUnit.SECONDS); //10s自动解锁，自动解锁时间一定要大于业务的执行时间
        // 问题：lock.lock(10, TimeUnit.SECONDS); 在锁时间到了以后，不会自动续期
        // 1. 如果我们传递了锁的超时时间，就发送给redis执行脚本，进行占锁，默认超时就是我们指定的时间
        // 2. 如果我们没有传递锁的超时时间，就使用30*1000【watchdogTimeout看门狗的默认时间】
        // 只要占锁成功，就会启动一个定时任务【重新给锁设置过期时间，新的过期时间就是看门狗的默认时间】
        // 每隔1/3的看门狗时间，就会自动续期，续成满时间
        // 最佳实战
        // 1、lock.lock(10, TimeUnit.SECONDS); 省掉了续期操作，手动解锁
        try {
            System.out.println("加锁成功，执行业务。。。 " + Thread.currentThread().getId());
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            //3）、解锁
            System.out.println("释放锁。。。" + Thread.currentThread().getId());
            lock.unlock();
        }
        return "Hello,CYLee";
    }

//     保证一定能读取到最新数据，修改期间，写锁是一个排他锁（互斥锁、独享锁），读锁是一个共享锁
//     写锁没释放读锁就必须等待
//     读+读，相当于无锁，并发读，只会在当前读redis中记录好，所有当前的读锁，他们都会同时加锁成功
//     写+读，等待写锁释放
//     写+写，阻塞方式
//     读+写，有读锁写也需要等待
//     只要有写的存在，都必须等待
    @GetMapping("/write")
    @ResponseBody
    public String writeValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = null;
            RLock rLock = lock.writeLock();
        try {
            // 1、改数据加写锁，读数据加读锁
            rLock.lock();
            s = UUID.randomUUID().toString();
            Thread.sleep(30000);
            stringRedisTemplate.opsForValue().set("writeValue",s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }

    @GetMapping("/read")
    @ResponseBody
    public String readValue() {
        RReadWriteLock lock = redisson.getReadWriteLock("rw-lock");
        String s = null;
        RLock rLock = lock.readLock();
        rLock.lock();
        try {
            s= stringRedisTemplate.opsForValue().get("writeValue");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rLock.unlock();
        }
        return s;
    }


}
