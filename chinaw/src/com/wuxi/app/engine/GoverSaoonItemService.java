package com.wuxi.app.engine;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.wuxi.app.util.Constants;
import com.wuxi.app.util.JAsonPaserUtil;
import com.wuxi.domain.GoverMaterials;
import com.wuxi.domain.GoverSaoonCFCL;
import com.wuxi.domain.GoverSaoonItem;
import com.wuxi.domain.GoverSaoonItemCFDetail;
import com.wuxi.domain.GoverSaoonItemQTDetail;
import com.wuxi.domain.GoverSaoonItemQZDetail;
import com.wuxi.domain.GoverSaoonItemWrapper;
import com.wuxi.domain.GoverSaoonItemXKDetail;
import com.wuxi.domain.GoverSaoonItemZSDetail;
import com.wuxi.exception.NODataException;
import com.wuxi.exception.NetException;

public class GoverSaoonItemService extends Service {

	public GoverSaoonItemService(Context context) {
		super(context);

	}

	/**
	 * 
	 * wanglu 泰得利通 通过部门获取办件信息 分页接口
	 * 
	 * @param deptId
	 * @param start
	 * @param end
	 * @return
	 * @throws JSONException
	 * @throws NetException
	 * @throws NODataException
	 */
	public GoverSaoonItemWrapper getGoverSaoonItemsByDeptId(String deptId,
			int start, int end) throws JSONException, NetException,
			NODataException {

		String url = Constants.Urls.GETITEM_BYDEPT_URL
				.replace("{deptid}", deptId).replace("{start}", start + "")
				.replace("{end}", end + "");

		
		return getGoverSaoonItemsByURL(url);
	}

	/**
	 * 
	 * 
	 * 获取办件信息多条件信息 wanglu 泰得利通
	 * 
	 * @param params
	 *            参数
	 * @return
	 * @throws JSONException
	 * @throws NetException
	 * @throws NODataException
	 */
	public GoverSaoonItemWrapper getGoverSaoonItemsByParas(
			Map<String, String> params) throws JSONException, NetException,
			NODataException {

		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> paramSet : params.entrySet()) {

			sb.append(paramSet.getKey()).append("=")
					.append(paramSet.getValue()).append("&");

		}

		sb.deleteCharAt(sb.length() - 1);// 删除最后一个字符

