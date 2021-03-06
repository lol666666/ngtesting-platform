package com.ngtesting.platform.action;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.entity.TestHistory;
import com.ngtesting.platform.entity.TestPlan;
import com.ngtesting.platform.entity.TestProject;
import com.ngtesting.platform.entity.TestRelationProjectRoleEntity;
import com.ngtesting.platform.service.*;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.vo.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping(Constant.API_PATH_CLIENT + "project/")
public class ProjectAction extends BaseAction {
	private static final Log log = LogFactory.getLog(ProjectAction.class);

	@Autowired
    ProjectService projectService;
	@Autowired
	PlanService planService;
	@Autowired
	HistoryService historyService;

    @Autowired
    ProjectRoleService projectRoleService;
    @Autowired
    PushSettingsService pushSettingsService;

    @Autowired
    RelationProjectRoleEntityService relationProjectRoleEntityService;

	@AuthPassport(validate = true)
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

		String keywords = json.getString("keywords");
		String disabled = json.getString("disabled");

		List<TestProjectVo> vos = projectService.listVos(orgId, keywords, disabled);

        ret.put("data", vos);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());

		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "getInfo", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> getInfo(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

		Long projectId = json.getLong("id");

		if (projectId != null) {
			TestProject project = projectService.getDetail(projectId);
			TestProjectVo vo = projectService.genVo(project);

			if (TestProject.ProjectType.group.equals(project.getType())) {
				vo.setLastestProjectGroup(projectService.isLastestProjectGroup(orgId, projectId));
			}

			ret.put("data", vo);
		}
        List<TestProjectVo> groups = projectService.listProjectGroups(orgId);
        ret.put("groups", groups);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}
    @AuthPassport(validate = true)
    @RequestMapping(value = "getUsers", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> getUsers(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
        Long orgId = userVo.getDefaultOrgId();

        Long projectId = json.getLong("id");


        List<ProjectRoleVo> projectRoles = projectRoleService.list(orgId, null, null);

        List<TestRelationProjectRoleEntity> entityInRolesPos = relationProjectRoleEntityService.listByProject(projectId);
        List<RelationProjectRoleEntityVo> entityInRoles = relationProjectRoleEntityService.genVos(entityInRolesPos);

        ret.put("projectRoles", projectRoles);
        ret.put("entityInRoles", entityInRoles);

        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

	@AuthPassport(validate = true)
	@RequestMapping(value = "view", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> view(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long id = json.getLong("id");

		TestProject po = projectService.getDetail(id);
		TestProjectVo vo = projectService.genVo(po);

        List<TestPlan> planPos = planService.list(id, vo.getType());
        List<TestPlanVo> planVos = planService.genVos(planPos);

        List<TestHistory> historyPos = historyService.list(id, vo.getType());
        Map<String, List<TestHistoryVo>> historyVos = historyService.genVosByDate(historyPos);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
        ret.put("project", vo);
        ret.put("plans", planVos);
        ret.put("histories", historyVos);

		return ret;
	}

    @AuthPassport(validate = true)
    @RequestMapping(value = "change", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> change(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
        Long id = json.getLong("id");

        TestProjectVo vo = projectService.viewPers(id, userVo);

        if (vo.getType().equals(TestProject.ProjectType.project.toString())) {
            pushSettingsService.pushRecentProjects(userVo);
            pushSettingsService.pushPrjSettings(userVo);
        }

        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        ret.put("data", vo);

        return ret;
    }

	@AuthPassport(validate = true)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		Long userId = userVo.getId();

		TestProjectVo vo = json.getObject("model", TestProjectVo.class);

        TestProject po = projectService.save(vo, orgId, userVo);
		if (TestProject.ProjectType.project.equals(po.getType())) {
			projectService.updateNameInHisotyPers(po.getId(), userId);
		}

        pushSettingsService.pushRecentProjects(userVo);
        pushSettingsService.pushPrjSettings(userVo);

		ret.put("data", vo);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "saveMembers", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> saveMembers(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

        List<TestRelationProjectRoleEntity> pos = relationProjectRoleEntityService.batchSavePers(json);
        List<RelationProjectRoleEntityVo> entityInRoles = relationProjectRoleEntityService.genVos(pos);

        Long projectId = json.getLong("projectId");
        TestProject project = (TestProject) relationProjectRoleEntityService.get(TestProject.class, projectId);
        historyService.create(project.getId(), userVo, Constant.MsgType.update.msg,
                TestHistory.TargetType.project_member, project.getId(), project.getName());

		ret.put("entityInRoles", entityInRoles);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "changeRole", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> changeRole(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();

		List<TestRelationProjectRoleEntity> pos = relationProjectRoleEntityService.changeRolePers(json);
		List<RelationProjectRoleEntityVo> entityInRoles = relationProjectRoleEntityService.genVos(pos);

        pushSettingsService.pushPrjSettings(userVo);

		ret.put("entityInRoles", entityInRoles);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

	@AuthPassport(validate = true)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		Long id = json.getLong("id");

		projectService.delete(id);

		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}

}
