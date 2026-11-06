package com.mdd.front.service.impl;

/**
 * #162 登录失败诊断日志：纯逻辑断言器（带 main，不依赖 junit）。
 *
 * <p>本仓库离线 Maven 仓库里没有 junit / surefire provider，测试按仓库惯例写成
 * 「带 main 的纯 Java 断言器」（与 LoginFailLimitGuardTest 同款），由
 * tools/instructor/run-login-log-test.sh 编译 like-front 后直接跑。</p>
 *
 * <p>覆盖（接口行为——三分支日志可区分、响应不变——由 E2E 探针实跑验证，
 * 见 tools/instructor/login-logs-e2e.mjs）：</p>
 * <ol>
 *   <li>reason 取值：四个常量精确匹配且互不相同（可区分）；</li>
 *   <li>手机号打码：11 位保留前 3 后 4，异常输入整体打码，null 归 "-"，不泄露；</li>
 *   <li>盐指纹：sha256 前 8 位十六进制、与明文盐不同、盐缺失（null/空/空白）归 "none"。</li>
 * </ol>
 */
public final class LoginLogFieldsTest {

    private static int passed;
    private static int failed;

    public static void main(String[] args) {
        reasonConstants();
        phoneMasking();
        saltFingerprint();
        System.out.println();
        System.out.println("passed=" + passed + " failed=" + failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    private static void reasonConstants() {
        System.out.println("[reason 取值]");
        check("NO_ACCOUNT 拼写精确",
                "NO_ACCOUNT".equals(LoginLogFields.REASON_NO_ACCOUNT));
        check("NOT_OPENED 拼写精确",
                "NOT_OPENED".equals(LoginLogFields.REASON_NOT_OPENED));
        check("PASSWORD_MISMATCH 拼写精确",
                "PASSWORD_MISMATCH".equals(LoginLogFields.REASON_PASSWORD_MISMATCH));
        check("CONFIG 拼写精确",
                "CONFIG".equals(LoginLogFields.REASON_CONFIG));
        check("四个 reason 互不相同（日志可区分）",
                !LoginLogFields.REASON_NO_ACCOUNT.equals(LoginLogFields.REASON_NOT_OPENED)
                        && !LoginLogFields.REASON_NO_ACCOUNT.equals(LoginLogFields.REASON_PASSWORD_MISMATCH)
                        && !LoginLogFields.REASON_NO_ACCOUNT.equals(LoginLogFields.REASON_CONFIG)
                        && !LoginLogFields.REASON_NOT_OPENED.equals(LoginLogFields.REASON_PASSWORD_MISMATCH)
                        && !LoginLogFields.REASON_NOT_OPENED.equals(LoginLogFields.REASON_CONFIG)
                        && !LoginLogFields.REASON_PASSWORD_MISMATCH.equals(LoginLogFields.REASON_CONFIG));
    }

    private static void phoneMasking() {
        System.out.println("[手机号打码]");
        check("11 位手机号保留前 3 后 4",
                "138****1234".equals(LoginLogFields.maskPhone("13800001234")));
        check("11 位带首尾空白先 trim 再打码",
                "199****5678".equals(LoginLogFields.maskPhone(" 19900005678 ")));
        check("短输入整体打码不泄露",
                "****".equals(LoginLogFields.maskPhone("123")));
        check("超长输入整体打码不泄露",
                "****".equals(LoginLogFields.maskPhone("1380000123499999")));
        check("空串整体打码",
                "****".equals(LoginLogFields.maskPhone("")));
        check("null 归 \"-\"",
                "-".equals(LoginLogFields.maskPhone(null)));
        check("打码结果不含完整手机号",
                !LoginLogFields.maskPhone("13800001234").contains("13800001234"));
    }

    private static void saltFingerprint() {
        System.out.println("[盐指纹]");
        check("固定盐 → sha256 前 8 位（与独立实现比对）",
                "08728035".equals(LoginLogFields.saltFingerprintOf("test-salt")));
        check("另一固定盐 → 不同指纹",
                "45e72c45".equals(LoginLogFields.saltFingerprintOf("lxt-f162")));
        check("指纹不等于明文盐",
                !LoginLogFields.saltFingerprintOf("test-salt").contains("test-salt"));
        check("指纹长度为 8",
                LoginLogFields.saltFingerprintOf("test-salt").length() == 8);
        check("不同盐 → 不同指纹",
                !LoginLogFields.saltFingerprintOf("a").equals(LoginLogFields.saltFingerprintOf("b")));
        check("null 盐 → none",
                "none".equals(LoginLogFields.saltFingerprintOf(null)));
        check("空串盐 → none",
                "none".equals(LoginLogFields.saltFingerprintOf("")));
        check("全空白盐 → none",
                "none".equals(LoginLogFields.saltFingerprintOf("   ")));
    }

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("  PASS " + name);
        } else {
            failed++;
            System.out.println("  FAIL " + name);
        }
    }

    private LoginLogFieldsTest() {
    }
}
