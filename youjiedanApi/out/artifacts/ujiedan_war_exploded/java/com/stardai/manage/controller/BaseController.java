package com.stardai.manage.controller;

import com.google.common.collect.Interner;
import com.google.common.collect.Interners;

/**
 *  @author jdw
 *  @date 2017/10/16
 */
public class BaseController {
    static final Interner<String> POOL = Interners.newWeakInterner();
}