		String url = Constants.Urls.GETITEM_QUERY_URL + "?" + sb.toString();

		
		return getGoverSaoonItemsByURL(url);

	}

	/**
	 * 
	 * wanglu 泰得利通 根据URL获取办件列表
	 * 
	 * @param url
	 * @return
	 * @throws JSONException
	 * @throws NetException
	 * @throws NODataException
	 */
	public GoverSaoonItemWrapper getGoverSaoonItemsByURL(String url)
			throws JSONException, NetException, NODataException {

		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String resultStr = httpUtils.executeGetToString(url, 5000);
		if (resultStr != null) {
			JSONObject jsonObject = new JSONObject(resultStr);
			JSONObject jresult = jsonObject.getJSONObject("result");
			GoverSaoonItemWrapper goverSaoonItemWrapper = new GoverSaoonItemWrapper();
			goverSaoonItemWrapper.setEnd(jresult.getInt("end"));
			goverSaoonItemWrapper.setStart(jresult.getInt("start"));
			goverSaoonItemWrapper.setNext(jresult.getBoolean("next"));
			goverSaoonItemWrapper.setPrevious(jresult.getBoolean("previous"));
			goverSaoonItemWrapper.setTotalRowsAmount(jresult
					.getInt("totalRowsAmount"));
			
			Object o = jresult.get("data");

			if (!o.toString().equals("[]") && !o.equals("null")) {
				JSONArray jData = (JSONArray) o;
				try {
					goverSaoonItemWrapper.setGoverSaoonItems(JAsonPaserUtil
							.getListByJassory(GoverSaoonItem.class, jData));
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			return goverSaoonItemWrapper;

		} else {
			throw new NODataException(Constants.ExceptionMessage.NODATA_MEG);// 没有获取到数据异常
		}
	}

	/**
	 * 
	 * wanglu 泰得利通 根据kindType获取办件信息
	 * 
	 * @return
	 * @throws NODataException
	 * @throws NetException
	 * @throws JSONException
	 */
	public GoverSaoonItemWrapper getGoverSaoonItemsByKindType(String type,
			String kindType, int start, int end) throws JSONException,
			NetException, NODataException {
		String url = Constants.Urls.GETITEM_BYKINDTYPE_URL
				.replace("{type}", type).replace("{kindtype}", kindType)
				.replace("{start}", start + "").replace("{end}", end + "");

		
		return getGoverSaoonItemsByURL(url);
	}

	/**
	 * 
	 * wanglu 泰得利通 获取办件详情
	 * 
	 * @param type
	 *            分类
	 * @param id
	 *            主键
	 * @return
	 * @throws NetException
	 * @throws NODataException
	 * @throws JSONException
	 */
	public GoverSaoonItemXKDetail getGoverItemXKDetailById(String id)
			throws NetException, NODataException, JSONException {
		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.GETGOVER_ITEMDETIAL_XK_URL.replace("{id}",
				id);

		
		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);
		if (resultStr != null) {

			JSONObject jobject = new JSONObject(resultStr);
			JSONObject jresult = jobject.getJSONObject("result");
			GoverSaoonItemXKDetail goverSaoonItemDetail = new GoverSaoonItemXKDetail();
			goverSaoonItemDetail.setSszt(jresult.getString("sszt"));
			goverSaoonItemDetail.setSsztbm(jresult.getString("ssztbm"));
			goverSaoonItemDetail.setSsztxz(jresult.getString("ssztxz"));
			goverSaoonItemDetail.setWtjg(jresult.getString("wtjg"));
			goverSaoonItemDetail.setBslc(jresult.getString("bslc"));
			goverSaoonItemDetail.setFlfg(jresult.getString("flfg"));
			goverSaoonItemDetail.setFwzn(jresult.getString("fwzn"));
			goverSaoonItemDetail.setSfbz(jresult.getString("sfbz"));
			goverSaoonItemDetail.setSltj(jresult.getString("sltj"));
			goverSaoonItemDetail.setItemcode(jresult.getString("itemcode"));
			goverSaoonItemDetail.setSupertel(jresult.getString("supertel"));
			Object o = jresult.get("materials");
			if (!o.toString().equals("[]") && !o.toString().equals("null")) {
				JSONArray jmaterials = (JSONArray) o;
				try {
					List<GoverMaterials> meGoverMaterials = JAsonPaserUtil
							.getListByJassory(GoverMaterials.class, jmaterials);
					goverSaoonItemDetail.setGoverMaterials(meGoverMaterials);
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InstantiationException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (NoSuchMethodException e) {

				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}

			}

			goverSaoonItemDetail.setTimelimit(jresult.getString("timelimit"));
			goverSaoonItemDetail.setSlbm(jresult.getString("slbm"));
			goverSaoonItemDetail.setJdbm(jresult.getString("jdbm"));
			goverSaoonItemDetail.setLinktel(jresult.getString("linktel"));
			goverSaoonItemDetail.setBjtype(jresult.getString("bjtype"));
			goverSaoonItemDetail.setCert(jresult.getString("cert"));

			goverSaoonItemDetail.setCharge(jresult.getString("charge"));
			goverSaoonItemDetail.setZxbl(jresult.getString("zxbl"));
			goverSaoonItemDetail.setOtherAddr(jresult.getString("otherAddr"));
			goverSaoonItemDetail.setIswssb(jresult.getBoolean("iswssb"));
			goverSaoonItemDetail.setIsout(jresult.getBoolean("isout"));
			goverSaoonItemDetail.setOuturl(jresult.getString("outurl"));
			goverSaoonItemDetail.setName(jresult.getString("name"));
			goverSaoonItemDetail.setId(jresult.getString("id"));
			goverSaoonItemDetail.setDeptid(jresult.getString("deptid"));
			goverSaoonItemDetail.setDeptname(jresult.getString("deptname"));

			return goverSaoonItemDetail;

		} else {
			throw new NODataException(Constants.ExceptionMessage.NODATA_MEG);// 没有获取到数据异常
		}

	}

	/**
	 * 
	 * wanglu 泰得利通 获取行政征收信息
	 * 
	 * @param id
	 * @return
	 * @throws NetException
	 * @throws JSONException
	 */
	public GoverSaoonItemZSDetail getGoverSaoonItemZSDetailByid(String id)
			throws NetException, JSONException {

		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.GETGOVER_ITEMDETIAL_ZS_URL.replace("{id}",
				id);

		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);
		if (resultStr != null) {

			JSONObject jobject = new JSONObject(resultStr);
			JSONObject jresult = jobject.getJSONObject("result");

			try {
				GoverSaoonItemZSDetail goverSaoonItemZSDetail = JAsonPaserUtil
						.getBeanByJASSON(GoverSaoonItemZSDetail.class, jresult);
				return goverSaoonItemZSDetail;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	/**
	 * 
	 * wanglu 泰得利通 行政强制 详情
	 * 
	 * @param id
	 * @return
	 * @throws NetException
	 * @throws JSONException
	 */
	public GoverSaoonItemQZDetail getGoverSaoonItemQZDetailByid(String id)
			throws NetException, JSONException {

		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.GETGOVER_ITEMDETIAL_QZ_URL.replace("{id}",
				id);
		

		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);
		if (resultStr != null) {

			JSONObject jobject = new JSONObject(resultStr);
			JSONObject jresult = jobject.getJSONObject("result");

			try {
				GoverSaoonItemQZDetail goverSaoonItemQZDetail = JAsonPaserUtil
						.getBeanByJASSON(GoverSaoonItemQZDetail.class, jresult);
				return goverSaoonItemQZDetail;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

	/**
	 * 
	 * wanglu 泰得利通 QT获取
	 * 
	 * @param id
	 * @return
	 * @throws NetException
	 * @throws NODataException
	 * @throws JSONException
	 */
	public GoverSaoonItemQTDetail getGoverItemQTDetailById(String id)
			throws NetException, NODataException, JSONException {
		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.GETGOVER_ITEMDETIAL_QT_URL.replace("{id}",
				id);
		

		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);
		if (resultStr != null) {

			JSONObject jobject = new JSONObject(resultStr);
			JSONObject jresult = jobject.getJSONObject("result");
			GoverSaoonItemQTDetail goverSaoonItemQTDetail = new GoverSaoonItemQTDetail();
			goverSaoonItemQTDetail.setBbh(jresult.getString("bbh"));
			goverSaoonItemQTDetail.setBm(jresult.getString("bm"));
			goverSaoonItemQTDetail.setJc(jresult.getString("jc"));
			goverSaoonItemQTDetail.setLjdz(jresult.getString("ljdz"));
			goverSaoonItemQTDetail.setSfsbssp(jresult.getString("sfsbssp"));
			goverSaoonItemQTDetail.setSszt(jresult.getString("sszt"));
			goverSaoonItemQTDetail.setSsztxz(jresult.getString("ssztxz"));
			goverSaoonItemQTDetail.setSsztbm(jresult.getString("ssztbm"));
			goverSaoonItemQTDetail.setWtjg(jresult.getString("wtjg"));
			goverSaoonItemQTDetail.setXzqh(jresult.getString("xzqh"));
			goverSaoonItemQTDetail.setBldate(jresult.getString("bldate"));
			goverSaoonItemQTDetail.setBslc(jresult.getString("bslc"));
			goverSaoonItemQTDetail.setCbm(jresult.getString("cbm"));
			goverSaoonItemQTDetail.setCnsj(jresult.getString("cnsj"));
			goverSaoonItemQTDetail.setCnsjms(jresult.getString("cnsjms"));
			goverSaoonItemQTDetail.setFdcnsj(jresult.getString("fdcnsj"));
			goverSaoonItemQTDetail.setFlfg(jresult.getString("flfg"));
			goverSaoonItemQTDetail.setFwzn(jresult.getString("fwzn"));
			goverSaoonItemQTDetail.setIsfz(jresult.getString("isfz"));
			goverSaoonItemQTDetail.setIssf(jresult.getString("issf"));
			goverSaoonItemQTDetail.setIswssb(jresult.getString("iswssb"));
			goverSaoonItemQTDetail.setJdjg(jresult.getString("jdjg"));
			goverSaoonItemQTDetail.setLxdh(jresult.getString("lxdh"));
			goverSaoonItemQTDetail.setQtbldd(jresult.getString("qtbldd"));
			goverSaoonItemQTDetail.setSfbz(jresult.getString("sfbz"));
			goverSaoonItemQTDetail.setSfjbj(jresult.getString("sfjbj"));
			goverSaoonItemQTDetail.setSltj(jresult.getString("sltj"));
			goverSaoonItemQTDetail.setSqsljg(jresult.getString("sqsljg"));
			goverSaoonItemQTDetail.setXzfwzxbl(jresult.getString("xzfwzxbl"));

			Object o = jresult.get("materials");
			if (!o.toString().equals("[]") && !o.toString().equals("null")) {
				JSONArray jmaterials = (JSONArray) o;
				try {
					List<GoverMaterials> meGoverMaterials = JAsonPaserUtil
							.getListByJassory(GoverMaterials.class, jmaterials);
					goverSaoonItemQTDetail.setGoverMaterials(meGoverMaterials);
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InstantiationException e) {

					e.printStackTrace();
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (NoSuchMethodException e) {

				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}

			}

			goverSaoonItemQTDetail.setName(jresult.getString("name"));
			goverSaoonItemQTDetail.setId(jresult.getString("id"));
			goverSaoonItemQTDetail.setDeptid(jresult.getString("deptid"));
			goverSaoonItemQTDetail.setDeptname(jresult.getString("deptname"));

			return goverSaoonItemQTDetail;

		} else {
			throw new NODataException(Constants.ExceptionMessage.NODATA_MEG);// 没有获取到数据异常
		}

	}

	/**
	 * 
	 * wanglu 泰得利通 行政处罚详情
	 * 
	 * @param id
	 * @return
	 * @throws NetException
	 * @throws JSONException
	 */
	public GoverSaoonItemCFDetail getGoverSaoonItemCFDetailByid(String id)
			throws NetException, JSONException {

		if (!checkNet()) {
			throw new NetException(Constants.ExceptionMessage.NO_NET);
		}

		String url = Constants.Urls.GETGOVER_ITEMDETIAL_CF_URL.replace("{id}",
				id);
		

		String resultStr = httpUtils.executeGetToString(url, TIME_OUT);
		if (resultStr != null) {

			JSONObject jobject = new JSONObject(resultStr);
			JSONObject jresult = jobject.getJSONObject("result");

			try {
				GoverSaoonItemCFDetail goverSaoonItemCFDetail = JAsonPaserUtil
						.getBeanByJASSON(GoverSaoonItemCFDetail.class, jresult);
				Object o = jresult.get("clList");
				if (!o.toString().equals("null") && !o.toString().equals("[]")) {
					JSONArray jarray = (JSONArray) o;
					goverSaoonItemCFDetail.setGoverSaoonCFCLs(JAsonPaserUtil
							.getListByJassory(GoverSaoonCFCL.class, jarray));
				}

				return goverSaoonItemCFDetail;
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		return null;
	}

}
