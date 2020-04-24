package com.study.studymarket;

import com.study.studymarket.common.util.DateUtils;
import com.study.studymarket.common.util.elasticSearch.SimpleSearchQueryEngine;
import com.study.studymarket.common.util.elasticSearch.model.ScrollId;
import com.study.studymarket.dao.UnimedRepository;
import com.study.studymarket.model.document.Unimed;
import com.study.studymarket.model.document.UnimedQuery;
import com.study.studymarket.service.impl.UnimedServiceImpl;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.engine.DocumentMissingException;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticSearchTests {

    @Resource
    private UnimedRepository unimedRepository;
    @Resource
    private ElasticsearchTemplate elasticsearchTemplate;
    @Resource
    private UnimedServiceImpl unimedService;
    @Resource
    private SimpleSearchQueryEngine simpleSearchQueryEngine;

    @Test
    public void add() {
        Unimed unimed = new Unimed();
//        unimed.setBithday(new Date());
        String date = DateUtils.format(new Date(), DateUtils.DATETIME_Y_M_DH_M_S);
        unimed.setCreateDate(new Date());
//        unimed.setGender("1");
//        unimed.setIdNumber("210219199702242235");
//        unimed.setMobile("13321776503");
//        unimed.setName("何时圈222");
        unimed.setId("1");
//        应该是put方式，因为只有put方式需要自己定义id,所以这个方法属于可用于添加和替换
//        unimedRepository.saveOrUpdate(unimed);

        simpleSearchQueryEngine.add(unimed);
//        simpleSearchQueryEngine.update(unimed, Unimed.class);
    }

    @Test
    public void delete() {

        unimedRepository.deleteById("Dp_phmoBVp_lAJVrBPbH");
    }

    @Test
    public void getById() {

        Optional<Unimed> result =  unimedRepository.findById("1");
        if (result != null) {
            System.out.println(result.get());
        }
    }

    @Test
    public void queryForList() {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("bithday"))
//                .must(QueryBuilders.matchQuery("name", "赵六张爱萍"))
//                .mustNot(QueryBuilders.matchQuery("gender", "2"))
                .should(QueryBuilders.termQuery("gender", "0"))
//                .should(QueryBuilders.termQuery("gender", "1"))
                .filter(QueryBuilders.termQuery("idNumber", "210281199502214502"))
        ;
        SearchQuery query = new NativeSearchQueryBuilder().withQuery(queryBuilder).build();
        List<Unimed> result = elasticsearchTemplate.queryForList(query, Unimed.class);
       for (Unimed params : result) {
           System.out.println(params);
       }
    }

    @Test
    public void update() {
        try {
            String id = "D58SiGoBVp_lAJVryvam";
            UpdateRequest updateRequest = new UpdateRequest();
            updateRequest.doc(XContentFactory.jsonBuilder().startObject()
                            .field("name", "何时圈1")
                            .field("mobile","123")
                            .endObject());
            UpdateQuery searchQuery = new UpdateQueryBuilder().withId(id)
                            .withClass(Unimed.class).withUpdateRequest(updateRequest).build();
            UpdateResponse result = elasticsearchTemplate.update(searchQuery);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentMissingException e) {
            System.out.println("找不到文档");

        }
    }

    @Test
    public void findPage() {
        int page = 0;
        int size = 10;
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.existsQuery("bithday"))
//                .must(QueryBuilders.matchQuery("name", "赵六张爱萍"))
//                .mustNot(QueryBuilders.matchQuery("gender", "2"))
//                .must(QueryBuilders.termQuery("gender", "0"))
//                .should(QueryBuilders.termQuery("gender", "1"))
                .filter(QueryBuilders.termQuery("idNumber", "210281199502214502"))
                ;
        Pageable pageable = PageRequest.of(page, size);
        SortBuilder sort = new SortBuilders()
                .fieldSort("bithday")
                .order(SortOrder.DESC);

        SortBuilder sort1 = new SortBuilders()
                .fieldSort("gender")
                .order(SortOrder.DESC);

        SearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(pageable)
