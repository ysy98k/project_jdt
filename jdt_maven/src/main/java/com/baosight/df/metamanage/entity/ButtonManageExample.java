package com.baosight.df.metamanage.entity;

import java.util.ArrayList;
import java.util.List;

public class ButtonManageExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected String tenant;

    protected int offset = -1;

    protected int limit = -1;

    public ButtonManageExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andButtonIdIsNull() {
            addCriterion("button_id is null");
            return (Criteria) this;
        }

        public Criteria andButtonIdIsNotNull() {
            addCriterion("button_id is not null");
            return (Criteria) this;
        }

        public Criteria andButtonIdEqualTo(Integer value) {
            addCriterion("button_id =", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdNotEqualTo(Integer value) {
            addCriterion("button_id <>", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdGreaterThan(Integer value) {
            addCriterion("button_id >", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("button_id >=", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdLessThan(Integer value) {
            addCriterion("button_id <", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdLessThanOrEqualTo(Integer value) {
            addCriterion("button_id <=", value, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdIn(List<Integer> values) {
            addCriterion("button_id in", values, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdNotIn(List<Integer> values) {
            addCriterion("button_id not in", values, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdBetween(Integer value1, Integer value2) {
            addCriterion("button_id between", value1, value2, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonIdNotBetween(Integer value1, Integer value2) {
            addCriterion("button_id not between", value1, value2, "buttonId");
            return (Criteria) this;
        }

        public Criteria andButtonNameIsNull() {
            addCriterion("button_name is null");
            return (Criteria) this;
        }

        public Criteria andButtonNameIsNotNull() {
            addCriterion("button_name is not null");
            return (Criteria) this;
        }

        public Criteria andButtonNameEqualTo(String value) {
            addCriterion("button_name =", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotEqualTo(String value) {
            addCriterion("button_name <>", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameGreaterThan(String value) {
            addCriterion("button_name >", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameGreaterThanOrEqualTo(String value) {
            addCriterion("button_name >=", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLessThan(String value) {
            addCriterion("button_name <", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLessThanOrEqualTo(String value) {
            addCriterion("button_name <=", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameLike(String value) {
            addCriterion("button_name like", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotLike(String value) {
            addCriterion("button_name not like", value, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameIn(List<String> values) {
            addCriterion("button_name in", values, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotIn(List<String> values) {
            addCriterion("button_name not in", values, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameBetween(String value1, String value2) {
            addCriterion("button_name between", value1, value2, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonNameNotBetween(String value1, String value2) {
            addCriterion("button_name not between", value1, value2, "buttonName");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameIsNull() {
            addCriterion("button_displayname is null");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameIsNotNull() {
            addCriterion("button_displayname is not null");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameEqualTo(String value) {
            addCriterion("button_displayname =", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameNotEqualTo(String value) {
            addCriterion("button_displayname <>", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameGreaterThan(String value) {
            addCriterion("button_displayname >", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameGreaterThanOrEqualTo(String value) {
            addCriterion("button_displayname >=", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameLessThan(String value) {
            addCriterion("button_displayname <", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameLessThanOrEqualTo(String value) {
            addCriterion("button_displayname <=", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameLike(String value) {
            addCriterion("button_displayname like", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameNotLike(String value) {
            addCriterion("button_displayname not like", value, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameIn(List<String> values) {
            addCriterion("button_displayname in", values, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameNotIn(List<String> values) {
            addCriterion("button_displayname not in", values, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameBetween(String value1, String value2) {
            addCriterion("button_displayname between", value1, value2, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andButtonDisplaynameNotBetween(String value1, String value2) {
            addCriterion("button_displayname not between", value1, value2, "buttonDisplayname");
            return (Criteria) this;
        }

        public Criteria andPageIdIsNull() {
            addCriterion("buttonmanage.page_id is null");
            return (Criteria) this;
        }

        public Criteria andPageIdIsNotNull() {
            addCriterion("buttonmanage.page_id is not null");
            return (Criteria) this;
        }

        public Criteria andPageIdEqualTo(Integer value) {
            addCriterion("buttonmanage.page_id =", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdNotEqualTo(Integer value) {
            addCriterion("buttonmanage.page_id <>", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdGreaterThan(Integer value) {
            addCriterion("buttonmanage.page_id >", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("buttonmanage.page_id >=", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdLessThan(Integer value) {
            addCriterion("buttonmanage.page_id <", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdLessThanOrEqualTo(Integer value) {
            addCriterion("buttonmanage.page_id <=", value, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdIn(List<Integer> values) {
            addCriterion("buttonmanage.page_id in", values, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdNotIn(List<Integer> values) {
            addCriterion("buttonmanage.page_id not in", values, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdBetween(Integer value1, Integer value2) {
            addCriterion("buttonmanage.page_id between", value1, value2, "pageId");
            return (Criteria) this;
        }

        public Criteria andPageIdNotBetween(Integer value1, Integer value2) {
            addCriterion("buttonmanage.page_id not between", value1, value2, "pageId");
            return (Criteria) this;
        }

        // page_ename
        public Criteria andPageEnameIsNull() {
            addCriterion("page_ename is null");
            return (Criteria) this;
        }

        public Criteria andPageEnameIsNotNull() {
            addCriterion("page_ename is not null");
            return (Criteria) this;
        }

        public Criteria andPageEnameEqualTo(String value) {
            addCriterion("page_ename =", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameNotEqualTo(String value) {
            addCriterion("page_ename <>", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameGreaterThan(String value) {
            addCriterion("page_ename >", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameGreaterThanOrEqualTo(String value) {
            addCriterion("page_ename >=", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameLessThan(String value) {
            addCriterion("page_ename <", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameLessThanOrEqualTo(String value) {
            addCriterion("page_ename <=", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameLike(String value) {
            addCriterion("page_ename like", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameNotLike(String value) {
            addCriterion("page_ename not like", value, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameIn(List<String> values) {
            addCriterion("page_ename in", values, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameNotIn(List<String> values) {
            addCriterion("page_ename not in", values, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameBetween(String value1, String value2) {
            addCriterion("page_ename between", value1, value2, "PageEname");
            return (Criteria) this;
        }

        public Criteria andPageEnameNotBetween(String value1, String value2) {
            addCriterion("page_ename not between", value1, value2, "PageEname");
            return (Criteria) this;
        }
        
        // page_cname

        public Criteria andPageCnameIsNull() {
            addCriterion("page_cname is null");
            return (Criteria) this;
        }

        public Criteria andPageCnameIsNotNull() {
            addCriterion("page_cname is not null");
            return (Criteria) this;
        }

        public Criteria andPageCnameEqualTo(String value) {
            addCriterion("page_cname =", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameNotEqualTo(String value) {
            addCriterion("page_cname <>", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameGreaterThan(String value) {
            addCriterion("page_cname >", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameGreaterThanOrEqualTo(String value) {
            addCriterion("page_cname >=", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameLessThan(String value) {
            addCriterion("page_cname <", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameLessThanOrEqualTo(String value) {
            addCriterion("page_cname <=", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameLike(String value) {
            addCriterion("page_cname like", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameNotLike(String value) {
            addCriterion("page_cname not like", value, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameIn(List<String> values) {
            addCriterion("page_cname in", values, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameNotIn(List<String> values) {
            addCriterion("page_cname not in", values, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameBetween(String value1, String value2) {
            addCriterion("page_cname between", value1, value2, "PageCname");
            return (Criteria) this;
        }

        public Criteria andPageCnameNotBetween(String value1, String value2) {
            addCriterion("page_cname not between", value1, value2, "PageCname");
            return (Criteria) this;
        }

        // page_path

        public Criteria andPagePathIsNull() {
            addCriterion("page_path is null");
            return (Criteria) this;
        }

        public Criteria andPagePathIsNotNull() {
            addCriterion("page_path is not null");
            return (Criteria) this;
        }

        public Criteria andPagePathEqualTo(String value) {
            addCriterion("page_path =", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathNotEqualTo(String value) {
            addCriterion("page_path <>", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathGreaterThan(String value) {
            addCriterion("page_path >", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathGreaterThanOrEqualTo(String value) {
            addCriterion("page_path >=", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathLessThan(String value) {
            addCriterion("page_path <", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathLessThanOrEqualTo(String value) {
            addCriterion("page_path <=", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathLike(String value) {
            addCriterion("page_path like", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathNotLike(String value) {
            addCriterion("page_path not like", value, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathIn(List<String> values) {
            addCriterion("page_path in", values, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathNotIn(List<String> values) {
            addCriterion("page_path not in", values, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathBetween(String value1, String value2) {
            addCriterion("page_path between", value1, value2, "PagePath");
            return (Criteria) this;
        }

        public Criteria andPagePathNotBetween(String value1, String value2) {
            addCriterion("page_path not between", value1, value2, "PagePath");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}