package com.study.studymarket.common.util.elasticSearch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.study.studymarket.common.config.ElasticsearchRestClient;
import com.study.studymarket.common.util.elasticSearch.annotations.*;
import com.study.studymarket.common.util.elasticSearch.enums.Container;
import com.study.studymarket.common.util.elasticSearch.enums.Operator;
import com.study.studymarket.common.util.elasticSearch.exception.SearchQueryBuildException;
import com.study.studymarket.common.util.elasticSearch.model.ScrollId;
import com.study.studymarket.model.document.Unimed;
import com.sun.tools.doclets.internal.toolkit.util.IndexBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ScrolledPage;
import org.springframework.data.elasticsearch.core.query.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static com.study.studymarket.common.util.elasticSearch.enums.Container.*;
import static org.elasticsearch.index.query.QueryBuilders.*;

@Component
@ComponentScan
public class SimpleSearchQueryEngine <T> extends SearchQueryEngine<T> {



    private int numberOfRowsPerScan = 10;
    /**
     * scroll游标快照超时时间，单位ms
     */
    private static final long SCROLL_TIMEOUT = 5000;

    private static RestHighLevelClient client;

    @Override
    public int saveOrUpdate(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return 0;
        }

        T base = list.get(0);
        Field id = null;
        for (Field field : base.getClass().getDeclaredFields()) {
            Id _id = field.getAnnotation(Id.class);
            if (_id != null) {
                id = field;
                break;
            }
        }
        if (id == null) {
            throw new SearchQueryBuildException("Can't find _id on " + base.getClass().getName());
        }

        Document document = getDocument(base);
        List<UpdateQuery> bulkIndex = new ArrayList<>();
        for (T t : list) {
            UpdateQuery updateQuery = new UpdateQuery();
            updateQuery.setIndexName(document.index());
            updateQuery.setType(document.type());
            updateQuery.setId(getFieldValue(id, t).toString());
            updateQuery.setUpdateRequest(new UpdateRequest(updateQuery.getIndexName(), updateQuery.getType(), updateQuery.getId()).doc(JSONObject.toJSONString(t, SerializerFeature.WriteMapNullValue)));
            updateQuery.setDoUpsert(true);
            updateQuery.setClazz(t.getClass());
            bulkIndex.add(updateQuery);
        }
        elasticsearchTemplate.bulkUpdate(bulkIndex);
        return list.size();
    }

