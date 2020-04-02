package com.raising.rest.aasPublic;


import com.alibaba.fastjson.JSONObject;
import com.baosight.common.constants.Constants;
import com.common.BaseController;
import com.util.CSVUtils;
import com.util.MultipleDataSource;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Component
@Path("/raising/aasRest")
public class AASRest extends BaseController {

    @GET
    @Path("/collectorNameResources")
    @Produces(MediaType.APPLICATION_JSON)
    public JSONObject getProgenyGroup(@QueryParam("groupNamesStr") String groupNamesStr){
        JSONObject returnInfo = new JSONObject();
        List<String> collectionNames = resourceService.getCollectionNames(groupNamesStr);
        returnInfo.put("status",Constants.EXECUTE_SUCCESS);
        returnInfo.put("data",collectionNames);
        return returnInfo;
    }
}
