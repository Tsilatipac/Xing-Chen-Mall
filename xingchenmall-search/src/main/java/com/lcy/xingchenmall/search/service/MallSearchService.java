package com.lcy.xingchenmall.search.service;

import com.lcy.xingchenmall.search.vo.SearchParam;
import com.lcy.xingchenmall.search.vo.SearchResult;

public interface MallSearchService {
    /**
     *
     * @param param  检索所有参数
     * @return  返回检索的结果，里面包含页面需要的所有信息
     */
    SearchResult search(SearchParam param);
}