//    @Override
//    public <R> List<R> aggregation(T query, Class<R> clazz) {
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildNativeSearchQueryBuilder(query);
//        nativeSearchQueryBuilder.addAggregation(buildGroupBy(query));
//        Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), new ResultsExtractor<Aggregations>() {
//            @Override
//            public Aggregations extract(SearchResponse searchResponse) {
//                return searchResponse.getAggregations();
//            }
//        });
//        try {
//            return transformList(null, aggregations, clazz.newInstance(), new ArrayList());
//        } catch (Exception e) {
//            throw new SearchResultBuildException(e);
//        }
//    }


    /**
     * 添加操作
     *
     * */
    public T add(T model) {
        Document document = getDocument(model);
        Class modelClass = model.getClass();
        Field id = null;
        for (Field field : modelClass.getDeclaredFields()) {
            Id _id = field.getAnnotation(Id.class);
            if (_id != null) {
                id = field;
            }
        }

        if (id == null) {
            throw new SearchQueryBuildException("Can't find _id on " + model.getClass().getName());
        }
        String hostName = elasticsearchRestClient.getHOST_NAME();
        int port = elasticsearchRestClient.getPORT();
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(hostName, port,"http")
                )
        );
        XContentBuilder content = null;
        IndexResponse response = null;
        try {
            content = XContentFactory.jsonBuilder().startObject();
            for (Field field : modelClass.getDeclaredFields()) {
                String name = field.getName();
                if ("id".equals(name)) {
                    continue;
                }
                Object value = getFieldValue(field, model);
                if (value != null) {
                    content.field(name, value);
                }
            }
            IndexRequest request = new IndexRequest(document.index(), document.type());
            request.id(getFieldValue(id, model).toString());
            request.source(content.endObject());
            response = restHighLevelClient.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        IndexQuery indexQuery = new IndexQueryBuilder()
//                .withIndexName(document.index())
//                .withType(document.type())
//                .withId()
//                .withObject(content)
//                .build();
//        elasticsearchTemplate.index(request);

        return null;
    }


    /**
     * 更新
     *
     * */
    public UpdateResponse update(T model, Class<?> clazz){
        Document document = getDocument(model);
        Class modelClass = model.getClass();
        Field id = null;
        for (Field field : modelClass.getDeclaredFields()) {
            Id _id = field.getAnnotation(Id.class);
            if (_id != null) {
                id = field;
            }
        }

        if (id == null) {
            //没传id 返回异常
            throw new SearchQueryBuildException("Can't find _id on " + model.getClass().getName());
        } else {
            // id不为空则执行更新操作
            UpdateRequest updateRequest = new UpdateRequest();
            XContentBuilder docDetail = null;
            try {
                docDetail = XContentFactory.jsonBuilder().startObject();
                for (Field field : modelClass.getDeclaredFields()) {
                    String name = field.getName();
                    Object value = getFieldValue(field, model);
                    if (value != null) {
                        docDetail.field(name, value);
                    }
                }
                updateRequest.doc(docDetail.endObject());
            } catch (IOException e) {
                e.printStackTrace();
            }

            UpdateQuery searchQuery = new UpdateQueryBuilder()
                    .withId(getFieldValue(id, model).toString())
                    .withClass(clazz)
                    .withIndexName(document.index())
                    .withType(document.type())
                    .withUpdateRequest(updateRequest).build();
            UpdateResponse result = elasticsearchTemplate.update(searchQuery);
            System.out.println(result.getClass());
            System.out.println(result.getId());
            System.out.println(result.getGetResult());
            System.out.println(result.status());
            System.out.println(result.toString());
            return result ;
        }
    }

    @Override
    public <R> Page<R> scroll(T query, Class<R> clazz, Pageable pageable, ScrollId scrollId) {
        if (pageable.getPageSize() % numberOfRowsPerScan > 0) {
            throw new SearchQueryBuildException("Page size must be an integral multiple of " + numberOfRowsPerScan);
        }
        SearchQuery searchQuery = buildNativeSearchQueryBuilder(query)
                .withPageable(new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort()))
//                .withSort()
                .build();
        ScrolledPage<R> page = null;
        if (StringUtils.isEmpty(scrollId.getValue())) {
            page = (ScrolledPage<R>) elasticsearchTemplate.startScroll(SCROLL_TIMEOUT, searchQuery, clazz);
            scrollId.setValue(page.getScrollId());
        } else {
            page = (ScrolledPage<R>) elasticsearchTemplate.continueScroll(scrollId.getValue(), SCROLL_TIMEOUT, clazz);
        }
        if (page == null || page.getContent().size() == 0) {
            elasticsearchTemplate.clearScroll(scrollId.getValue());
        }
        return page;
    }

    @Override
    public <R> List<R> find(T query, Class<R> clazz, int size) {
        // Caused by: QueryPhaseExecutionException[Result window is too large, from + size must be less than or equal to: [10000] but was [2147483647].
        // See the scroll api for a more efficient way to request large data sets. This limit can be set by changing the [index.max_result_window] index level parameter.]
        if (size % numberOfRowsPerScan > 0) {
            throw new SearchQueryBuildException("Parameter 'size' must be an integral multiple of " + numberOfRowsPerScan);
        }
        int pageNum = 0;
        List<R> result = new ArrayList<>();
        ScrollId scrollId = new ScrollId();
        while (true) {
            Page<R> page = scroll(query, clazz, new PageRequest(pageNum, numberOfRowsPerScan), scrollId);
            if (page != null && page.getContent().size() > 0) {
                result.addAll(page.getContent());
            } else {
                break;
            }
            if (result.size() >= size) {
                break;
            } else {
                pageNum++;
            }
        }
        elasticsearchTemplate.clearScroll(scrollId.getValue());
        return result;
    }

    @Override
    public <R> Page<R> find(T query, Class<R> clazz, Pageable pageable) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildNativeSearchQueryBuilder(query).withPageable(pageable);
        return elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), clazz);
    }