//                .withSort(sort)
                .withSort(sort1)
                .build();
        AggregatedPage<Unimed> result = elasticsearchTemplate.queryForPage(query, Unimed.class);
        System.out.println("totalPages:" + result.getTotalPages());
        List<Unimed> contentList =  result.getContent();
        for(Unimed unimed : contentList) {
//            System.out.println("bithday:"+ DateUtils.format(unimed.getBithday(), DateUtils.DATE_Y_M_D));
            System.out.println("content:" + unimed);
        }

        System.out.println("pageSize:" + result.getPageable().getPageSize());
        System.out.println("pageNum:" + result.getPageable().getPageNumber());
    }


    @Test
    public void find() throws ParseException {
        UnimedQuery unimedQuery = new UnimedQuery();
//        unimedQuery.setName("赵六张爱萍");
//        unimedQuery.setMobile("13521741253");
        Date date = DateUtils.parse("2019-05-14 16:19:18", DateUtils.DATETIME_Y_M_DH_M_S);
        unimedQuery.setCreateDate(date);
        List<Unimed> result = simpleSearchQueryEngine.find(unimedQuery, Unimed.class, 10000);
        for (Unimed unimed : result) {
//            System.out.println("bithday:"+ DateUtils.format(unimed.getBithday(), DateUtils.DATE_Y_M_D));
            System.out.println("content:" + unimed);
        }
    }

    @Test
    public void findPage1() throws ParseException {
        int page = 0;
        int size = 10;
        UnimedQuery unimedQuery = new UnimedQuery();
        List<String> genders = new ArrayList<>();
//        genders.add("0");
////        genders.add("1");

        unimedQuery.setGenders(genders);
        Pageable pageable = PageRequest.of(page, size);
//        Page<Unimed> result = simpleSearchQueryEngine.find(unimedQuery, Unimed.class, pageable);
//        List<Unimed> contentList =  result.getContent();
//        for (Unimed unimed : contentList) {
//            System.out.println("bithday:"+ DateUtils.format(unimed.getBithday(), DateUtils.DATE_Y_M_D));
//            System.out.println("content:" + unimed);
//        }


        ScrollId scrollId = new ScrollId();
        Page<Unimed> first = simpleSearchQueryEngine.scroll(unimedQuery, Unimed.class, pageable, scrollId);
        scrollId.getValue();
        System.out.println("first");
        if (!CollectionUtils.isEmpty(first.getContent())) {
            for (Unimed unimed : first) {
                System.out.println("bithday:" + DateUtils.format(unimed.getCreateDate(), DateUtils.DATETIME_Y_M_DH_M_S));
                System.out.println("content:" + unimed);
            }
        }

//        Pageable secondPageable = PageRequest.of(1, 10);
//        Page<Unimed> second = simpleSearchQueryEngine.scroll(unimedQuery, Unimed.class, secondPageable, scrollId);
//        System.out.println("second");
//        if (!CollectionUtils.isEmpty(second.getContent())) {
//            for (Unimed unimed : second) {
//                System.out.println("bithday:" + DateUtils.format(unimed.getBithday(), DateUtils.DATE_Y_M_D));
//                System.out.println("content:" + unimed);
//            }
//        }

//        Pageable threePageable = PageRequest.of(2, 10);
//        Page<Unimed> three = simpleSearchQueryEngine.scroll(unimedQuery, Unimed.class, threePageable, scrollId);
//        System.out.println("three");
//        if (!CollectionUtils.isEmpty(three.getContent())) {
//            for (Unimed unimed : three) {
////                System.out.println("bithday:" + DateUtils.format(unimed.getBithday(), DateUtils.DATE_Y_M_D));
//                System.out.println("content:" + unimed);
//            }
//        }
    }

    @Test
//    选择排序(选择最小的 or 最大)
    public void select_sort() {
        int[] array = new int[] {341, 21, 54, 1, 123, 5213, 654, 345};
        for (int i=0; i< array.length -1; i++) {
//            System.out.println("i:"+array[i]);
            int minIndex = i;

            // 这里面的思想是找最小，比如说for循环第一次找到21最小，然后从21开始遍历找比21还小的数。
            for (int j=i+1; j<array.length; j++) {
//                这里的 ><决定是大到小还是小到大
                if (array[j] > array[minIndex]) {
                    minIndex = j;
                }
            }
//            System.out.println("i:"+i+",minIndex"+minIndex);
            if (minIndex != i) {
                int temp = array[i];
                array[i] = array[minIndex];
                array[minIndex] = temp;
            }
            for (int m=0; m< array.length; m++) {
                System.out.println(array[m]+",");
            }
            System.out.println("");
        }
    }

    /**
     * 插入排序
     *
     * */
    @Test
    public void insert_sort() {
        int[] array = new int[] {341, 21, 54, 1, 123, 5213, 654, 345};
        for (int i = 0; i< array.length; i++) {
            // 提取当前的
            int temp = array[i];
            //从当前序列最右边的开始向左比，找到比其小的数
            int j = i;
//            while (j > 0; j < ) {
//
//            }
        }
    }
}
