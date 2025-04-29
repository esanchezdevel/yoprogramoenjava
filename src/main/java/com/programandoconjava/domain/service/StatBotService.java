package com.programandoconjava.domain.service;

public interface StatBotService {

    /**
     * Store in stats_bots db table one record
     * 
     * @param ip The IP of the request
     * @param method The method of the request
     * @param endpoint The endpoint of the request
     * @param userAgent The userAgent of the request
     */
    void logStat(String ip, String method, String endpoint, String userAgent);
}
