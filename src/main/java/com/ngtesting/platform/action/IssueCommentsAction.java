package com.ngtesting.platform.action;

import com.alibaba.fastjson.JSONObject;
import com.ngtesting.platform.config.Constant;
import com.ngtesting.platform.service.IssueCommentsService;
import com.ngtesting.platform.util.AuthPassport;
import com.ngtesting.platform.vo.TestCaseCommentsVo;
import com.ngtesting.platform.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(Constant.API_PATH_CLIENT + "issue_comments/")
public class IssueCommentsAction extends BaseAction {
    @Autowired
    IssueCommentsService commentsService;

    @AuthPassport(validate = true)
    @RequestMapping(value = "save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(HttpServletRequest request, @RequestBody JSONObject json) {
        Map<String, Object> ret = new HashMap<String, Object>();

        UserVo userVo = (UserVo) request.getSession().getAttribute(Constant.HTTP_SESSION_USER_KEY);

        TestCaseCommentsVo vo = commentsService.save(json, userVo);

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
        commentsService.delete(json.getLong("id"), userVo.getId());

        ret.put("code", Constant.RespCode.SUCCESS.getCode());
        return ret;
    }
}
