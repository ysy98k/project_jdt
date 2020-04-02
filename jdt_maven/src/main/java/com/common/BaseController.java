package com.common;

import com.baosight.common.basic.dao.RestDao;
import com.baosight.common.basic.service.BaseService;
import com.baosight.df.metamanage.service.FrameSettingService;
import com.baosight.df.metamanage.service.TenantMenuService;
import com.raising.backstage.service.*;
import com.raising.backstage.service.privilege.*;
import com.raising.backstage.service.privilege.projectManage.AlarmVariableManagementService;
import com.raising.ccs.ResourceService;
import com.raising.forward.service.*;
import com.raising.forward.service.ProgressManage.CurrentProgressService;
import com.raising.forward.service.ProgressManage.ProgressAnalysisService;
import com.raising.forward.service.ProgressManage.WorkPlanService;
import com.raising.forward.service.constructionManagement.*;
import com.raising.forward.service.d.DMeasureResultService;
import com.raising.forward.service.d.DesignLineService;
import com.raising.forward.service.d.StationConfigService;
import com.raising.forward.service.d.ZeroConfigService;
import com.raising.forward.service.guidance.GuidanceManagementService;
import com.raising.forward.service.guidance.LineDataService;
import com.raising.forward.service.j.JDisDataService;
import com.raising.forward.service.j.JMileageDataService;
import com.raising.forward.service.j.JRingDataService;
import com.raising.forward.service.j.JSettingService;
import com.raising.forward.service.tbmManage.TbmResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;


/**
 * 供controller继承。
 */
public abstract class BaseController {

    @Autowired
    protected RestDao restDao;

    @Autowired
    protected RedisTemplate redisTemplate;

    @Autowired
    protected HttpServletRequest request;


    @Autowired
    protected BaseService baseService;

    @Autowired
    protected FrameSettingService frameSettingService;

    @Autowired
    protected MobileService mobileService;

    @Autowired
    protected CodeItemService codeItemService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected TbmResumeService tbmResumeService;

    @Autowired
    protected TbmService bTbmService;//后台tbmService

    @Autowired
    protected SectionManageService sectionManageService;

    @Autowired
    protected ProjectService projectService;

    @Autowired
    protected ProjectForwardService projectForwardService;

    @Autowired
    protected DrawLineService drawLineService;

    @Autowired
    protected ListQueryService listQueryService;

    @Autowired
    protected WorkPlanService workPlanService;

    @Autowired
    protected CurrentProgressService currentProgressService;

    @Autowired
    protected RoleManagementService roleManagementService;

    @Autowired
    protected RoleUserService roleUserService;

    @Autowired
    protected BackstageUserService backstageUserService;

    @Autowired
    protected RoleResourceService roleResourceService;

    @Autowired
    protected UserManageService userManageService;

    @Autowired
    protected TenantMenuService tenantMenuService;

    @Autowired
    protected ResourceService resourceService;

    @Autowired
    protected ProgressAnalysisService progressAnalysisService;

    @Autowired
    protected ConstructionTbmInfoService constructionTbmInfoService;

    @Autowired
    protected GuidanceManagementService guidanceManagementService;

    @Autowired
    protected JDisDataService jDisDataService;

    @Autowired
    protected JMileageDataService jMileageDataService;

    @Autowired
    protected JRingDataService jRingDataService;

    @Autowired
    protected JSettingService jSettingService;

    @Autowired
    protected DMeasureResultService dMeasureResultService;

    @Autowired
    protected TotalInfoService totalInfoService;

    @Autowired
    protected AlarmInfoService alarmInfoService;

    @Autowired
    protected AlarmVariableManagementService alarmVariableManagementService;

    @Autowired
    protected DesignLineService designLineService;

    @Autowired
    protected ZeroConfigService zeroConfigService;

    @Autowired
    protected StationConfigService stationConfigService;

    @Autowired
    protected LineDataService lineDataService;



}
