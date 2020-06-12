package com.lcy.xingchenmall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lcy.common.utils.PageUtils;
import com.lcy.common.utils.Query;
import com.lcy.xingchenmall.product.dao.CategoryDao;
import com.lcy.xingchenmall.product.entity.CategoryEntity;
import com.lcy.xingchenmall.product.service.CategoryBrandRelationService;
import com.lcy.xingchenmall.product.service.CategoryService;
import com.lcy.xingchenmall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    //    @Autowired
//    CategoryDao categoryDao;
//    private Map<String, Object> cache = new HashMap<>();
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查出所有分类
        List<CategoryEntity> entities = baseMapper.selectList(null);

        //2、组装成父子的树形结构

        //2.1）、找到所有的一级分类
        List<CategoryEntity> level1Menus = entities.stream().filter(categoryEntity ->
                categoryEntity.getParentCid() == 0
        ).map((menu) -> {
            menu.setChildren(getChildrens(menu, entities));
            return menu;
        }).sorted((menu1, menu2) -> {
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());


        return level1Menus;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO  1、检查当前删除的菜单，是否被别的地方引用

        //逻辑删除
        baseMapper.deleteBatchIds(asList);
    }

    //[2,25,225]
    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        Collections.reverse(parentPath);


        return parentPath.toArray(new Long[parentPath.size()]);
    }

    /**
     * 级联更新所有关联的数据
     *
     * @param category
     */
    @Caching(evict = {
            @CacheEvict(value = "category", key = "'getLevel1Categorys'"),
            @CacheEvict(value = "category", key = "'getCatelogJson'")
    })
