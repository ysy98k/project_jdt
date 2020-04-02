package com.baosight.df.designer.dao;

import com.baosight.common.basic.dao.BaseDaoImpl;
import com.baosight.df.designer.entity.DesignPageBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Repository("designPageDao")
public class DesignPageDaoImpl extends BaseDaoImpl<DesignPageBean> {
	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	@PostConstruct
	public void init() {
		this.setSqlSession(sqlSessionTemplate);
	}

	public DesignPageDaoImpl() {
		super("com.baosight.xinsight.template.model.designpage");

	}

}
