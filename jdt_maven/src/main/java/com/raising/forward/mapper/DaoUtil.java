package com.raising.forward.mapper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baosight.common.basic.entity.BaseEntity;
import com.baosight.common.basic.entity.GridData;
import com.baosight.common.utils.JsonUtils;
import com.baosight.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raising.forward.controller.tbmManage.FaultTreatController;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据库操作工具类。
 * 仿BaseService类
 * 操作数据库时出现异常会抛出异常，以便spring事务回滚。
 */
@Service
public class DaoUtil {

    private static final Logger logger = LoggerFactory.getLogger(DaoUtil.class);

    public static final String QUERY_BLOCK = "queryBlock";
    public static final String QUERY_SQL = "querySql";
    public static final String COUNT_SQL = "countSql";
    public static final String INSERT_SQL = "insertSql";
    public static final String UPDATE_SQL = "updateSql";
    public static final String DELETE_SQL = "deleteSql";
    public static final String RESULT_BLOCK = "resultBlock";
    public static final String RESULT_Row = "resultRow";
    public static final String DAO_ENTITY = "daoEntity";
    protected SqlSessionTemplate sqlSessionTemplate;
    protected JSONObject primaryKey;

    public DaoUtil(){}

    public JSONObject getPrimaryKey() {
        return this.primaryKey;
    }

    public JSONObject query(JSONObject paramInfo){
        HashMap queryMap = (HashMap)this.getQueryKey(paramInfo);
        String querySql = paramInfo.getString("querySql");
        String countSql = paramInfo.getString("countSql");
        String classStr = paramInfo.getString("daoEntity");

        BaseEntity daoEntity;
        try {
            Class classObj = Class.forName(classStr);
            daoEntity = (BaseEntity)classObj.newInstance();
        } catch (Exception var14) {
            logger.error(var14.getMessage());
            var14.printStackTrace();
            daoEntity = new BaseEntity();
        }

        daoEntity.fromMap(queryMap);
        if (querySql == null) {
            paramInfo.put("status", "-1");
            paramInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
            return paramInfo;
        } else {
            Integer curPage = (Integer)paramInfo.get("curPage");
            Integer curRowNum = (Integer)paramInfo.get("curRowNum");

            PageInfo pageResult;
            try {
                if (curPage == null || curRowNum == null) {
                    curRowNum = 1000;
                    curPage = 1;
                }

                PageHelper.startPage(curPage, curRowNum);
                List<JSONObject> list = this.sqlSessionTemplate.selectList(querySql, daoEntity, new RowBounds((curPage - 1) * curRowNum, curRowNum));
                pageResult = new PageInfo(list);
                pageResult.setList(list);
            } catch (Exception var16) {
                logger.error(var16.getMessage());
                paramInfo.put("status", "-1");
                paramInfo.put("returnMsg", "查询出错，请检查SQL语句！");
                return paramInfo;

            }
            Object generalArr = new ArrayList();
            try {
                if (countSql != null) {
                    generalArr = this.sqlSessionTemplate.selectList(countSql, daoEntity);
                }
            } catch (Exception var15) {
                logger.error(var15.getMessage());
                paramInfo.put("status", "-1");
                paramInfo.put("returnMsg", "查询出错，请检查SQL语句！");
                return paramInfo;
            }

            int generalSize = 0;
            if (generalArr != null && ((List)generalArr).size() > 0) {
                generalSize = (Integer)((List)generalArr).get(0);
            }

            GridData outGridSource = new GridData(generalSize, pageResult.getList());
            outGridSource.fillGridAttribute(paramInfo);
            JSONObject outJSONObj = JSONObject.parseObject(JSON.toJSONString(outGridSource));
            outJSONObj.put("status", "0");
            outJSONObj.put("returnMsg", "查询成功！本次返回" + pageResult.getList().size() + "条记录，总共" + generalSize + "条记录！");
            return outJSONObj;
        }
    }