//    @CacheEvict(value = "category",allEntries = true)
    @CachePut  //双写模式
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCategory(category.getCatId(), category.getName());
    }

    // 每一个需要缓存的数据我们都需要来指定放到哪个名字的缓存【缓存的分区（按照业务类型分）】
    //代表当前方法结果需要缓存，如果缓存中有，方法不用调用，如果缓存中没有，会调用方法，最后将方法的结果放入缓存
    @Cacheable(value = {"category"}, key = "#root.method.name",sync = true)
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        return categoryEntities;
    }

    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson() {
        System.out.println("查询了数据库。。。");
        List<CategoryEntity> selectList = baseMapper.selectList(null);


        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parent_id = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1. 每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2. 封装上面的结果
            List<Catelog2Vo> category2Vo = null;
            if (categoryEntities != null) {
                category2Vo = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1、找当前二级分类的三级分类封装称vo
                    List<CategoryEntity> level3Category = getParent_cid(selectList, l2.getCatId());
                    if (level3Category != null) {
                        //封装成指定格式
                        List<Object> collect = level3Category.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());

            }
            return category2Vo;
        }));
        return parent_id;
    }

    //    @Override
    public Map<String, List<Catelog2Vo>> getCatelogJson2() {
        //给缓存中放json字符串，拿出的json字符串还要逆转为对象类型[序列化和反序列化]

        /**
         * 1、空结果缓存：解决缓存穿透
         * 2、设置过期时间（加随机值）：解决缓存雪崩
         * 3、加锁：解决缓存击穿
         */

        //1、加入缓存逻辑，缓存中存的数据是json字符串
        //JSON跨语言，跨平台兼容
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (StringUtils.isEmpty(catalogJSON)) {
            //2、缓存中没有，查询数据库
            Map<String, List<Catelog2Vo>> catelogJsonFromDB = getCatelogJsonFromDBWithRedisLock();


        }
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    /**
     * 缓存里面的数据如何和数据库保持一致
     * 缓存数据一致性
     * 1、双写模式
     * 2、失效模式
     *
     * @return
     */

    //从数据库查询并封装分类
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDBWithRedissonLock() {

        // 1、锁的名字，涉及到锁的粒度，越细越快
        // 锁的粒度，具体缓存是某个数据
        RLock lock = redisson.getLock("catelogJson-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDb = null;
        try {
            dataFromDb = getDataFromDb();
        } finally {
            lock.unlock();
        }

        return dataFromDb;
    }

    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDBWithRedisLock() {

        // 1、占分布式锁，去Redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", uuid, 30, TimeUnit.SECONDS);
        if (lock) {
            //加锁成功。。。
            // 2、设置过期时间，必须和加锁是同步的
            //redisTemplate.expire("lock",30,TimeUnit.SECONDS);
            Map<String, List<Catelog2Vo>> dataFromDb = null;
            try {
                dataFromDb = getDataFromDb();
                // 获取值对比+对比成功删除=原子操作   lua脚本操作
//            String lockValue = redisTemplate.opsForValue().get("lock");
//            if(uuid.equals(lockValue)){
//                // 删除自己的锁
//                redisTemplate.delete("lock");
//            }
            } finally {
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                // 删除锁
                Long lock1 = redisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }

            return dataFromDb;
        } else {
            //加锁失败。。。重拾
            //休眠100ms重试
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
            return getCatelogJsonFromDBWithRedisLock();//自旋的方式
        }
    }


    private Map<String, List<Catelog2Vo>> getDataFromDb() {
        // 得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续
        String catalogJSON = redisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            // 缓存不为空，直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }
        System.out.println("查询了数据库。。。");
        List<CategoryEntity> selectList = baseMapper.selectList(null);


        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parent_id = level1Categorys.stream().collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
            //1. 每一个的一级分类，查到这个一级分类的二级分类
            List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
            //2. 封装上面的结果
            List<Catelog2Vo> category2Vo = null;
            if (categoryEntities != null) {
                category2Vo = categoryEntities.stream().map(l2 -> {
                    Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName());
                    //1、找当前二级分类的三级分类封装称vo
                    List<CategoryEntity> level3Category = getParent_cid(selectList, l2.getCatId());
                    if (level3Category != null) {
                        //封装成指定格式
                        List<Object> collect = level3Category.stream().map(l3 -> {
                            Catelog2Vo.Catelog3Vo catelog3Vo = new Catelog2Vo.Catelog3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                            return catelog3Vo;
                        }).collect(Collectors.toList());
                        catelog2Vo.setCatalog3List(collect);
                    }
                    return catelog2Vo;
                }).collect(Collectors.toList());

            }
            return category2Vo;
        }));
        //3、查到的数据放入缓存，将对象转为json存在缓存中
        String s = JSON.toJSONString(parent_id);
        redisTemplate.opsForValue().set("catalogJSON", s, 1, TimeUnit.DAYS);
        return parent_id;
    }


    //从数据库查询并封装分类
    public Map<String, List<Catelog2Vo>> getCatelogJsonFromDBWithLocalLock() {
        //1、如果缓存中有，就用缓存的
//        Map<String, List<Catelog2Vo>> catalogJson = (Map<String, List<Catelog2Vo>>) cache.get("catalogJson");
//        if (cache.get("catalogJson") == null) {
//             //调用业务
//              //返回数据放入缓存
//            cache.put("catalogJson", parent_id);
//        }
//        return catalogJson;
        // 只要是同一把锁，就能锁住需要这个锁的所有线程
        // 1、synchronized (this):SpringBoot所有的组件在容器中都是单例的，
        synchronized (this) {
            // 得到锁以后，我们应该再去缓存中确定一次，如果没有才需要继续
            return getDataFromDb();
        }

    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parent_cid) {
        List<CategoryEntity> collect = selectList.stream().filter(item -> item.getParentCid() == parent_cid).collect(Collectors.toList());
//        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", v.getCatId()));
        return collect;
    }

    //225,25,2
    private List<Long> findParentPath(Long catelogId, List<Long> paths) {
        //1、收集当前节点id
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;

    }


    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {

        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return categoryEntity.getParentCid() == root.getCatId();
        }).map(categoryEntity -> {
            //1、找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted((menu1, menu2) -> {
            //2、菜单的排序
            return (menu1.getSort() == null ? 0 : menu1.getSort()) - (menu2.getSort() == null ? 0 : menu2.getSort());
        }).collect(Collectors.toList());

        return children;
    }


}