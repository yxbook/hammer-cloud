package com.fmkj.user.server.util;

import com.alibaba.fastjson.JSONObject;
import com.fmkj.common.util.PropertiesUtil;
import com.fmkj.user.dao.domain.HcAccount;

import java.util.HashMap;

public class JDWXUtil {
	
	/**
	 * 身份认证检测
	 * @param ha
	 * @return
	 */
	public static synchronized Boolean cardRealName(HcAccount ha){
		RequestModel model = new RequestModel();
		model.setGwUrl(PropertiesUtil.getInstance("user").get("cardPath"));
		model.setAppkey(PropertiesUtil.getInstance("user").get("cardPwd"));
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("cardNo", ha.getCardnum()); // 访问参数
		queryMap.put("realName", ha.getName()); // 访问参数
		model.setQueryParams(queryMap);
		WxApiCall call = new WxApiCall();
		call.setModel(model);
		String request = call.request();

		JSONObject jb = JSONObject.parseObject(request);

		Object code = jb.get("code");
		if (!"10000".equals(code)) {
			return false;
		}
		JSONObject result = (JSONObject) jb.get("result");

		JSONObject resp = (JSONObject) result.get("result");

		Boolean isok = (Boolean) resp.get("isok");
		if (!isok) {
			return false;
		}
		return true;
	}
	
	/**
	 * 敏感词汇检测
	 */
	public static synchronized String wordIsOk(String word) {
		RequestModel model = new RequestModel();
		model.setGwUrl(PropertiesUtil.getInstance("user").get("wordPath"));
		model.setAppkey(PropertiesUtil.getInstance("user").get("cardPwd"));
		HashMap<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("src",word); //访问参数
		model.setQueryParams(queryMap);
		WxApiCall call = new WxApiCall();
		call.setModel(model);
		String request = call.request();
		JSONObject jb = JSONObject.parseObject(request);

		Object code = jb.get("code");
		if (!"10000".equals(code)) {
			return "500";
		}
		JSONObject jsonObject = (JSONObject) jb.get("result");
		String result =  (String) jsonObject.get("result");
		return result;
	}
}
