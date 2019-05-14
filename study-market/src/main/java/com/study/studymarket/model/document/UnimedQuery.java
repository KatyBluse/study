package com.study.studymarket.model.document;


import com.study.studymarket.common.util.elasticSearch.annotations.Document;
import com.study.studymarket.common.util.elasticSearch.annotations.ExistQuery;
import com.study.studymarket.common.util.elasticSearch.annotations.MatchQuery;
import com.study.studymarket.common.util.elasticSearch.enums.Container;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Document(index = "test",type = "user_info")
@Data
public class UnimedQuery {

    @MatchQuery(container = Container.must, column = "name")
    private String name;

    @MatchQuery(container = Container.must, column = "mobile")
    @ExistQuery(isExist = true, column = "mobile")
    private String mobile;

    @MatchQuery(container = Container.must, column = "idNumber")
    private String idNumber;

    @MatchQuery(container = Container.must, column = "gender")
    private String gender;

    @MatchQuery(container = Container.should, column = "gender")
    private List<String> genders;

    @MatchQuery(container = Container.must, column = "createDate")
    private Date createDate;

}
