package com.softeserve.wm_demo.commonservice.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.softeserve.wm_demo.commonservice.CommonService;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;

/**
 * Controller object for domain model class {@link CommonService}.
 * @see CommonService
 */
@RestController
@Api(value = "CommonController", description = "controller class for java service execution")
@RequestMapping("/common")
public class CommonController {

    @Autowired
    private CommonService commonService;

    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    @RequestMapping(value = "/sampleJavaOperation", method = RequestMethod.GET)
    public String sampleJavaOperation(@RequestParam(value = "name", required = false) String name,  HttpServletRequest request) {
        return commonService.sampleJavaOperation(name, request);
    }
}
