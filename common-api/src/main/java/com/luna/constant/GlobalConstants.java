package com.luna.constant;

public interface GlobalConstants {

    /**
     * 租户系统管理员
     */
    int USER_TYPE_ADMIN = 99;

    /**
     * 租户
     */
    int USER_TYPE_TENANT = 1;
    /**
     * 员工
     */
    int USER_TYPE_EMPLOYEE = 2;


    // -----------------------------
    // 工作范围
    // -----------------------------

    /**
     * 指定多个医院
     */
    int WORK_SCOPE_HOSPITAL = 1;
    /**
     * 整个公司（租户下所有医院）
     */
    int WORK_SCOPE_TENANT = 9;

    // -----------------------------
    // 就诊状态  0:未确认， 1：已确认，2：已挂号，3：治疗中，4：治疗完成，5：已结账，6：已离开，7：已失约，8：已取消
    // -----------------------------



    int VISIT_STATE_NOT_CONFIRMED = 0;

    int VISIT_STATE_CONFIRMED = 1;

    int VISIT_STATE_REGISTER = 2;

    int VISIT_STATE_TREATMENT = 3;

    int VISIT_STATE_TREATMENT_COMPLETE = 4;

    int VISIT_STATE_CHECKOUT = 5;

    int VISIT_STATE_LEAVE = 6;

    int VISIT_STATE_MISS = 7;

    int VISIT_STATE_CANCEL = 8;


    /**
     * 一次性创建
     */
    Integer ONE_TIME_CREATION = 1;

    /**
     * 待上次随访计划完成，再生成下次随访计划
     */
    Integer AFTER_LAST_CREATION = 2;


    // -----------------------------
    // 随访状态 1.待计划,2.随访完成,3.未成功,4.取消本次随访
    // -----------------------------

    int FOLLOW_UP_TO_PLAN = 1;

    int FOLLOW_UP_FINISH = 2;

    int FOLLOW_UP_UNSUCCESSFUL = 3;

    int FOLLOW_UP_CANCEL = 4;

    // -----------------------------
    // 患者-检验状态 0未提交（保存的时候），1已提交（提交的时候），2已作废;
    // -----------------------------

    int APPLY_STATUS_SUBMIT = 1;
    int APPLY_STATUS_NO_SUBMIT = 0;
    int APPLY_STATUS_CANCEL = 2;

    // -----------------------------
    // 0 未收费 ，1已收费
    // -----------------------------

    int CHARGE_ORDER_STATUS_NO = 0;
    int CHARGE_ORDER_STATUS_YES = 1;


    // -----------------------------
    // 回访状态 1.待回访,2.回访完成,3.回访未成功,4.取消本次回访
    // -----------------------------

    int RECORD_TO_PLAN = 1;

    int RECORD_FINISH = 2;

    int RECORD_UNSUCCESSFUL = 3;

    int RECORD_CANCEL = 4;


    // -----------------------------
    // 添加方式(文档的类型) 1.上传的文档,2.模板填写,3.文档模板,4.表单
    // -----------------------------

    int TEMPLATE_UPLOAD = 1;

    int TEMPLATE_WRITE = 2;

    int TEMPLATE_DOCUMENT = 3;

    int TEMPLATE_FORM = 4;


    /**
     * 适用所有规格
     */
    Integer QUALIFICATION_ALL_SPEC = 1;

    /**
     *
     */
    Integer QUALIFICATION_CHOSE_SPEC = 2;


    /**
     * 诊所中心-组织管理的查询方式 1.科室 ，2.员工
     */
    Integer SEARCH_TYPE_DEPARTMENT = 1;

    /**
     * 待上次随访计划完成，再生成下次随访计划
     */
    Integer SEARCH_TYPE_EMPLOYEE = 2;

    /**
     * 折扣方式：整单
     */
    String DISCOUNT_TYPE_ORDER = "order";

    /**
     * 折扣方式：整单
     */
    String DISCOUNT_TYPE_DETAIL = "detail";

    /**
     * 是否
     */
    int YES = 1;
    int NO = 0;
}
