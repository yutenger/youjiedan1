package com.stardai.manage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(value = "MZ")
@SuppressWarnings("all")
public class PageController {

    
    
    // 推广
    @RequestMapping(value = "/{zhuce}", method = RequestMethod.GET)
    public String getClickAmount(@PathVariable("zhuce") String zhuce) {
            return zhuce;
    }
}
