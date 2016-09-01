package com.shenjianli.shenlib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 *
 **/
@SuppressLint("UseValueOf")
public class SharedPreUtil {
	/**
	 * 保存在手机里面的文件名
	 */
	public static final String SP_NAME = "config";
	public static final int SP_MODE = Context.MODE_PRIVATE;
	private static SharedPreferences sp;
	private static Editor editor;

	/**
	 * 根据类型调用不同的保存方法
	 * 
	 * @param context
	 *            上下文
	 * @param key
	 *            添加的键
	 * @param value
	 *            添加的值
	 * @return 是否添加成功（可以使用apply提交）
	 */
	public static boolean put(Context context, String key, Object value) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, SP_MODE);
		}
		editor = sp.edit();
		if (value == null) {
			editor.putString(key, null);
		} else if (value instanceof String) {
			editor.putString(key, (String) value);
		} else if (value instanceof Integer) {
			editor.putInt(key, (Integer) value);
		} else if (value instanceof Boolean) {
			editor.putBoolean(key, (Boolean) value);
		} else if (value instanceof Float) {
			editor.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			editor.putLong(key, (Long) value);
		} else {
			editor.putString(key, value.toString());
		}
		return editor.commit();
	}

	/**
	 * 根据默认值，得到保存的数据
	 * 
	 * @param context
	 * @param key
	 * @param value
	 * @return 具体类型，无匹配默认为空
	 */
//	public static Object getObject(Context context, String key,
//			Object defaultValue) {
//		if (sp == null) {
//			sp = context.getSharedPreferences(SP_NAME, SP_MODE);
//		}
//		if (defaultValue instanceof String) {
//			return sp.getString(key, (String) defaultValue);
//		} else if (defaultValue instanceof Integer) {
//			return sp.getInt(key, (Integer) defaultValue);
//		} else if (defaultValue instanceof Boolean) {
//			return sp.getBoolean(key, (Boolean) defaultValue);
//		} else if (defaultValue instanceof Float) {
//			return sp.getFloat(key, (Float) defaultValue);
//		} else if (defaultValue instanceof Long) {
//			return sp.getLong(key, (Long) defaultValue);
//		}
//
//		return null;
//	}
	@SuppressWarnings("unchecked")
	public static <T> T get(Context context, String key,
			Object defaultValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, SP_MODE);
		}
		if (defaultValue instanceof String) {
			return (T) sp.getString(key, (String) defaultValue);
		} else if (defaultValue instanceof Integer) {
			return (T) new Integer((sp.getInt(key, (Integer) defaultValue)));
		} else if (defaultValue instanceof Boolean) {
			return (T) new Boolean(sp.getBoolean(key, (Boolean) defaultValue));
		} else if (defaultValue instanceof Float) {
			return (T) new Float(sp.getFloat(key, (Float) defaultValue));
		} else if (defaultValue instanceof Long) {
			return (T) new Long(sp.getLong(key, (Long) defaultValue));
		}
		return null;
	}

	/**
	 * 清除数据
	 * @param context
	 * @return 是否清除成功(如果不关注结果，可以使用apply)
	 */
	public static boolean clear(Context context) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, SP_MODE);
		return sp.edit().clear().commit();
	}

}
