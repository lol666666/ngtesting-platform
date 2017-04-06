package com.ngtesting.platform.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.entity.SysCustomField;
import com.ngtesting.platform.service.CustomFieldService;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.util.Constant;
import com.ngtesting.platform.util.Constant.RespCode;
import com.ngtesting.platform.vo.Page;
import com.ngtesting.platform.vo.CustomFieldVo;
import com.ngtesting.platform.vo.UserVo;


@Controller
@RequestMapping(Constant.API_PATH_CLIENT + "customField/")
public class CustomFieldAction extends BaseAction {
	@Autowired
	CustomFieldService customFieldService;
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "list", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> list(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		
		String keywords = json.getString("keywords");
		String disabled = json.getString("disabled");
		int currentPage = json.getInteger("currentPage") == null? 0: json.getInteger("currentPage") - 1;
		int itemsPerPage = json.getInteger("itemsPerPage") == null? Constant.PAGE_SIZE: json.getInteger("itemsPerPage");
		
		Page page = customFieldService.listByPage(orgId, keywords, disabled, currentPage, itemsPerPage);
		List<CustomFieldVo> vos = customFieldService.genVos(page.getItems());
        
		ret.put("totalItems", page.getTotal());
        ret.put("data", vos);
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
		
		Long customFieldId = json.getLong("id");
		
//		List<RelationOrgGroupCustomFieldVo> relations = orgGroupCustomFieldService.listRelationsByCustomField(orgId, customFieldId);
		
		if (customFieldId == null) {
			ret.put("customField", new CustomFieldVo());
//	        ret.put("relations", relations);
			ret.put("code", Constant.RespCode.SUCCESS.getCode());
			return ret;
		}
		
		SysCustomField po = (SysCustomField) customFieldService.get(SysCustomField.class, Long.valueOf(customFieldId));
		CustomFieldVo vo = customFieldService.genVo(po);
		
        ret.put("customField", vo);
//        ret.put("relations", relations);
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();

		UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);
		Long orgId = userVo.getDefaultOrgId();
		
		CustomFieldVo customField = JSON.parseObject(JSON.toJSONString(json.get("customField")), CustomFieldVo.class);
		SysCustomField po = customFieldService.save(customField, orgId);
		
		if (po == null) {
			ret.put("code", RespCode.BIZ_FAIL.getCode());
			ret.put("msg", "邮箱已存在");
			return ret;
		} 

//		List<RelationOrgGroupCustomFieldVo> relations = (List<RelationOrgGroupCustomFieldVo>) json.get("relations");
//		boolean success = orgGroupCustomFieldService.saveRelations(relations);
//		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		
		return ret;
	}
	
	@AuthPassport(validate = true)
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, @RequestBody JSONObject json) {
		Map<String, Object> ret = new HashMap<String, Object>();
		
		Long customFieldId = json.getLong("id");
		Long orgId = json.getLong("orgId");
		
		boolean success = customFieldService.delete(customFieldId);
		
		ret.put("code", Constant.RespCode.SUCCESS.getCode());
		return ret;
	}
	
}