//    @Override
//    public <R> R sum(T query, Class<R> clazz) {
//        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildNativeSearchQueryBuilder(query);
//        for (SumAggregationBuilder sumBuilder : getSumBuilderList(query)) {
//            nativeSearchQueryBuilder.addAggregation(sumBuilder);
//        }
//        Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), new AggregationResultsExtractor());
//        try {
//            return transformSumResult(aggregations, clazz);
//        } catch (Exception e) {
//            throw new SearchResultBuildException(e);
//        }
//    }

    /**
     * 将Aggregations转为List
     *
     * @param terms
     * @param aggregations
     * @param baseObj
     * @param resultList
     * @param <R>
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    private <R> List<R> transformList(Aggregation terms, Aggregations aggregations, R baseObj, List<R> resultList) throws NoSuchFieldException, IllegalAccessException, InstantiationException, InvocationTargetException {
        for (String column : aggregations.asMap().keySet()) {
            Aggregation childAggregation = aggregations.get(column);
            if (childAggregation instanceof InternalSum) {
                // 使用@Sum
                if (!(terms instanceof InternalSum)) {
                    R targetObj = (R) baseObj.getClass().newInstance();
                    BeanUtils.copyProperties(baseObj, targetObj);
                    resultList.add(targetObj);
                }
                setFieldValue(baseObj.getClass().getDeclaredField(column), resultList.get(resultList.size() - 1), ((InternalSum) childAggregation).getValue());
                terms = childAggregation;
            } else {
                Terms childTerms = (Terms) childAggregation;
                for (Terms.Bucket bucket : childTerms.getBuckets()) {
                    if (CollectionUtils.isEmpty(bucket.getAggregations().asList())) {
                        // 未使用@Sum
                        R targetObj = (R) baseObj.getClass().newInstance();
                        BeanUtils.copyProperties(baseObj, targetObj);
                        setFieldValue(targetObj.getClass().getDeclaredField(column), targetObj, bucket.getKey());
                        resultList.add(targetObj);
                    } else {
                        setFieldValue(baseObj.getClass().getDeclaredField(column), baseObj, bucket.getKey());
                        transformList(childTerms, bucket.getAggregations(), baseObj, resultList);
                    }
                }
            }
        }
        return resultList;
    }


    private <R> R transformSumResult(Aggregations aggregations, Class<R> clazz) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        R targetObj = clazz.newInstance();
        for (Aggregation sum : aggregations.asList()) {
            if (sum instanceof InternalSum) {
                setFieldValue(targetObj.getClass().getDeclaredField(sum.getName()), targetObj, ((InternalSum) sum).getValue());
            }
        }
        return targetObj;
    }


    /**
     * 构建query请求
     *
     *
     * */
    private NativeSearchQueryBuilder buildNativeSearchQueryBuilder(T query) {
        Document document = getDocument(query);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withIndices(document.index())
                .withTypes(document.type());

        QueryBuilder whereBuilder = buildBoolQuery(query);
        if (whereBuilder != null) {
            nativeSearchQueryBuilder.withQuery(whereBuilder);
        }
        // sort
        buildSort(nativeSearchQueryBuilder, query);

        return nativeSearchQueryBuilder;
    }


    /**
     * 布尔查询构建
     *
     * @param query
     * @return
     */
    private BoolQueryBuilder buildBoolQuery(T query) {
        BoolQueryBuilder boolQueryBuilder = boolQuery();
        buildMatchQuery(boolQueryBuilder, query); // match 检索
        buildRangeQuery(boolQueryBuilder, query);  // range 查询
        buildTermQuery(boolQueryBuilder, query); // term 检索
        buildExistQuery(boolQueryBuilder, query); // exist 条件
        BoolQueryBuilder queryBuilder = boolQuery().must(boolQueryBuilder);
        return queryBuilder;
    }

    /**
     * and or 查询构建
     *
     * @param boolQueryBuilder
     * @param query
     */
    private void buildMatchQuery(BoolQueryBuilder boolQueryBuilder, T query) {
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            MatchQuery annotation = field.getAnnotation(MatchQuery.class);
            Object value = getFieldValue(field, query);
            if (annotation == null || value == null) {
                continue;
            }
            if (Container.must.equals(annotation.container())) {
                boolQueryBuilder.must(matchQuery(getFieldName(field, annotation.column()), formatValue(value)));
            } else if (should.equals(annotation.container())) {
                // 这里should可以是集合类型，也可以是单个值
                if (value instanceof Collection) {
                    BoolQueryBuilder shouldQueryBuilder = boolQuery();
                    Collection tmp = (Collection) value;
                    for (Object obj : tmp) {
                        shouldQueryBuilder.should(matchQuery(getFieldName(field, annotation.column()), formatValue(obj)));
                    }
                    boolQueryBuilder.must(shouldQueryBuilder);
                } else {
                    boolQueryBuilder.must(boolQuery().should(matchQuery(getFieldName(field, annotation.column()), formatValue(value))));
                }
            } else if (filter.equals(annotation.container())) {
                boolQueryBuilder.filter(matchQuery(getFieldName(field, annotation.column()), formatValue(value)));
            }
        }
    }

    /**
     * and or 查询构建
     *
     * */
    private void buildTermQuery(BoolQueryBuilder boolQueryBuilder, T query) {
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            MatchQuery annotation = field.getAnnotation(MatchQuery.class);
            Object value = getFieldValue(field, query);
            if (annotation == null || value == null) {
                continue;
            }
            if (Container.must.equals(annotation.container())) {
                boolQueryBuilder.must(termQuery(getFieldName(field, annotation.column()), formatValue(value)));
            } else if (should.equals(annotation.container())) {
                // 这里should可以是集合类型，也可以是单个值
                if (value instanceof Collection) {
                    BoolQueryBuilder shouldQueryBuilder = boolQuery();
                    Collection tmp = (Collection) value;
                    for (Object obj : tmp) {
                        shouldQueryBuilder.should(termQuery(getFieldName(field, annotation.column()), formatValue(obj)));
                    }
                    boolQueryBuilder.must(shouldQueryBuilder);
                } else {
                    boolQueryBuilder.must(boolQuery().should(termQuery(getFieldName(field, annotation.column()), formatValue(value))));
                }
            } else if (mustNot.equals(annotation.container())) {
                boolQueryBuilder.mustNot(termQuery(getFieldName(field, annotation.column()), formatValue(value)));
            } else if (filter.equals(annotation.container())) {
                boolQueryBuilder.filter(termQuery(getFieldName(field, annotation.column()), formatValue(value)));
            }
        }
    }


    /**
     * 范围查询构建
     *
     * @param boolQueryBuilder
     * @param query
     */
    private void buildRangeQuery(BoolQueryBuilder boolQueryBuilder, T query) {
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            RangeQuery annotation = field.getAnnotation(RangeQuery.class);
            Object value = getFieldValue(field, query);
            if (annotation == null || value == null) {
                continue;
            }
            if (Operator.gt.equals(annotation.operator())) {
                boolQueryBuilder.must(rangeQuery(getFieldName(field, annotation.column())).gt(formatValue(value)));
            } else if (Operator.gte.equals(annotation.operator())) {
                boolQueryBuilder.must(rangeQuery(getFieldName(field, annotation.column())).gte(formatValue(value)));
            } else if (Operator.lt.equals(annotation.operator())) {
                boolQueryBuilder.must(rangeQuery(getFieldName(field, annotation.column())).lt(formatValue(value)));
            } else if (Operator.lte.equals(annotation.operator())) {
                boolQueryBuilder.must(rangeQuery(getFieldName(field, annotation.column())).lte(formatValue(value)));
            }
        }
    }

    /**
     * exists查询构建
     *
     * @param boolQueryBuilder
     * @param query
     */
    private void buildExistQuery(BoolQueryBuilder boolQueryBuilder, T query) {
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            ExistQuery annotation = field.getAnnotation(ExistQuery.class);
            Object value = getFieldValue(field, query);
            if (annotation == null || value == null) {
                continue;
            }
            if (annotation.isExist()) {
                boolQueryBuilder.must(existsQuery(getFieldName(field, annotation.column())));
            } else {
                boolQueryBuilder.mustNot(existsQuery(getFieldName(field, annotation.column())));
            }
        }
    }

    private void buildSort(NativeSearchQueryBuilder nativeSearchQueryBuilder, T query) {
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Sort sortParam = field.getAnnotation(Sort.class);
            new SortBuilders();
//            for () {
//                SortBuilder sort = SortBuilders.fieldSort().order(sortParam.sortOrder());
//                nativeSearchQueryBuilder.withSort(sort);
//            }
        }
    }

    /**
     * Sum构建
     *
     * @param query
     * @return
     */
    private List<SumAggregationBuilder> getSumBuilderList(T query) {
        List<SumAggregationBuilder> list = new ArrayList<>();
        Class clazz = query.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            Sum annotation = field.getAnnotation(Sum.class);
            if (annotation == null) {
                continue;
            }
            list.add(AggregationBuilders.sum(field.getName()).field(field.getName()));
        }
        if (CollectionUtils.isEmpty(list)) {
            throw new SearchQueryBuildException("Can't find @Sum on " + clazz.getName());
        }
        return list;
    }


