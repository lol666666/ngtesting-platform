package com.ngtesting.platform.service;

import java.util.List;

import com.ngtesting.platform.entity.TestCustomField;
import com.ngtesting.platform.entity.TestUser;
import com.ngtesting.platform.vo.CustomFieldVo;
import com.ngtesting.platform.vo.Page;
import com.ngtesting.platform.vo.TestProjectVo;
import com.ngtesting.platform.vo.UserVo;

public interface CustomFieldService extends BaseService {
	List<TestCustomField> list(Long orgId);
	
	TestCustomField save(CustomFieldVo vo, Long orgId);
	boolean delete(Long id);

	List<CustomFieldVo> genVos(List<TestCustomField> pos);
	CustomFieldVo genVo(TestCustomField po);

	List<CustomFieldVo> listVos(Long orgId);

	List<String> listApplyTo();

	List<String> listType();

	List<String> listFormat();

	boolean changeOrderPers(Long id, String act);

	List<TestProjectVo> listProjectsForField(Long orgId, Long fieldId);

	boolean saveRelationsProjects(Long id, List<TestProjectVo> projects);

	void initPo(TestCustomField po, CustomFieldVo vo);

}
