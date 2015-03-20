package com.cho.chove.core.mybatis;

import javax.annotation.Resource;

import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;

/**
 * Set sqlSessionTemplate for parent Class expend SqlSessionDaoSupport
 * @author ChoHoyoung
 * @since 2015.03.20
 * @version 0.1
 * @see
 *
 * <pre>
 * << Modification Information >>
 *   
 *  UpdateDate  Updator     Description
 *  ----------  ----------  ---------------------------
 *  2014.03.20  ChoHoyoung  initialize
 *
 * </pre>
 */

public abstract class BaseSqlSessionDaoSupport extends SqlSessionDaoSupport {
	
	@Resource(name = "sqlSession")
	public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
		super.setSqlSessionTemplate(sqlSessionTemplate);
	}
}