//    /**
//     * GroupBy构建
//     *
//     * @param query
//     * @return
//     */
//    private TermsAggregationBuilder buildGroupBy(T query) {
//        List<Field> sumList = new ArrayList<>();
//        Object groupByCollection = null;
//        Class clazz = query.getClass();
//        for (Field field : clazz.getDeclaredFields()) {
//            Sum sumAnnotation = field.getAnnotation(Sum.class);
//            if (sumAnnotation != null) {
//                sumList.add(field);
//            }
//            GroupBy groupByannotation = field.getAnnotation(GroupBy.class);
//            Object value = getFieldValue(field, query);
//            if (groupByannotation == null || value == null) {
//                continue;
//            } else if (!(value instanceof Collection)) {
//                throw new SearchQueryBuildException("GroupBy filed must be collection");
//            } else if (CollectionUtils.isEmpty((Collection<String>) value)) {
//                continue;
//            } else if (groupByCollection != null) {
//                throw new SearchQueryBuildException("Only one @GroupBy is allowed");
//            } else {
//                groupByCollection = value;
//            }
//        }
//        Iterator<String> iterator = ((Collection<String>) groupByCollection).iterator();
//        TermsAggregationBuilder termsBuilder = recursiveAddAggregation(iterator, sumList);
//        return termsBuilder;
//    }

//    /**
//     * 添加Aggregation
//     *
//     * @param iterator
//     * @return
//     */
//    private TermsAggregationBuilder recursiveAddAggregation(Iterator<String> iterator, List<Field> sumList) {
//        String groupBy = iterator.next();
//        TermsAggregationBuilder termsBuilder = AggregationBuilders.terms(groupBy).field(groupBy).size(0);
//        if (iterator.hasNext()) {
//            termsBuilder.subAggregation(recursiveAddAggregation(iterator, sumList));
//        } else {
//            for (Field field : sumList) {
//                termsBuilder.subAggregation(AggregationBuilders.sum(field.getName()).field(field.getName()));
//            }
//            sumList.clear();
//        }
//        return termsBuilder.order(Bucketorder);
//    }

}
