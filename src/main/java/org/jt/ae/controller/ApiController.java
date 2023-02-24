package org.jt.ae.controller;

import java.util.Map;

import javax.annotation.Resource;

import org.jt.ae.service.ApiService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口Controller
 * @author mjn
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	@Resource
	ApiService apiService;
	/**
	 * 分组列表
	 * @return
	 */
	@RequestMapping("/unit_list")
	public String UnitList(@RequestBody Map<String,String> param){
		return apiService.UnitList(param);
	}
	/**
	 * 产品列表
	 * @return
	 */
	@RequestMapping("/pro_list")
	public String ProList(@RequestBody Map<String,String> param){
		return apiService.ProList(param);
	}

	/**
	 * 机台列表
	 * @return
	 */
	@RequestMapping("/mac_list")
	public String MacList(@RequestBody Map<String,String> param){
		return apiService.MacList(param);
	}

	/**
	 * 尺寸列表
	 * @return
	 */
	@RequestMapping("/position_list")
	public String PositionList(@RequestBody Map<String,String> param){
		return apiService.PositionList(param);
	}

	/**
	 * 2、分组良品率TOP10
	 * @return
	 */
	@RequestMapping("/yield_group")
	public String YieldRateByGroup(@RequestBody  Map<String,String>param){
		return apiService.YieldRateByGroup(param);
	}
	/**
	 * 3、设备分组良品率TOP10
	 * @return
	 */
	@RequestMapping("/yield_mac")
	public String YieldRateByMac(@RequestBody  Map<String,String>param){
		return apiService.YieldRateByMac(param);
	}
	/**
	 * 2-1、NG最高分组TOP10
	 * @return
	 */
	@RequestMapping("/ng_group")
	public String NGByGroup(@RequestBody  Map<String,String>param){
		return apiService.NGByGroup(param);
	}
	/**
	 * 2-2、NG最高设备TOP10
	 * @return
	 */
	@RequestMapping("/ng_mac")
	public String NGByMac(@RequestBody  Map<String,String>param){
		return apiService.NGByMac(param);
	}

	/**
	 * 5.良品率汇总统计
	 * @return
	 */
	@RequestMapping("/yield_in_all")
	public String YieldByAll(@RequestBody  Map<String,String>param){
		return apiService.YieldRateByAll(param);
	}


	/**
	 *6、全分组良率yield_mac
	 * @return
	 */
	@RequestMapping("/yield_all")
	public String YieldAll(@RequestBody  Map<String,String>param){
		return apiService.YieldRateAll(param);
	}

	/**
	 * 7、设备良率
	 * @return
	 */
	@RequestMapping("/yield_all_mac")
	public String YieldMac(@RequestBody  Map<String,String>param){
		return apiService.YieldRateUnit(param);
	}

	/**
	 * 8、单机归类----机台良品率分布
	 * @return
	 */
	@RequestMapping("/yield_percent")
	public String YieldRatePercent(@RequestBody  Map<String,String>param){
		return apiService.YieldRatePercent(param);
	}

	/**
	 * 8-2、单机归类----机台良率最高/最低top20
	 * @return
	 */
	@RequestMapping("/yield_rate_mac")
	public String YieldRateMac(@RequestBody  Map<String,String>param){
		return apiService.YieldRateMac(param);
	}

	/**
	 * 9、班次良率
	 * @return
	 */
	@RequestMapping("/yield_shift")
	public String YieldShift(@RequestBody  Map<String,String>param){
		return apiService.YieldRateShift(param);
	}

	/**
	 * 10、组别归类----分组班次良率统计
	 * @return
	 */
	@RequestMapping("/yield_group_shift")
	public String YieldRateGroupShift(@RequestBody  Map<String,String>param){
		return apiService.YieldRateGroupShift(param);
	}

	/**
	 * 11、机型归类----机型良率对比
	 * @return
	 */
	@RequestMapping("/yield_chk_shift")
	public String YieldRateChkShif(@RequestBody  Map<String,String>param){
		return apiService.YieldRateChkShif(param);
	}
	/**
	 * 12、测量机台归类-4--测量机台良率
	 * @return
	 */
	@RequestMapping("/yield_chk")
	public String YieldRateChkcode(@RequestBody  Map<String,String>param){
		return apiService.YieldRateChkcode(param);
	}
	/**
	 * 13、单项尺寸良率
	 * @return
	 */
	@RequestMapping("/yield_position")
	public String YieldRatePosition(@RequestBody  Map<String,String>param){
		return apiService.YieldRatePosition(param);
	}
	/**
	 * 13、时段归类--时段良率
	 * @return
	 */
	@RequestMapping("/yield_time")
	public String YieldRateTime(@RequestBody  Map<String,String>param){
		return apiService.YieldRateTime(param);
	}

	/**
	 * 14、检测结果列表
	 * @param param
	 * @return
	 */
	@RequestMapping("/check_value_list")
	public String checkValueList(@RequestBody  Map<String,String>param){
		return apiService.checkValueList(param);
	}
	//###########################孙素鹏###################################	
	@RequestMapping("/cpk/getAllCPK")
	public String GetAllCPK(@RequestBody  Map<String,String>param){
		return apiService.GetAllCPK(param);
	}
	
	@RequestMapping("/cpk/getPostCPK")
	public String GetAllCPKByPos(@RequestBody  Map<String,String>param){
		return apiService.GetAllCPKByPos(param);
	}
	
	@RequestMapping("/cpk/getSPC")
	public String GetSPC(@RequestBody  Map<String,String>param){
		return apiService.GetSPC(param);
	}

	/**
	 * 15、各组检测NG数量
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	@RequestMapping(value = "/team_check_count", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String teamCheckCount(@RequestBody  Map<String,String> param){
		return apiService.teamCheckCount(param);
	}

	/**
	 * 16、各组检测NG数量明细
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	@RequestMapping(value = "/team_check_count_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String teamCheckValueList(@RequestBody  Map<String,String> param){
		return apiService.teamCheckCountDetail(param);
	}

	/**
	 * 17、查询班组列表
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	@RequestMapping(value = "/queryShiftName", method = RequestMethod.POST)
	public String queryShiftName(@RequestBody Map<String,String> param){
		return apiService.queryShiftName(param);
	}

	/**
	 * 18、查询机台历史运行状态
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	@RequestMapping(value = "/queryMac_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String queryMac_status(@RequestBody  Map<String,String> param){
		return apiService.queryMac_status(param);
	}

	/**
	 * 19、案例分析
	 * @author yzy
	 * @date 2020-06-08
	 * @return
	 */
	@RequestMapping(value = "/queryCase_analysis", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String queryCase_analysis(@RequestBody  Map<String,String> param){
		return apiService.queryCase_analysis(param);
	}

	/**
	 * 20、查询机床列表
	 * @author yzy
	 * @date 2020-06-16
	 * @return
	 */
	@RequestMapping(value = "/queryMacCode", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
	public String queryMacCode(@RequestBody Map<String,String> param){
		return apiService.queryMacCode(param);
	}


}
