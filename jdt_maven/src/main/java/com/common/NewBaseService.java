package com.common;

import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.service.BaseService;
import com.baosight.common.utils.RedisUtils;
import com.baosight.xinsight.redis.RedisUtil;
import com.raising.backstage.service.ProjectService;
import com.raising.forward.mapper.CodeItemDao;
import com.raising.forward.mapper.DaoUtil;
import com.raising.forward.service.CodeItemService;
import com.raising.forward.service.ProjectForwardService;
import com.raising.forward.service.UserService;
import com.util.NewRedisUtils;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 供service继承
 */
public abstract class NewBaseService {


    @Autowired
    protected RestDao restDao;

    @Qualifier("baseService")
    @Autowired
    protected BaseService baseDao;

    @Autowired
    protected DaoUtil daoUtil;

    @Autowired
    @Qualifier("codeItemDao")
    protected CodeItemDao codeItemDao;

    @Autowired
    protected SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected RedisUtils redisUtils;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected NewRedisUtils newRedisUtils;













}
