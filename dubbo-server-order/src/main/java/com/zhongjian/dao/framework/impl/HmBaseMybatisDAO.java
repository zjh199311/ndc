package com.zhongjian.dao.framework.impl;

import com.zhongjian.dto.Page;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 基本DAO层操作抽象父类
 *
 * @param <T>实体
 * @param <PK>主键类型
 * @author wangyugang
 * @version 1.0
 * @pdOid ae9bf0db-4d74-4275-89a7-c51200f9ee02
 */
public abstract class HmBaseMybatisDAO<T, PK extends Serializable> extends SqlSessionDaoSupport {

    /**
     * 设置sqlSessionFactory
     */
    @Override
    @Autowired
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        super.setSqlSessionFactory(sqlSessionFactory);
    }

    /**
     * 新增数据动作
     *
     * @param statement
     * @param entity
     */
    protected Integer create(String statement, T entity) {
        return getSqlSession().insert(statement, entity);
    }

    /**
     * 根据ID列表进行删除，可以删除一个或多个
     *
     * @param statement
     * @param
     * @return
     */
    protected Integer delete(String statement, PK id) {
        return getSqlSession().delete(statement, id);
    }

    /**
     * 更新数据
     *
     * @param statement
     * @param entity
     */
    protected Integer edit(String statement, T entity) {
        return getSqlSession().update(statement, entity);
    }

    /**
     * 根据ID进行查询实体的一个实例
     *
     * @param statement
     * @param id
     * @return
     */
    protected T getEntityById(String statement, PK id) {
        return getSqlSession().selectOne(statement, id);
    }

    /**
     * 根据实体参数进行查询
     *
     * @param statement
     * @param pagestatement
     * @param page
     * @param entity
     * @return
     */
    protected List<T> searchEntityList(String statement, String pagestatement, Page page, T entity) {
        if (null == page) {
            // 不需要分页
            return getSqlSession().selectList(statement, entity);
        } else {// 需要分页
            // 得到总记录数
            Integer totalRows = count(pagestatement, entity);
            page.setTotalRows(totalRows);
            // 分页查询
            // 分页计算
            page.init(totalRows, page.getPageSize(), page.getCurrentPage());
            RowBounds rowBounds = null;
            if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
                if (page.isFlag()) {
                    rowBounds = new RowBounds(page.getOffset1(), page.getLimit1());
                } else {
                    rowBounds = new RowBounds(page.getPageSize() * (page.getCurrentPage() - 1), page.getPageSize());
                }
            }
            return getSqlSession().selectList(statement, entity, rowBounds);
        }
    }

    /**
     * 根据实体进行count
     *
     * @param statement
     * @param entity
     * @return
     */
    protected Integer count(String statement, T entity) {
        return getSqlSession().selectOne(statement, entity);
    }

    protected int executeInsertMethodImpl(Object param, String statement) {
        return getSqlSession().insert(statement, param);
    }

    protected int executeUpdateMethodImpl(Object param, String statement) {
        return getSqlSession().update(statement, param);

    }

    protected int executeDeleteMethodImpl(Object param, String statement) {
        return getSqlSession().delete(statement, param);
    }

    protected <R> R executeSelectOneMethodImpl(Object param, String statement, Class<R> r) {
        return getSqlSession().selectOne(statement, param);
    }

    protected Integer countEntityList(Object param, String statement) {
        return getSqlSession().selectOne(statement, param);
    }

    protected <R> List<R> executeListMethodImpl(Object param, String statement, Page page, String pageStatement,
                                                Class<R> r) {
        if (null == page) {
            // 不需要分页
            return getSqlSession().selectList(statement, param);
        } else {// 需要分页
            // 得到总记录数
            int totalRows = getSqlSession().selectOne(pageStatement, param);
            page.setTotalRows(totalRows);
            // 分页计算
            page.init(totalRows, page.getPageSize(), page.getCurrentPage());
            RowBounds rowBounds = null;
            if (rowBounds == null || rowBounds == RowBounds.DEFAULT) {
                if (page.isFlag()) {
                    rowBounds = new RowBounds(page.getOffset1(), page.getLimit1());
                } else {
                    rowBounds = new RowBounds(page.getPageSize() * (page.getCurrentPage() - 1), page.getPageSize());
                }
            }
            // 分页查询
            return getSqlSession().selectList(statement, param, rowBounds);
        }

    }

    /**
     * 内部分页查询
     *
     * @param param
     * @param statement
     * @param page
     * @param pageStatement
     * @param r
     * @return
     * @throws Exception
     */
    protected <R> List<R> executeListMethodInsidePageImpl(Object param, String statement, Page page, String pageStatement, Class<R> r) {
        if (null == page) {
            return null;
        } else {
            // 得到总记录数
            int totalRows = getSqlSession().selectOne(pageStatement, param);
            page.setTotalRows(totalRows);
            // 分页计算
            page.init(totalRows, page.getPageSize(), page.getCurrentPage());
            //不使用mybatis的分页查询
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (HashMap<String, Object>) param;
            if (page.isFlag()) {
                map.put("_start", page.getOffset1());
                map.put("_size", page.getLimit1());
            } else {
                map.put("_start", page.getPageSize()
                        * (page.getCurrentPage() - 1));
                map.put("_size", page.getPageSize());
            }
            // 查询
            try {
                return getSqlSession().selectList(statement, param);
            } catch (Exception e) {
                return null;
            }
        }


    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) {
            return null;
        }
        Object obj = beanClass.newInstance();

        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor property : propertyDescriptors) {
            Method setter = property.getWriteMethod();
            if (setter != null) {
                setter.invoke(obj, map.get(property.getName()));
            }
        }
        return obj;
    }

    public int executeDeleteMethod(Object param, String statement) {
        // TODO Auto-generated method stub
        return 0;
    }

}
