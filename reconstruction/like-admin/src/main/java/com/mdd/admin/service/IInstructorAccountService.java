package com.mdd.admin.service;

import com.mdd.admin.validate.commons.PageValidate;
import com.mdd.admin.vo.instructor.InstructorAccountAuditVo;
import com.mdd.admin.vo.instructor.InstructorAccountVo;
import com.mdd.common.core.PageResult;

public interface IInstructorAccountService {

    /**
     * 查看教练账号状态。
     */
    InstructorAccountVo status(Integer instructorId);

    /**
     * 开通教练账号：绑定/创建 la_user、生成初始口令、标记首登改密。
     *
     * @return 仅本次返回初始口令，不写日志、不落审计。
     */
    InstructorAccountCredential open(Integer instructorId, String initialPassword,
                                     Integer adminId, String adminName, String ip);

    /**
     * 重置教练密码并强制下次登录改密。
     */
    InstructorAccountCredential reset(Integer instructorId, String initialPassword,
                                      Integer adminId, String adminName, String ip);

    /**
     * 停用教练账号：停用资料状态、禁用登录账号、清除会话。
     */
    void disable(Integer instructorId, String reason, Integer adminId, String adminName, String ip);

    /**
     * 启用教练账号：恢复资料状态、解除登录账号禁用。
     */
    void enable(Integer instructorId, Integer adminId, String adminName, String ip);

    /**
     * 账号操作审计分页。
     */
    PageResult<InstructorAccountAuditVo> audits(Integer instructorId, String action, PageValidate pageValidate);

    /**
     * 开通/重置返回值：初始口令只在这里出现一次。
     */
    class InstructorAccountCredential {
        private final String initialPassword;
        private final InstructorAccountVo account;

        public InstructorAccountCredential(String initialPassword, InstructorAccountVo account) {
            this.initialPassword = initialPassword;
            this.account = account;
        }

        public String getInitialPassword() {
            return initialPassword;
        }

        public InstructorAccountVo getAccount() {
            return account;
        }
    }
}
