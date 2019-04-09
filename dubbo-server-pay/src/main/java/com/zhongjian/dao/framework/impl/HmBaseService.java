package com.zhongjian.dao.framework.impl;

import com.zhongjian.dao.framework.inf.HmDAO;
import org.apache.log4j.Logger;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


/**
 * 基本的服务层
 * 
 * @author wangyugang
 * @param <T>
 * @param <PK>
 */
public class HmBaseService<T, PK extends Serializable> {

	protected Logger log = Logger.getLogger(this.getClass());

	/**
	 * 得到数据访问对象
	 */
	protected HmDAO<T, PK> dao;

	/**
	 * 自动注入必须写在方法上，否则无法加载命名空间
	 * 
	 * @param dao
	 */
	@Resource
	public void setDao(HmDAO<T, PK> dao) {
		this.dao = dao;
		// 加载dao前缀
		loadDaoNameSpace();
	}

	/**
	 * 向DAO中加载设置prefix属性，即DAO中用到的namespace<br>
	 * 而内容的来源是泛型类T中的一个static方法getNamespace
	 */
	@SuppressWarnings("unchecked")
	private void loadDaoNameSpace() {
		Type t = getClass().getGenericSuperclass();
		// 转换类型成为参数类型
		ParameterizedType p = (ParameterizedType) t;
		// 取得泛型参数的类型的第一个
		Type tp = p.getActualTypeArguments()[0];
		// 转换类型成为class
		Class<T> x = (Class<T>) tp;
		// 向dao中加载前缀   前缀为类名
		this.dao.setPerfix(x.getName());
		
		log.debug("向dao中加载前缀   前缀为类名:" + x.getName());
	}

}
