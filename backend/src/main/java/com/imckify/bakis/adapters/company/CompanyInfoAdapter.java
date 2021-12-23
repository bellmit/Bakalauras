package com.imckify.bakis.adapters.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CompanyInfoAdapter {

    public static final Logger logger = LoggerFactory.getLogger(CompanyInfoAdapter.class);

    @Autowired
    CacheManager cacheManager;

    @Autowired
    CompanyInfoService service;

    @Scheduled(fixedRate = 1000 * 5)
    private void getCompanyInfo() throws Exception {
        logger.info("Executing scheduled task {}()", new Object(){}.getClass().getEnclosingMethod().getName());
        Submission submission = service.getSubmission();
        Cache cache = this.cacheManager.getCache("getSubmission"); // debug spring cache

        System.out.println("==============================================");
    }
}
