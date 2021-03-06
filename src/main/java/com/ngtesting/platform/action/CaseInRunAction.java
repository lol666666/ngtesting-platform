package com.ngtesting.platform.action;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.entity.TestCaseInRun;
import com.ngtesting.platform.service.CaseInRunService;
import com.ngtesting.platform.service.CaseService;
import com.ngtesting.platform.service.CustomFieldService;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.vo.CustomFieldVo;
import com.ngtesting.platform.vo.TestCaseInRunVo;
import com.ngtesting.platform.vo.UserVo;
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
@RequestMapping(Constant.API_PATH_CLIENT + "caseInRun/")
public class CaseInRunAction extends BaseAction {
    @Autowired
    CaseService caseService;
	@Autowired
	CaseInRunService caseInRunService;

    @Autowired
    CustomFieldService customFieldService;

    @AuthPassport(validate = true)
    @RequestMapping(value = "query", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> query(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        Long orgId = json.getLong("orgId");
        Long projectId = json.getLong("projectId");
        Long runId = json.getLong("runId");

        List<TestCaseInRunVo> vos = caseInRunService.query(runId);
        List<CustomFieldVo> customFieldList = customFieldService.listForCaseByProject(orgId, projectId);

        ret.put("data", vos);
        ret.put("customFields", customFieldList);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

    @AuthPassport(validate = true)
    @RequestMapping(value = "get", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> get(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
        Long orgId = userVo.getDefaultOrgId();
        Long caseId = json.getLong("id");

        TestCaseInRunVo vo = caseInRunService.getById(caseId);

        ret.put("data", vo);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

    @AuthPassport(validate = true)
    @RequestMapping(value = "setResult", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> setResult(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
        Long orgId = userVo.getDefaultOrgId();

        Long caseInRunId = json.getLong("id");
        String result = json.getString("result");
        String status = json.getString("status");
        Long nextId = json.getLong("nextId");

        TestCaseInRunVo vo = caseInRunService.setResultPers(caseInRunId, result, status, nextId, userVo);

        ret.put("data", vo);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

    @AuthPassport(validate = true)
    @RequestMapping(value = "rename", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> rename(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

        TestCaseInRunVo vo = caseInRunService.renamePers(json, userVo);
        caseService.updateParentIfNeededPers(vo.getpId());

        ret.put("data", vo);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

    @AuthPassport(validate = true)
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

        Long entityId = json.getLong("entityId");
        TestCaseInRun caseInRun = caseInRunService.deleteCaseFromRunPers(entityId, userVo);

        caseService.updateParentIfNeededPers(caseInRun.getpId());

        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

    @AuthPassport(validate = true)
    @RequestMapping(value = "move", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> move(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();
        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

        TestCaseInRunVo vo = caseInRunService.movePers(json, userVo);
        caseService.updateParentIfNeededPers(vo.getpId());

        ret.put("data", vo);
        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }

}
