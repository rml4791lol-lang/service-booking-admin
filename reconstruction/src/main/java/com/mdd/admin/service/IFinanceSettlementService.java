package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.validate.finance.FinanceSettlementPushValidate;
import com.mdd.admin.validate.finance.FinanceSettlementSearchValidate;
import com.mdd.common.core.PageResult;
import com.mdd.common.settlement.SettlementItem;
import java.util.Map;

/**
 * 分账数据只读查询、导出与受控上报。
 *
 * <p>查询/导出不写库、不划拨资金；上报只在显式调用且配置开启时发生。</p>
 */
public interface IFinanceSettlementService {

    PageResult<SettlementItem> lists(PageValidate pageValidate, FinanceSettlementSearchValidate searchValidate);

    Map<String, Object> summary(FinanceSettlementSearchValidate searchValidate);

    Map<String, Object> export(FinanceSettlementSearchValidate searchValidate);

    Map<String, Object> config();

    Map<String, Object> push(FinanceSettlementPushValidate validate);
}
