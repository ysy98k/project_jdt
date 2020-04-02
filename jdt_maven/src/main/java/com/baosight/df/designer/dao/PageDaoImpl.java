package com.baosight.df.designer.dao;

import com.baosight.common.basic.dao.BaseDaoImpl;
import com.baosight.df.designer.entity.PageBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Repository("pageDao")
public class PageDaoImpl extends BaseDaoImpl<PageBean> {

	@Resource
	private SqlSessionTemplate sqlSessionTemplate;

	@PostConstruct
	public void init() {
		this.setSqlSession(sqlSessionTemplate);
	}

	public PageDaoImpl() {
		super("com.baosight.xinsight.template.model.pagemanage");

	}

}
