package com.ngtesting.platform.service;

import java.util.List;
import java.util.Map;

public interface ReportService extends BaseService {

    Map<String, List<Object>> chart_excution_process_by_project(Long projectId, Integer numb);
    Map<String, List<Object>> chart_design_progress_by_project(Long projectId, Integer numb);

    List<Map<Object, Object>> chart_execution_result_by_plan(Long planId, Integer numb);
    Map<String, List<Object>> chart_execution_process_by_plan(Long planId, Integer numb);
    Map<String, List<Object>> chart_execution_progress_by_plan(Long planId, Integer numb);

    Map<String, List<Object>> countByStatus(List<Object[]> ls);
}