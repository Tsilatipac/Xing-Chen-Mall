package com.lcy.xingchenmall.search.controller;

import com.lcy.xingchenmall.search.service.MallSearchService;
import com.lcy.xingchenmall.search.vo.SearchParam;
import com.lcy.xingchenmall.search.vo.SearchResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SearchController {

    @Autowired
    MallSearchService mallSearchService;

    @GetMapping("/list.html")
    public String listPage(SearchParam param, Model model, HttpServletRequest request){
        param.set_queryString(request.getQueryString());
        //1、根据传递来的页面查询参数，去es中检索商品
        SearchResult result = mallSearchService.search(param);
        model.addAttribute("result",result);
        System.out.println(result);
        return "list";
    }
}
