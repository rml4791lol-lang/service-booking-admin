/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mdd.admin.config.quartz.TaskConstants
 */
package com.mdd.admin.config.quartz;

public class TaskConstants {
    public static final String TASK_CLASS_NAME = "TASK_CLASS_NAME";
    public static final String TASK_PROPERTIES = "TASK_PROPERTIES";
    /** la_dev_crontab.status：1=运行。 */
    public static final Integer STATUS_RUN = 1;
    /** la_dev_crontab.status：2=停止（管理员主动停止，重启后仍暂停）。 */
    public static final Integer STATUS_STOP = 2;
    /** la_dev_crontab.status：3=错误（执行失败，重启后继续调度以便自愈）。 */
    public static final Integer STATUS_FAIL = 3;
}