    public JSONObject insert(JSONObject paramInfo) throws Exception {
        HashMap queryMap = (HashMap)this.getQueryKey(paramInfo);
        String insertSql = paramInfo.getString("insertSql");
        String resultBlockName = paramInfo.getString("resultBlock");
        String classStr = paramInfo.getString("daoEntity");

        BaseEntity daoEntity;
        try {
            Class classObj = Class.forName(classStr);
            daoEntity = (BaseEntity)classObj.newInstance();
        } catch (Exception var15) {
            logger.error(var15.getMessage());
            var15.printStackTrace();
            daoEntity = new BaseEntity();
        }

        if (insertSql != null && resultBlockName != null) {
            JSONObject resultBlock = (JSONObject)paramInfo.get(resultBlockName);
            List insertRows = resultBlock.getJSONArray("resultRow");
            if (insertRows != null && insertRows.size() != 0) {
                for(int i = 0; i < insertRows.size(); ++i) {
                    daoEntity.fromMap(queryMap);
                    JSONObject tempRow = (JSONObject)insertRows.get(i);
                    HashMap rowInsert = JsonUtils.toHashMap(tempRow);
                    daoEntity.fromMap(rowInsert);

                    Object keyValue;
                    String keyDesc;
                    try {
                        this.sqlSessionTemplate.insert(insertSql, daoEntity);
                    } catch (DuplicateKeyException var16) {
                        logger.error(var16.getMessage());
                        var16.printStackTrace();
                        if (null == this.primaryKey) {
                            throw new Exception("插入出错，主键冲突，请修改后重新操作！");
                        } else {
                            keyValue = tempRow.get(this.primaryKey.getString("key"));
                            keyDesc = this.primaryKey.getString("keyDesc");
                            throw new Exception("对象：" + keyDesc + "；值：" + StringUtils.toString(keyValue) + "——插入出错，主键冲突，请修改后重新操作！");
                        }
                    } catch (Exception var17) {
                        logger.error(var17.getMessage());
                        var17.printStackTrace();
                        if (null == this.primaryKey) {
                            throw new Exception("插入出错，主键冲突，请修改后重新操作！");
                        } else {
                            keyValue = tempRow.get(this.primaryKey.getString("key"));
                            keyDesc = this.primaryKey.getString("keyDesc");
                            throw new Exception("对象：" + keyDesc + "；值：" + StringUtils.toString(keyValue) + "——插入出错，记录参数有误，请修改后重新操作！");

                        }
                    }
                }

                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "插入了" + insertRows.size() + "条记录！");
                return paramInfo;
            } else {
                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "插入了0条记录！");
                return paramInfo;
            }
        } else {
            paramInfo.put("status", "-1");
            paramInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
            return paramInfo;
        }
    }

    public JSONObject delete(JSONObject paramInfo) throws Exception {
        int operator = 0;
        HashMap queryMap = (HashMap)this.getQueryKey(paramInfo);
        String deleteSql = paramInfo.getString("deleteSql");
        String resultBlockName = paramInfo.getString("resultBlock");
        String classStr = paramInfo.getString("daoEntity");

        BaseEntity daoEntity;
        try {
            Class classObj = Class.forName(classStr);
            daoEntity = (BaseEntity)classObj.newInstance();
        } catch (Exception var16) {
            logger.error(var16.getMessage());
            var16.printStackTrace();
            daoEntity = new BaseEntity();
        }

        if (deleteSql != null && resultBlockName != null) {
            JSONObject resultBlock = (JSONObject)paramInfo.get(resultBlockName);
            List deleteRows = resultBlock.getJSONArray("resultRow");
            if (deleteRows != null && deleteRows.size() != 0) {
                for(int i = 0; i < deleteRows.size(); ++i) {
                    daoEntity.fromMap(queryMap);
                    JSONObject tempRow = (JSONObject)deleteRows.get(i);
                    HashMap rowDelete = JsonUtils.toHashMap(tempRow);
                    daoEntity.fromMap(rowDelete);
                    try {
                        int operatorInt = this.sqlSessionTemplate.delete(deleteSql, daoEntity);
                        operator += operatorInt;
                    } catch (Exception var17) {
                        logger.error(var17.getMessage());
                        var17.printStackTrace();
                        if (null == this.primaryKey) {
                            throw new Exception("删除出错，此记录可能被其它项目所引用，请联系管理员进行删除！");
                        } else {
                            Object keyValue = tempRow.get(this.primaryKey.getString("key"));
                            String keyDesc = this.primaryKey.getString("keyDesc");
                            throw new Exception("对象：" + keyDesc + "；值：" + StringUtils.toString(keyValue) + "——删除出错，此记录可能被其它项目所引用，请联系管理员进行删除！");

                        }
                    }
                }

                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "删除了" + operator + "条记录！");
                return paramInfo;
            } else {
                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "删除了0条记录！");
                return paramInfo;
            }
        } else {
            paramInfo.put("status", "-1");
            paramInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
            return paramInfo;
        }
    }

    public JSONObject update(JSONObject paramInfo) throws Exception {
        int operator = 0;
        HashMap queryMap = (HashMap)this.getQueryKey(paramInfo);
        String updateSql = paramInfo.getString("updateSql");
        String resultBlockName = paramInfo.getString("resultBlock");
        String classStr = paramInfo.getString("daoEntity");

        BaseEntity daoEntity;
        try {
            Class classObj = Class.forName(classStr);
            daoEntity = (BaseEntity)classObj.newInstance();
        } catch (Exception var16) {
            logger.error(var16.getMessage());
            var16.printStackTrace();
            daoEntity = new BaseEntity();
        }

        if (updateSql != null && resultBlockName != null) {
            JSONObject resultBlock = (JSONObject)paramInfo.get(resultBlockName);
            List updateRows = resultBlock.getJSONArray("resultRow");
            if (updateRows != null && updateRows.size() != 0) {
                for(int i = 0; i < updateRows.size(); ++i) {
                    daoEntity.fromMap(queryMap);
                    JSONObject tempRow = (JSONObject)updateRows.get(i);
                    HashMap mapRow = JsonUtils.toHashMap(tempRow);
                    daoEntity.fromMap(mapRow);
                    try {
                        int operatorInt = this.sqlSessionTemplate.update(updateSql, daoEntity);
                        operator += operatorInt;
                    } catch (Exception var17) {
                        logger.error(var17.getMessage());
                        var17.printStackTrace();
                        if (null == this.primaryKey) {
                            paramInfo.put("returnMsg", "更新出错，记录参数有误，请修改后重新操作！");
                            throw new Exception("更新出错，记录参数有误，请修改后重新操作！");

                        } else {
                            Object keyValue = tempRow.get(this.primaryKey.getString("key"));
                            String keyDesc = this.primaryKey.getString("keyDesc");
                            throw new Exception("对象：" + keyDesc + "；值：" + StringUtils.toString(keyValue) + "——更新出错，记录参数有误，请修改后重新操作！");
                        }
                    }
                }
                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "更新了" + operator + "条记录！");
                return paramInfo;
            } else {
                paramInfo.put("status", "0");
                paramInfo.put("returnMsg", "更新了0条记录！");
                return paramInfo;
            }
        } else {
            paramInfo.put("status", "-1");
            paramInfo.put("returnMsg", "传入参数传递有误，请重新尝试！");
            return paramInfo;
        }
    }

    public Map getQueryKey(JSONObject paramInfo) {
        HashMap queryMap = new HashMap();
        String queryBlockName = (String)paramInfo.get("queryBlock");
        JSONObject queryBlock = (JSONObject)paramInfo.get(queryBlockName);
        if (queryBlock == null) {
            queryBlock = new JSONObject();
        }

        Set keySet = queryBlock.keySet();
        Iterator iterator = keySet.iterator();

        while(iterator.hasNext()) {
            Object key = iterator.next();
            Object value = queryBlock.get(key);
            if (key != null) {
                queryMap.put(key, value);
            }
        }

        queryMap.put("fieldName", paramInfo.getString("fieldName"));
        queryMap.put("ascDesc", paramInfo.getString("ascDesc"));
        queryMap.put("tenant", "raising");
        return queryMap;
    }

    public SqlSessionTemplate getSqlSessionTemplate() {
        return this.sqlSessionTemplate;
    }

    @Resource
    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }
}
