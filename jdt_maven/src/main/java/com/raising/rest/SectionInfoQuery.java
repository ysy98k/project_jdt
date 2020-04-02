package com.raising.rest;

/**
 * @author ysy
 * @date 2018/5/11 8:58
 * @description
 */

import com.alibaba.fastjson.JSONArray;
import com.common.RequestHolder;
import com.raising.ccs.ResourceService;
import com.raising.forward.service.ListQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * 查询页面的rest服务
 *
 * @author yetianqi
 */
@Component
@Path("/section")
public class SectionInfoQuery {

    private static final Logger logger = LoggerFactory.getLogger(SectionInfoQuery.class);

    @Autowired
    protected ResourceService resourceService;

    @Context
    HttpServletRequest request;
    @Context
    HttpServletResponse response;

    @Autowired
    private ListQueryService listQueryService;

    @GET
    @Path("/queryInfo")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONArray query() {
        JSONArray returnInfo = new JSONArray();
        //String groupNames = request.getSession().getAttribute("groupNames").toString();
        HttpServletRequest currentRequest = RequestHolder.getCurrentRequest();
        String groupNames  =  currentRequest.getSession().getAttribute("groupNames").toString();
        List<String> collectionNames = resourceService.getCollectionNames(groupNames);
        returnInfo=listQueryService.getProjet(null,collectionNames);
        return returnInfo;
    }
}

