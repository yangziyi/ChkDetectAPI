package org.jt.ae.dao;

import java.util.*;

import org.jt.common.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

/**
 * 接口DAO
 * @author mjn
 *
 */
@Service
public class ApiDao {
	@Autowired
    private JdbcTemplate jdbcTemplate;
	/**
	 * 1-1.NG最高分组TOP10
	 * @param param
	 * @return
	 */
	public List NGByGroup(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object sort=param.get("sort");
		Object pro_code=param.get("pro_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select "+
				"a.group_code,toUInt32(count(*)) as qr " +
				"from quality.check_detect_" + shopCode + " as a  " +
				"where a.chk_result='NG' "+
				" and product_code='"+pro_code+"'"+
				(hours!=null?(" and a.chk_time>=subtractHours(now(),"+hours+") "):(start_time!=null&&end_time!=null?(" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "):""))+
				" and a.chk_type=1 " +
				" group by a.group_code " +
				" order by qr "+sort +
				" limit 10";

		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 1-2.NG最高设备TOP10
	 * @param param
	 * @return
	 */
	public List NGByMac(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object group_code=param.get("group_code");
		Object pro_code=param.get("pro_code");
		Object sort=param.get("sort");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select  " +
				"a.mac_code,toUInt32(count(*)) as qr " +
				"from quality.check_detect_" + shopCode + " as a  " +
				"where a.chk_result='NG' "+
				" and product_code='"+pro_code+"'"+
				(group_code!=null?" and group_code='"+group_code+"'":"")+
				(hours!=null?(" and a.chk_time>=subtractHours(now(),"+hours+") "):(start_time!=null&&end_time!=null?(" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "):""))+
				" and a.chk_type=1 " +
				" group by a.mac_code " +
				" order by qr "+sort +
				" limit 10";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 2-1.分组良品率TOP10
	 * @param param
	 * @return
	 */
	public List YieldRateByGroup(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object sort=param.get("sort");
		Object pro_code=param.get("pro_code");
		Object postArray=param.get("postArray");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select group_code,sum(multiIf(chk_result='OK',1,0))/count(*) as qr  from quality.check_detect_" + shopCode + " as a  array join detect as b" +
				" where a.product_code='"+pro_code+"'  " +
						 " and  a.chk_type=1 " +
				(hours!=null?(" and a.chk_time>=subtractHours(now(),"+hours+") "):(start_time!=null&&end_time!=null?(" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "):""))+
				(postArray!=null&&((String) postArray).length()>0?(" and b.position in ("+postArray+")"):"")+
				" group by  group_code order by qr "+sort+
				" limit 10";
		System.out.println("分组良品率TOP10 : " + sql);
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 2-2.设备分组良品率TOP10
	 * @param param
	 * @return
	 */
	public List YieldRateByMac(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object group_code=param.get("group_code");
		Object pro_code=param.get("pro_code");
		Object sort=param.get("sort");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select mac_code,sum(multiIf(chk_result='OK',1,0))/count(*) as qr  from quality.check_detect_" + shopCode + " as a " +
				" where a.product_code='"+pro_code+"' " + 
						" and  a.chk_type=1 " +
				(hours!=null?(" and a.chk_time>=subtractHours(now(),"+hours+") "):(start_time!=null&&end_time!=null?(" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "):""))+
				(group_code!=null?"  and a.group_code='"+group_code+"' ":"") +
				" group by  mac_code order by qr "+sort+
				" limit 10";
		System.out.println("YieldRateByMac:" + sql);
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 3.良品率汇总统计
	 * @param param
	 * @return
	 */
	public List YieldRateByAll(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object pro_code=param.get("pro_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select qr,toUInt32(sum(counts)) counts from( " +
				" select qr,count(qr)counts from( " +
				" select case when qr>=0.8 then 0.8 " +
				"    when qr>=0.75 then 0.75 " +
				"   when qr>=0.7 then 0.7 " +
				"   when qr>=0.6 then 0.6 " +
				"   else 0.5  end qr  from( " +
				"select mac_code,sum(multiIf(chk_result='OK',1,0))/count(*) as qr  from quality.check_detect_" + shopCode + " as a " +
				" where a.product_code='"+pro_code+"' " +
						 " and  a.chk_type=1 " +
				(hours!=null?(" and a.chk_time>=subtractHours(now(),"+hours+") "):(start_time!=null&&end_time!=null?(" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "):""))+
				" group by  mac_code "+
				"))  group by qr " +
				") group by qr order by qr desc";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 产品列表
	 * @return
	 */
	public List ProList(Map<String, String> param) {
		String sql=null;
		Object shopCode=param.get("shopCode").replace("-", "_");
		sql="select  product_code  from quality.check_detect_" + shopCode + " group by product_code  order by product_code desc";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 全分组良率
	 * @param param
	 * @return
	 */
	public List YieldRateAll(Map<String, String> param){
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object pro_code=param.get("pro_code");
		Object postArray=param.get("postArray");
		//Object include=param.get("include");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select  group_code,sum(multiIf(chk_result='OK',1,0))/count(*) qr  from quality.check_detect_" + shopCode + " as a  array join detect as b  " +
			" where 0=0"+//a.product_code='"+pro_code+"' " +
				(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
			" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
			(postArray!=null&&((String) postArray).length()>0?(" and b.position in ("+postArray+")"):"")+
			 " and  a.chk_type=1 " +
			//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
			" group by group_code order by sum(multiIf(chk_result='OK',1,0))";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 全设备良率
	 * @param param
	 * @return
	 */
	public List YieldRateUnit(Map<String, String> param){
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object pro_code=param.get("pro_code");
		Object unit_code=param.get("unit_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select mac_code,sum(multiIf(chk_result='OK',1,0))/count(*)  qr from quality.check_detect_" + shopCode + " as a "+
			" where 0=0"+//a.product_code='"+pro_code+"' " +
			//yzy-200814-mark
			//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
			(pro_code != null && pro_code != ""?(" and a.product_code='"+pro_code+"' "):"")+
			" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
			 " and  a.chk_type=1 " +
			//yzy-210422-mark
			//" and a.group_code='"+unit_code+"' " +
			(unit_code != null && unit_code != ""?(" and a.group_code='"+unit_code+"' "):"")+
			" group by mac_code order by sum(multiIf(chk_result='OK',1,0))";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 班次良率
	 * @param
	 * @return
	 */
	public List UnitList(Map<String, String> param){
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select group_code from quality.check_detect_" + shopCode + "   group by group_code order by group_code" ;
		return jdbcTemplate.queryForList(sql);
	}

	public List MacList(Map<String, String> param) {
		String sql=null;
		Object unit_code=param.get("unit_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		sql="select distinct mac_code from quality.check_detect_" + shopCode + "  where group_code='"+unit_code+"'" ;
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 尺寸列表
	 * @return
	 */
	public List PositionList(Map<String, String> param) {
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
	//	sql="select  distinct b.position  position from quality.check_detect as a array join detect as  b where toDate(a.chk_date)>toDate('2019-10-25') order by b.position ";
		sql="select  distinct b.position  position from quality.check_detect_" + shopCode + " as a array join detect as  b order by b.position limit 833,205";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 班次良率
	 * @param param
	 * @return
	 */
	public List YieldRateShift(Map<String, String> param){
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object pro_code=param.get("pro_code");
		Object unit_code=param.get("unit_code");
		Object shift_name=param.get("shift_name");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;

		sql="select toString(chk_date) chk_date,sum(multiIf(chk_result='OK',1,0))/count(*)  qr from "
			+"(select chk_result, multiIf(toHour(chk_time)>=0 and toHour(chk_time)<7,chk_date-1,chk_date) chk_date, multiIf(toHour(chk_time)>=7 and toHour(chk_time)<19,1,0) shift_name from quality.check_detect_" + shopCode + " "
			//yzy-210422-mark
			//+" where  product_code='"+pro_code+"' and group_code='"+unit_code+"'"
			//yzy-210422-start
			+" where  product_code='"+pro_code+"' " 
			+ (unit_code != null && unit_code != ""?(" and a.group_code='"+unit_code+"' "):"") 
			//yzy-210422-end
			+" and chk_time >= toDate('"+start_time+"') and chk_time <= toDate('"+end_time+"') and chk_type=1 ) "
			+" where shift_name="+shift_name+" group by  chk_date order by chk_date";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 单机归类----机台良品率分布
	 * @param param
	 * @return
	 */
	public List YieldRatePercent(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object unit_code=param.get("unit_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		//yzy-210422-mark
//		sql=" select qr,toUInt32(count(qr)) counts from(  " +
//				"select case when qr>=0.9 then 0.9    " +
//				" when qr>=0.8 then 0.8" +
//				" when qr>=0.75 then 0.75" +
//				" when qr>=0.7 then 0.7" +
//				" when qr>=0.6 then 0.6  else 0.5  end qr  " +
//				" from (select a.mac_code,sum(multiIf(b.chk_result='OK',1,0))/count(b.chk_result) as qr  " +
//				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b " +
//				" where a.chk_type=1 " +
//				" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"')" +
//				//yzy-200814-mark
//				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
//				//(unit_code!=null?(" and a.group_code='"+unit_code+"' "):"") +
//				(pro_code != null && pro_code != "" ?(" and a.product_code='"+pro_code+"' "):"")+
//				(unit_code != null && unit_code != ""?(" and a.group_code='"+unit_code+"' "):"") +
//				//yzy-2020/06/23-mark
//				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
//				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
//				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
//				//yzy-2020/06/23-end
//				" group by  mac_code ) ) group by qr order by qr desc";
		sql=" select qr,toUInt32(count(qr)) counts from(  " +
				"select case when qr>=0.9 then 0.9    " +
				" when qr>=0.8 then 0.8" +
				" when qr>=0.75 then 0.75" +
				" when qr>=0.7 then 0.7" +
				" when qr>=0.6 then 0.6  else 0.5  end qr  " +
				" from (select a.mac_code,sum(multiIf(a.chk_result='OK',1,0))/count(a.chk_result) as qr  " +
				" from quality.check_detect_" + shopCode + " as a " +
				" where a.chk_type=1 " +
				" and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"')" +
				(pro_code != null && pro_code != "" ?(" and a.product_code='"+pro_code+"' "):"")+
				(unit_code != null && unit_code != ""?(" and a.group_code='"+unit_code+"' "):"") +
				" group by  mac_code ) ) group by qr order by qr desc";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 单机归类----机台良率最高/最低top20
	 * @param param
	 * @return
	 */
	public List YieldRateMac(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		Object pro_code=param.get("pro_code");
		Object unit_code=param.get("unit_code");
		Object sort=param.get("sort");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select mac_code,sum(multiIf(chk_result='OK',1,0))/count(*) as qr  from quality.check_detect_" + shopCode + " as a " +
				" where  a.chk_type=1 and a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				//yzy-200814-mark
				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//(unit_code!=null?(" and a.group_code='"+unit_code+"' "):"") +
				(pro_code != null && pro_code != ""?(" and a.product_code='"+pro_code+"' "):"")+
				(unit_code != null && unit_code != ""?(" and a.group_code='"+unit_code+"' "):"") +
				" group by  mac_code order by qr "+sort+
				" limit 20";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 组别归类----分组班次良率统计
	 * @param param
	 * @return
	 */
	public List YieldRateGroupShift(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select a.group_code," +
				"a.shift_name," +
				"floor(toFloat64(sum(multiIf(chk_result='OK',1,0))/count()), 4) qr " +
				" from " +
				"(select a.chk_result,a.group_code," +
				" multiIf(toHour(a.chk_time) >= 7 and toHour(a.chk_time) < 19,'A班','B班') shift_name " +
				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b " +
				" where toDateTime(a.chk_time)>=toDateTime('"+start_time+"') " + 
				"and toDateTime(a.chk_time)<=toDateTime('"+end_time+"') "+
				" and  a.chk_type!=2 " +
				//yzy-200814-mark
				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				(pro_code != null && pro_code != ""?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				") a " +
				" group by a.group_code,a.shift_name " +
				" order by a.group_code,a.shift_name";
		
		//yzy-2020/06/24-add 需新增分组班次合计良率
		sql += " union all " +
				"select a.group_code," +
				"'合计'," +
				"floor(toFloat64(sum(multiIf(chk_result='OK',1,0))/count()), 4) qr " +
				" from " +
				"(select a.chk_result,a.group_code " +
				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b " +
				" where toDateTime(a.chk_time)>=toDateTime('"+start_time+"') " + 
				"and toDateTime(a.chk_time)<=toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				//yzy-200814-mark
				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				(pro_code!=null && pro_code != ""?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				") a " +
				" group by a.group_code " +
				" order by a.group_code";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 机型归类----机型良率
	 * @param param
	 * @return
	 */
	public List YieldRateChkShif(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object group_code=param.get("group_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select a.mac_type," +
				"a.shift_name," +
				"sum(multiIf(chk_result='OK',1,0))/count(*) qr " +
				"from (select a.chk_result," +
				" mac_type, " +
				" multiIf(toHour(a.chk_time)>=0 and toHour(a.chk_time)<7,a.chk_date-1,a.chk_date) chk_date, " +
				" multiIf(toHour(a.chk_time)>=7 and toHour(a.chk_time)<19,'A班','B班') shift_name " +
				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b" +
				" where a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				//yzy-200814-mark
				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//(group_code!=null?(" and a.group_code='"+group_code+"' "):"") +
				(pro_code!=null && pro_code != "" ?(" and a.product_code='"+pro_code+"' "):"")+
				(group_code != null && group_code != "" ?(" and a.group_code='"+group_code+"' "):"") +
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				") a " +
				" group by a.mac_type,shift_name " +
				" order by a.shift_name,a.mac_type";

		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 机台单项尺寸良率
	 * @param param
	 * @return
	 */
	public List YieldRatePosition(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object group_code=param.get("group_code");
		Object mac_code=param.get("mac_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select b.position position," +
				"sum(multiIf(b.chk_result='OK',1,0))/count(b.chk_result) qr " +
				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b  " +
				" where a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				//yzy-200814-mark
				//(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//(group_code!=null?(" and a.group_code='"+group_code+"' "):"") +
				//(mac_code!=null?(" and a.mac_code='"+mac_code+"' "):"") +
				(pro_code != null && pro_code != "" ? (" and a.product_code='"+pro_code+"' "):"")+
				(group_code != null && group_code != "" ?(" and a.group_code='"+group_code+"' "):"") +
				(mac_code != null && mac_code != "" ?(" and a.mac_code='"+mac_code+"' "):"") +
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				" group by b.position";
		return jdbcTemplate.queryForList(sql);
	}



	/**
	 * 测量机台归类-4--检测机台良率
	 * @param param
	 * @return
	 */
	public List YieldRateChkcode(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select a.chk_equ_code," +
				"sum(multiIf(chk_result='OK',1,0))/count(*) qr " +
				" from  quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b" +
				" where a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				" group by a.chk_equ_code" +
				" order by a.chk_equ_code";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 时段归类--时段良率
	 * @param param
	 * @return
	 */
	public List YieldRateTime(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code=param.get("pro_code");
		Object group_code=param.get("group_code");
		Object shopCode=param.get("shopCode").replace("-", "_");
		String sql=null;
		sql="select " +
				" time," +
				" sum(multiIf(chk_result='OK',1,0))/count(*) qr," +
				" toUInt32(count(chk_result)) quantity " +
			" from(" +
				"select " +
					"case when toHour(a.chk_time)>=7 and toHour(a.chk_time)<9 then 0" +
					" when toHour(a.chk_time)>=9 and toHour(a.chk_time)<11 then 1" +
					" when toHour(a.chk_time)>=11 and toHour(a.chk_time)<13 then 2" +
					" when toHour(a.chk_time)>=13 and toHour(a.chk_time)<15 then 3" +
					" when toHour(a.chk_time)>=15 and toHour(a.chk_time)<17 then 4" +
					" when toHour(a.chk_time)>=17 and toHour(a.chk_time)<19 then 5" +
					" when toHour(a.chk_time)>=19 and toHour(a.chk_time)<21 then 6" +
					" when toHour(a.chk_time)>=21 and toHour(a.chk_time)<23 then 7" +
					" when toHour(a.chk_time)>=23 or( toHour(a.chk_time)>=0 and toHour(a.chk_time)<1) then 8" +
					" when toHour(a.chk_time)>=1 and toHour(a.chk_time)<3 then 9" +
					" when toHour(a.chk_time)>=3 and toHour(a.chk_time)<5 then 10" +
					" when toHour(a.chk_time)>=5 and toHour(a.chk_time)<7 then 11" +
					//yzy-2020/06/23-add
					" else 12" +
					" end time,a.chk_result " +
				//yzy-210422-mark
				//" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b" +
				" from quality.check_detect_" + shopCode + " as a " +
				" where a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				//yzy-210421-mark
				//(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				//(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				//yzy-2020/08/14-mark
				//(group_code!=null?(" and a.group_code='"+group_code+"' "):"") +
				//yzy-2020/06/23-add
				(group_code!=null && group_code != "" ? (" and a.group_code='"+group_code+"' "):"") +
				//yzy-2020/06/23-end
			") group by time order by  time";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 检测结果列表
	 * * @param param
	 * @return
	 */
	public List checkValueList(Map<String, String> param,String all_position) {
		Object start_time = param.get("start_time");
		Object end_time = param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code = param.get("pro_code");
		Object shopCode = param.get("shopCode").replace("-", "_");
		String sql = null;
		sql="select " +
				"a.mac_code," +
				"a.product_code," +
				"toString(a.chk_time) chk_time," +
				"groupUniqArray(concat(b.position,':',toString(b.chk_value))) as chk_value  " +
			" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b " +
			" where b.position in("+all_position+")  "
				+ "and  a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
			" group by a.mac_code,a.product_code,a.chk_time order by a.mac_code";
		return jdbcTemplate.queryForList(sql);
	}
	/**
	 * 检测结果总数
	 * * @param param
	 * @return
	 */
	public int checkValueCount(Map<String, String> param, String all_position) {
		Object start_time = param.get("start_time");
		Object end_time = param.get("end_time");
		//yzy-2020/06/23-mark
		//Object include=param.get("include");
		String focus_type = param.get("focus_type");
		ArrayList<String> posi_code = new ArrayList<String>();
		String posi = "";
		if(!param.get("posi_code").toString().isEmpty()) {
			for(String s : Arrays.asList(param.get("posi_code").toString().split(","))) {
				posi_code.add(s);
			}
			for(String s : posi_code){
				posi += "'"+ s +"',";
			}
			posi = posi.substring(0, posi.length() - 1);
		}
		//yzy-2020/06/23
		Object pro_code = param.get("pro_code");
		Object shopCode = param.get("shopCode").replace("-", "_");
		String sql = null;
		sql="select count(0) c from (select " +
				"a.mac_code," +
				"a.product_code," +
				"toString(a.chk_time) chk_time " +
				" from quality.check_detect_" + shopCode + " as a ARRAY JOIN detect as b " +
				" where b.position in("+all_position+")  "
				+ "and  a.chk_time >= toDateTime('"+start_time+"') and a.chk_time <= toDateTime('"+end_time+"') "+
				" and  a.chk_type=1 " +
				(pro_code!=null?(" and a.product_code='"+pro_code+"' "):"")+
				//yzy-2020/06/23-mark
				//(include!=null && Integer.parseInt((String) include)==0?(" and b.position!='弧高'"):"")+
				(focus_type.equals("Y") ? " and b.focus_type = 1 " : " ") +
				(!posi.isEmpty() ? " and b.position in ("+ posi +")" : "") +
				//yzy-2020/06/23-end
				" group by a.mac_code,a.product_code,a.chk_time )";
		return jdbcTemplate.queryForObject(sql,Integer.class);
	}

//#############################孙素鹏####################################
	/**
	 * 根据条件查询全车间CPK
	 * @param param
	 * @return
	 */
	public List GetAllCPK(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object pro_code=param.get("pro_code");
		Object postArray=param.get("postArray");
		Object shopCode = param.get("shopCode").replace("-", "_");
		String sql=null;
		sql=" select position,CPK from ( select " +
				"	t1.t1_pro_code as pro_code, " + 
				"	t1.all_pos position, " +
				"	max(t2.spc_avg_val) spc_avg_val, " + 
				"	sqrt(sum(pow(t1.all_val - t2.spc_avg_val, 2)) / (count(1)-1)) spc_stddev, " + 
				"	max(t1.all_usl) as usl, " + 
				"	max(t1.all_lsl) as lsl, " + 
				"	(abs(usl - spc_avg_val)/(3.0 * spc_stddev )) spc_cpu, " + 
				"	(abs(spc_avg_val - lsl)/(3.0 * spc_stddev )) spc_cpl, " + 
				"	toDecimal32(if(spc_cpl > spc_cpu, " + 
				"	spc_cpu, " + 
				"	spc_cpl),3) as CPK " + 
				"from " + 
				"	( " + 
				"	select " + 
				"		a.product_code t1_pro_code, " + 
				"		b.position all_pos, " + 
				"		b.chk_value all_val, " + 
				"		(b.usl) all_usl, " + 
				"		(b.lsl) all_lsl " + 
				"	from " + 
				"		quality.check_detect_" + shopCode + " a ARRAY " + 
				"	join detect as b " + 
				"	where " + 
				"		t1_pro_code = '" + pro_code + "' and all_pos in " +  postArray  + 
				"		and all_pos != '' and b.usl !=0 and b.lsl !=0 and a.chk_result = 'OK' " + 
				(start_time!=null&&end_time!=null?(" and (chk_time)>=toDateTime('"+start_time+"') and (chk_time)<=toDateTime('"+end_time+"') ")
						:(" and chk_time>=subtractHours(now(),"+hours+") "))+
				"		 ) t1 " + 
				"inner join ( " + 
				"	select " + 
				"		a.product_code t2_pro_code, " + 
				"		b.position spc_avg_pos, " + 
				"		avg(b.chk_value) spc_avg_val " + 
				"	from " + 
				"		quality.check_detect_" + shopCode + " a 	ARRAY join detect as b" + 
				"	where " + 
				"		t2_pro_code = '" + pro_code + "' " + 
				"		and a.chk_result = 'OK' and spc_avg_pos != '' " + 
				(start_time!=null&&end_time!=null?(" and (chk_time)>=toDateTime('"+start_time+"') and (chk_time)<=toDateTime('"+end_time+"') ")
						:(" and chk_time>=subtractHours(now(),"+hours+") "))+
				"	group by " + 
				"		b.position, " + 
				"		a.product_code ) as t2 on " + 
				"	t1.all_pos = t2.spc_avg_pos " + 
				"	and t1.t1_pro_code = t2.t2_pro_code " + 
				"group by " + 
				"	t1.t1_pro_code, " + 
				"	t1.all_pos"
				+ " order by CPK asc"
				+ " ) where CPK > 0  limit 20";
		System.out.println(sql);
		return jdbcTemplate.queryForList(sql);
	}
	
	/**
	 * 根据条件查询位置CPK
	 * @param param
	 * @return
	 */
	public List GetAllCPKByPos(Map<String, String> param) {
		Object start_time=param.get("start_time");
		Object end_time=param.get("end_time");
		Object hours=param.get("hours");
		Object pro_code=param.get("pro_code");
		Object position=param.get("position");
		Object shopCode = param.get("shopCode").replace("-", "_");
		String sql=null;
		sql=" select  mac_code,CPK from (select	t1.t1_pro_code as pro_code, " +
				"			t1.all_pos position,t1.mac_code, " +
				"			max(t2.spc_avg_val) spc_avg_val, " + 
				"			count(1) ct, " + 
				"			if(ct > 2,sqrt(sum(pow(t1.all_val - t2.spc_avg_val,2)) / (count(1)-1)),1) spc_stddev, " + 
				"			max(t1.all_usl) as usl, " + 
				"			max(t1.all_lsl) as lsl, " + 
				"			(abs(usl - spc_avg_val)/(3.0 *spc_stddev )) spc_cpu, " + 
				"			(abs(spc_avg_val - lsl)/(3.0 * spc_stddev )) spc_cpl, " + 
				"			toDecimal32(if(ct > 2 ,if(spc_cpl > spc_cpu,spc_cpu,spc_cpl),0),3) CPK " + 
				"	from  " + 
				"	( " +  
				"		select  a.product_code t1_pro_code,b.position all_pos,a.mac_code ,(b.chk_value) all_val,(b.usl) all_usl,(b.lsl) all_lsl from quality.check_detect_" + shopCode + " a  " + 
				"		ARRAY join detect as b   " + 
				"		where t1_pro_code = '" + pro_code + "' and a.chk_result = 'OK' and all_pos = '" + position + "' and b.usl !=0 and b.lsl !=0  "
				+ (start_time!=null&&end_time!=null?(" and toDate(chk_time)>=toDate('"+start_time+"') and toDate(chk_time)<=toDate('"+end_time+"') ")
						:(" and chk_time>=subtractHours(now(),"+hours+") "))+
				"		 " + 
				"	) t1  " + 
				"	inner join  " + 
				"	( " +  
				"		select  a.product_code t2_pro_code,b.position spc_avg_pos,a.mac_code,avg(b.chk_value) spc_avg_val from quality.check_detect_" + shopCode + " a  " + 
				"		ARRAY join detect as b  " + 
				"		where t2_pro_code = '" + pro_code + "' and a.chk_result = 'OK' and spc_avg_pos == '" + position + "' " +
					(start_time!=null&&end_time!=null?(" and (chk_time)>=toDateTime('"+start_time+"') and (chk_time)<=toDateTime('"+end_time+"') ")
						:(" and chk_time>=subtractHours(now(),"+hours+") "))+
				"		group by b.position,a.product_code,a.mac_code  " + 
				"	) as t2  " + 
				"	on t1.all_pos = t2.spc_avg_pos and t1.t1_pro_code = t2.t2_pro_code and t1.mac_code = t2.mac_code   " + 
				"	group by t1.t1_pro_code,t1.all_pos,t1.mac_code order by CPK asc ) where CPK > 0 limit 20";
		return jdbcTemplate.queryForList(sql);
	}
		
		public List GetSPC(Map<String, String> param) {
			Object start_time=param.get("start_time");
			Object end_time=param.get("end_time");
			Object pro_code=param.get("pro_code");
			Object mac_code=param.get("mac_code");
			Object position=param.get("position");
			Object shopCode = param.get("shopCode").replace("-", "_");
			String sql=null;
			sql="		select  b.chk_value det_value,b.usl up_limit,b.lsl lv_limit,b.std_value std_value  from quality.check_detect_" + shopCode + " a  " +
					"		ARRAY join detect as b   " +
					"		where a.product_code = '" + pro_code + "' and a.chk_result = 'OK' and b.position == '" + position + "'  and a.mac_code = '" + mac_code + "'   " +
					(start_time!=null&&end_time!=null?(" and (chk_time)>=toDateTime('"+start_time+"') and (chk_time)<=toDateTime('"+end_time+"') ") :"");
			return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 各组检测结果数量分析
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public List teamCheckCount(Map<String, String> param){
		String start_time = param.get("start_time");
		String end_time = param.get("end_time");
		String shift_name = param.get("shift_name");
		Object shopCode = param.get("shopCode").replace("-", "_");
		//拼接sql
		String sql = "select product_code,shift_name,toString(sum(multiIf(chk_result='NG',1,0))) qr " +
					"from quality.check_detect_" + shopCode + "" +
					"where 1 = 1 ";
		if(!shift_name.isEmpty()){
			sql += "AND shift_name = '" + shift_name + "'";
		}
		if(!start_time.isEmpty() && !end_time.isEmpty()){
			sql += "AND chk_time>=toDateTime('" + start_time + "') AND chk_time <= toDateTime('" + end_time + "')";
		}
		sql += "group by product_code,shift_name";

		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 各组检测结果数量分析明细
	 * @param param
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public List teamCheckCountDetail(Map<String, String> param){
		String start_time = param.get("start_time");
		String end_time = param.get("end_time");
		String shift_name = param.get("shift_name");
		String product_code = param.get("product_code");
		Object shopCode = param.get("shopCode").replace("-", "_");

		//拼接sql
		String sql = "select toString(sum(multiIf(detect.chk_result='NG',1,0))) qr,detect.position " +
				"from quality.check_detect_" + shopCode + " " +
				"array join detect " +
				"where 1 = 1 " +
				"and  product_code = '" + product_code + "' and shift_name = '" + shift_name + "' " +
				"and chk_time>=toDateTime('" + start_time + "') and chk_time <= toDateTime('" + end_time + "') " +
				"group by detect.position";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询班组列表
	 * @author yzy
	 * @date 2020-05-29
	 * @return
	 */
	public List queryShiftName(Map<String, String> param){
		Object shopCode = param.get("shopCode").replace("-", "_");
		//拼接sql
		String sql = "select distinct shift_name from check_detect_" + shopCode + "  ";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询机台历史运行状态
	 * @author yzy
	 * @date 2020-05-30
	 * @return
	 */
	public List queryMac_status(Map<String, String> param){
		String date = param.get("date");
		String mac_code = param.get("mac_code");
		Object shopCode = param.get("shopCode").replace("-", "_");

		//拼接sql
		String sql = "select type_Data,machine_code,statusID_cur,statusID_pre,"
				+ "status_step,runmode_cur,toString(pub_time) pub_time,file_prog_main,num_prog_main "
				+ "from mac_status_" + shopCode + " "
				+ "where machine_code = '" + mac_code + "' and pub_date = toDate('" + date + "') " 
				+ "order by pub_time";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询案例分析最近50组数据尺寸位置
	 * @author yzy
	 * @date 2020-06-08
	 * @return
	 */
	public List<Map<String, Object>> queryCase_analysis(String product_code, String mac_code, String focus_type, String shopCode){
		//拼接sql
		String sql = "select DISTINCT detect.position,detect.std_value,detect.usl,detect.lsl " +
				"from check_detect_" + shopCode + " array join detect " +
				"where product_code = '" + product_code + "' and mac_code = '" + mac_code + "'" +
				" and  chk_type=1 ";
		if(focus_type.equals("Y")){
			sql += " and detect.focus_type = 1";
		}
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询案例分析最近50组数据明细
	 * @author yzy
	 * @date 2020-06-08
	 * @return
	 */
	public List queryCase_analysis_detail(String product_code, String mac_code, String position, String shopCode){
		//拼接sql
		String sql = "select toString(chk_time) chk_time,product_sn,chk_equ_code,detect.chk_value,detect.cmp_value " +
				"from check_detect_" + shopCode + " array join detect " +
				"where product_code = '" + product_code + "' and `detect.position`='" + position + "' and mac_code = '" + mac_code + "' " +
				" and  chk_type=1 " +
				"order by chk_time desc " +
				"limit 50";
		return jdbcTemplate.queryForList(sql);
	}

	/**
	 * 查询机床列表
	 * @author yzy
	 * @date 2020-06-16
	 * @return
	 */
	public List queryMacCode(Map<String, String> param){
		Object shopCode = param.get("shopCode").replace("-", "_");
		//拼接sql
		String sql = "select distinct machine_code from mac_status_" + shopCode + "";
		return jdbcTemplate.queryForList(sql);
	}

}