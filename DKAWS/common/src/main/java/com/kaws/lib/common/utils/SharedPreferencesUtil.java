package com.kaws.lib.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by 杨才 on 2016/1/29.
 */
public class SharedPreferencesUtil {
    private final static String FILE_NAME = "file_name";

    private final static String IS_RUN = "is_run";

    private final static String SPLASH_PATH = "splash_path";

    /**
     * 消息提醒之私信
     */
    public static final String DOCTOR_SIXIN = "doctor_sixin";
    /**
     * 消息提醒之患者申请提醒
     */
    public static final String DOCTOR_PATIENT_NOTICE = "doctor_patient_notice";
    /**
     * 消息提醒之患者问答
     */
    public static final String DOCTOR_QUES_ANSWER = "doctor_ques_answer";
    /**
     * 我的资料之修改姓名
     */
    public static final String CHANGE_NAME_MIME = "change_name_mime";
    /**
     * 我的资料之修改科室
     */
    public static final String CHANGE_DEPARTMENT_MIME = "change_department_mime";

    public static void saveRunConfig(Context context, boolean isRun) {
        getEditor(context).putBoolean(IS_RUN, isRun).commit();
    }

    public static boolean getRunConfig(Context context) {
        return getSharedPreferences(context).getBoolean(IS_RUN, false);
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp;
    }

    private static Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static String getSplashUrl(Context context) {
        return getSharedPreferences(context).getString(SPLASH_PATH, "");
    }

    public static void saveSplashUrl(Context context, String splashPath) {
        getEditor(context).putString(SPLASH_PATH, splashPath).commit();
    }

    public static void saveSiXinConfig(Context context, boolean open) {
        getEditor(context).putBoolean(DOCTOR_SIXIN, open).commit();
    }

    public static boolean getSiXinConfig(Context context) {
        return getSharedPreferences(context).getBoolean(DOCTOR_SIXIN, false);
    }

    public static void savePatientNoticeConfig(Context context, boolean open) {
        getEditor(context).putBoolean(DOCTOR_PATIENT_NOTICE, open).commit();
    }

    public static boolean getPatientNoticeConfig(Context context) {
        return getSharedPreferences(context).getBoolean(DOCTOR_PATIENT_NOTICE, false);
    }

    public static void saveQuesAnswerConfig(Context context, boolean open) {
        getEditor(context).putBoolean(DOCTOR_QUES_ANSWER, open).commit();
    }

    public static boolean getQuesAnswerConfig(Context context) {
        return getSharedPreferences(context).getBoolean(DOCTOR_QUES_ANSWER, false);
    }

    public static String getChangeNameMime(Context context) {
        return getSharedPreferences(context).getString(CHANGE_NAME_MIME, "");
    }

    public static void saveChangeNameMime(Context context, String name) {
        getEditor(context).putString(CHANGE_NAME_MIME, name).commit();
    }

    public static String getChangeDepartmentMime(Context context) {
        return getSharedPreferences(context).getString(CHANGE_DEPARTMENT_MIME, "");
    }

    public static void saveChangeDepartmentMime(Context context, String department) {
        getEditor(context).putString(CHANGE_DEPARTMENT_MIME, department).commit();
    }
}
