package com.study.studymarket.common.util.elasticSearch;

import com.study.studymarket.common.config.ElasticsearchRestClient;
import com.study.studymarket.common.util.DateUtils;
import com.study.studymarket.common.util.elasticSearch.annotations.Document;
import com.study.studymarket.common.util.elasticSearch.exception.SearchQueryBuildException;
import com.study.studymarket.common.util.elasticSearch.model.ScrollId;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

public abstract class SearchQueryEngine<T> {

    @Autowired
    protected ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    protected ElasticsearchRestClient elasticsearchRestClient;


    public abstract int saveOrUpdate(List<T> list);

//    public abstract <R> List<R> aggregation(T query, Class<R> clazz);

    public abstract <R> Page<R> scroll(T query, Class<R> clazz, Pageable pageable, ScrollId scrollId);

    public abstract <R> List<R> find(T query, Class<R> clazz, int size);

    public abstract <R> Page<R> find(T query, Class<R> clazz, Pageable pageable);

//    public abstract <R> R sum(T query, Class<R> clazz);

    protected Document getDocument(T t) {
        Document annotation = t.getClass().getAnnotation(Document.class);
        if (annotation == null) {
            throw new SearchQueryBuildException("Can't find annotation @Document on " + t.getClass().getName());
        }
        return annotation;
    }

    /**
     * 获取字段名，若设置column则返回该值
     *
     * @param field
     * @param column
     * @return
     */
    protected String getFieldName(Field field, String column) {
        return StringUtils.isNotBlank(column) ? column : field.getName();
    }

    /**
     * 设置属性值
     *
     * @param field
     * @param obj
     * @param value
     */
    protected void setFieldValue(Field field, Object obj, Object value) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            switch (field.getType().getSimpleName()) {
                case "BigDecimal":
                    field.set(obj, new BigDecimal(value.toString()).setScale(5, BigDecimal.ROUND_HALF_UP));
                    break;
                case "Long":
                    field.set(obj, new Long(value.toString()));
                    break;
                case "Integer":
                    field.set(obj, new Integer(value.toString()));
                    break;
                case "Date":
                    field.set(obj, new Date(Long.valueOf(value.toString())));
                    break;
                default:
                    field.set(obj, value);
            }
        } catch (IllegalAccessException e) {
            throw new SearchQueryBuildException(e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * 获取字段值
     *
     * @param field
     * @param obj
     * @return
     */
    protected Object getFieldValue(Field field, Object obj) {
        boolean isAccessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new SearchQueryBuildException(e);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * 转换为es识别的value值
     *
     * @param value
     * @return
     */
    protected Object formatValue(Object value) {
        if (value instanceof Date) {
//            return ((Date) value).getTime();
            return DateUtils.formatDateToUTC((Date) value);
        } else {
            return value;
        }
    }


    /**
     * 转换时间为字符串
     *
     *
     * */
    protected Object formatDateToUTC(Object value) throws ParseException {
        if (value instanceof Date) {
            Date date = (Date) value;
            return DateUtils.formatDateToUTC(date);
        } else {
            return value;
        }
    }

    /**
     * 获取索引分区数
     *
     * @param t
     * @return
     */
    protected int getNumberOfShards(T t) {
        return Integer.parseInt(elasticsearchTemplate.getSetting(getDocument(t).index()).get(IndexMetaData.SETTING_NUMBER_OF_SHARDS).toString());
    }
}
