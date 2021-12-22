package com.imckify.bakis.adapters.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompanyInfoAdapter {

    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoAdapter.class);

    @Autowired
    CompanyInfoService service;

    @Scheduled(fixedRate = 1000 * 5)
    private void getCompanyInfo() throws Exception {
        logger.info("Executing scheduled task {}()", new Object(){}.getClass().getEnclosingMethod().getName());
        Object info = service.getSubmission();
//        org.springframework.cache.Cache cache = this.cacheManager.getCache("feed"); // debug spring cache

        System.out.println("==============================================");
    }
}
