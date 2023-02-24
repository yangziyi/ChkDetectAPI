package org.jt.ae.service;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jt.ae.dao.ApiDao;
import org.jt.common.utils.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 接口Service
 * @author mjn
 *
 */
@Service
public class ApiService {
	@Resource
	private ApiDao apiDao;
	private static Logger log= LoggerFactory.getLogger(ApiService.class);
	/**
	 * 1-1.NG分组最高TOP10
	 * @param param
	 * @return
	 */
	public String NGByGroup(Map<String, String> param) {
		Map result=new HashMap();
		try{
			if(param.get("sort")==null)
				param.put("sort","asc" );
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if( param.get("hours")==null){
				if(!validateDate(param)){
					throw new Exception("日期参数或时间段参数不能都为空");
				}
			}
			List data=apiDao.NGByGroup(param);
			if(data!=null) {
				result.put("data", data);
				System.out.println(data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	/**
	 * 1-2.NG设备最高TOP10
	 * @param param
	 * @return
	 */
	public String NGByMac(Map<String, String> param) {
		Map result=new HashMap();
		try{
			if(param.get("sort")==null)
				param.put("sort","asc" );
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if( param.get("hours")==null){
				if(!validateDate(param)){
					throw new Exception("日期参数或时间段参数不能都为空");
				}
			}
			List data=apiDao.NGByMac(param);
			if(data!=null) {
				result.put("data", data);
				System.out.println(data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 2-1.良品率分组最高TOP10
	 * @param param
	 * @return
	 */
	public String YieldRateByGroup(Map<String, String> param) {
		Map result=new HashMap();
		try{
			if(param.get("sort")==null)
				param.put("sort","asc" );
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if( param.get("hours")==null){
				if(!validateDate(param)){
					throw new Exception("日期参数或时间段参数不能都为空");
				}
			}
			List data=apiDao.YieldRateByGroup(param);
			if(data!=null) {
				result.put("data", data);
				System.out.println(data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 2-2.良品率设备高TOP10
	 * @param param
	 * @return
	 */
	public String YieldRateByMac(Map<String, String> param) {
		Map result=new HashMap();
		try{
			if(param.get("sort")==null)
				param.put("sort","asc" );
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if( param.get("hours")==null){
				if(!validateDate(param)){
					throw new Exception("日期参数或时间段参数不能都为空");
				}
			}
			List data=apiDao.YieldRateByMac(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}


	/**
	 * 5.良品率汇总统计
	 * @param param
	 * @return
	 */
	public String YieldRateByAll(Map<String, String> param) {
		Map result=new HashMap();
		try{
			List data=apiDao.YieldRateByAll(param);
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if( param.get("hours")==null){
				if(!validateDate(param)){
					throw new Exception("日期参数或时间段参数不能都为空");
				}
			}
			if(data!=null) {
				result.put("data", data);
			}
			 result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 全分组良率
	 * @param param
	 * @return
	 */
	public String YieldRateAll(Map<String, String> param) {
		Map result=new HashMap();
		try{
			/*if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}*/
			validateDate(param);
			List data=apiDao.YieldRateAll(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	/**
	 * 分组全设备良率
	 * @param param
	 * @return
	 */
	public String YieldRateUnit(Map<String, String> param) {
		Map result=new HashMap();
		try{
		/*	if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}*/
			if(param.get("unit_code")==null){
				throw new Exception("unit_code参数不能为空");
			}
			validateDate(param);
			List data=apiDao.YieldRateUnit(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	/**
	 * 班次良率
	 * @param param
	 * @return
	 */
	public String YieldRateShift(Map<String, String> param) {
		Map result=new HashMap();
		try{
			if(param.get("pro_code")==null){
				throw new Exception("pro_code参数不能为空");
			}
			if(param.get("unit_code")==null){
				throw new Exception("unit_code参数不能为空");
			}
			if(param.get("shift_name")==null){
				throw new Exception("shift_name参数不能为空");
			}
			validateDate(param);
			List data=apiDao.YieldRateShift(param);
			if(data!=null) {
				result.put("data",data );
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}



	public String PositionList(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.PositionList(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}

	public String UnitList(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.UnitList(param);
			if(data!=null) {
				result.put("data", data);
			}
			 result.put("code", 200);
		}catch(Exception ex){
			 result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}

	public String MacList(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			if(param.get("unit_code")==null){
				throw new Exception("unit_code参数不能为空");
			}
			List data=apiDao.MacList(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}
	public String ProList(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.ProList(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}

	/**
	 * 单机归类----机台良品率分布
	 * @param param
	 * @return
	 */
	public String YieldRatePercent(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRatePercent(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	/**
	 * 单机归类----机台良率最高/最低top20
	 * @param param
	 * @return
	 */
	public String YieldRateMac(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRateMac(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 组别归类----分组班次良率统计
	 * @param param
	 * @return
	 */
	public String YieldRateGroupShift(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRateGroupShift(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 机型归类----机型良率对比
	 * @param param
	 * @return
	 */
	public String YieldRateChkShif(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRateChkShif(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 测量机台归类-4--测量机台良率
	 * @param param
	 * @return
	 */
	public String YieldRateChkcode(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRateChkcode(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	public String YieldRatePosition(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRatePosition(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
	/**
	 * 时段归类--时段良率
	 * @param param
	 * @return
	 */
	public String YieldRateTime(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			List data=apiDao.YieldRateTime(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 检测结果列表
	 * @param param
	 * @return
	 */
	public String checkValueList(Map<String, String> param) {
		Map result=new HashMap();
		try{
			validateDate(param);
			String positons=	PropertyUtil.getValue("all_position");
			int max_lines=	Integer.parseInt(PropertyUtil.getValue("max_lines"));
			String[] pos_arr=positons.split(",");
			String all_position="'"+StringUtils.join(pos_arr,"','")+"'";
			int counts=apiDao.checkValueCount(param,all_position);
			if(counts>max_lines){
				throw new Exception("记录数为"+counts+",超过最大限制"+max_lines);
			}
			List<Map>data=apiDao.checkValueList(param,all_position);
			List list=new ArrayList();
			List title=new ArrayList();
			title.addAll(Arrays.asList("设备编号,产品编号,检测时间".split(",")));
			title.addAll(Arrays.asList(pos_arr));
			if(data!=null && data.size()>0) {
				list.add(title);
			}
			for(Map map:data){
				List data_arr=new ArrayList();
				data_arr.add(map.get("mac_code"));
				data_arr.add(map.get("product_code"));
				data_arr.add(map.get("chk_time"));
				Object[] chk_value_arr=(Object[])map.get("chk_value");
				for(String pos:pos_arr){
					boolean isfind=false;
					for(Object chk_value:chk_value_arr){
						String[] v_arr=chk_value.toString().split(":");
						if(pos.equals(v_arr[0])){
							data_arr.add(Float.parseFloat(v_arr[1]));
							isfind=true;
							break;
						}
					}
					if(!isfind){
						data_arr.add("");
					}
				}
				list.add(data_arr);
			}
			result.put("data", list);
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}
//##################孙素鹏############################	
	
	public String GetAllCPK(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.GetAllCPK(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}
	
	public String GetAllCPKByPos(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.GetAllCPKByPos(param);
			if(data!=null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}
	
	public String GetSPC(Map<String, String> param) {
		JSONObject result=new JSONObject();
		try{
			List data=apiDao.GetSPC(param);
			if(data!=null) {
				result.put("data", data);
			}
			 result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return result.toString();
	}

	private boolean validateDate(Map<String, String> param)throws Exception {
		if(param.get("start_time")==null && param.get("end_time")==null ){
			throw new Exception("日期参数不能都为空");
		}
		return true;
	}

	/**
	 * 各组检测结果数量分析
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public String teamCheckCount(Map<String, String> param) {
		String start_time = param.get("start_time");						//开始时间
		String end_time = param.get("end_time");							//结束时间
		String shift_name = param.get("shift_name");						//班组
		Map result = new HashMap();
		try{
			if(start_time == null || end_time == null){
				throw new Exception("起始时间信息不能为空");
			}
			List data = apiDao.teamCheckCount(param);
			if(data != null) {
				result.put("data", data);
				System.out.println(result);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 各组检测结果数量分析明细
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public String teamCheckCountDetail(Map<String, String> param) {
		String start_time = param.get("start_time");							//开始时间
		String end_time = param.get("end_time");								//结束时间
		String shift_name = param.get("shift_name");							//班组
		String product_code = param.get("product_code");						//产品编号

		Map result = new HashMap();
		try{
			if(start_time == null || end_time == null || shift_name == null || product_code == null){
				throw new Exception("起始时间及产品、班组信息均不能为空，请联系开发人员排查");
			}
			List data = apiDao.teamCheckCountDetail(param);
			if(data!=null) {
				result.put("data", data);
				System.out.println(data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 查询班组列表
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public String queryShiftName(Map<String, String> param) {
		Map result = new HashMap();
		try{
			List data = apiDao.queryShiftName(param);
			if(data != null) {
				result.put("data", data);
				System.out.println(result);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 查询班组列表
	 * @author yzy
	 * @date 2020-05-30
	 * @return
	 */
	public String queryMac_status(Map<String, String> param) {
		Map result = new HashMap();
		try{
			List data = apiDao.queryMac_status(param);
			if(data != null) {
				result.put("data", data);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 查询案例分析最近50组数据
	 * @author yzy
	 * @date 2020-06-08
	 * @return
	 */
	public String queryCase_analysis(Map<String, String> param) {
		String product_code = param.get("product_code");								//产品编号
		String mac_code = param.get("mac_code");										//设备编号
		String focus_type = param.get("focus_type");									//设备编号
		String shopCode = param.get("shopCode").replace("-", "_");
		Map result = new HashMap();
		try{
			List<Map<String, Object>> data = apiDao.queryCase_analysis(product_code, mac_code, focus_type, shopCode);
			ArrayList<Map<String, Object>> data2 = new ArrayList<Map<String, Object>>();
			data.forEach(xs -> {
				Map result2 = new HashMap();
				result2.put("key", xs.get("detect.position").toString());			//尺寸位置
				result2.put("std_value", xs.get("detect.std_value"));	//标准值
				result2.put("usl", xs.get("detect.usl"));				//上限值
				result2.put("lsl", xs.get("detect.lsl"));				//下限值
				result2.put("data", queryCase_analysis_detail(product_code, mac_code, xs.get("detect.position").toString(), shopCode));
				data2.add(result2);
			});
			//System.out.println(data2);
			result.put("data", data2);
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

	/**
	 * 查询案例分析最近50组数据明细
	 * @author yzy
	 * @date 2020-06-08
	 * @param product_code	产品编号
	 * @param mac_code		设备编号
	 * @param position		尺寸位置
	 * @return
	 */
	public List queryCase_analysis_detail(String product_code, String mac_code, String position, String shopCode) {
		return Lists.reverse(apiDao.queryCase_analysis_detail(product_code, mac_code, position, shopCode));
	}

	/**
	 * 查询机床列表
	 * @author yzy
	 * @date 2020-06-08
	 * @return
	 */
	public String queryMacCode(Map<String, String> param) {
		Map result = new HashMap();
		try{
			List data = apiDao.queryMacCode(param);
			if(data != null) {
				result.put("data", data);
				System.out.println(result);
			}
			result.put("code", 200);
		}catch(Exception ex){
			result.put("code", 500);
			result.put("data", new Object[]{});
			result.put("msg", ex.getMessage());
			log.debug(ex.getMessage());
		}
		return JSONObject.fromMap(result).toString();
	}

}
