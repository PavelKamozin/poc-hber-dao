/*Copyright (c) 2019-2020 softserveinc.com All Rights Reserved.
 This software is the confidential and proprietary information of softserveinc.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with softserveinc.com*/
package com.softeserve.wm_demo.db_hibernate.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.wavemaker.runtime.data.dao.procedure.WMProcedureExecutor;

@Service
public class Db_hibernateProcedureExecutorServiceImpl implements Db_hibernateProcedureExecutorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Db_hibernateProcedureExecutorServiceImpl.class);

    @Autowired
    @Qualifier("db_hibernateWMProcedureExecutor")
    private WMProcedureExecutor procedureExecutor;


}