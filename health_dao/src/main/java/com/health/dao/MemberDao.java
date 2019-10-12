package com.health.dao;

import com.health.pojo.Member;
import org.apache.ibatis.annotations.Param;

public interface MemberDao {
    /**
     * 根据身份证号码和手机号码查询当前会员
     * @param idCard
     * @param telephone
     * @return
     */
    Member FindMemberByTelephoneAndIdCard(@Param("idCard") String idCard, @Param("telephone") String telephone);

    /*
    注册会员，并获取自增长的id存到当前对象中
     */
    void AddMember(Member member);

    /**
     * 本日新增会员
     * @param reportDate
     * @return
     */
    Integer getTodayMemberCount(String reportDate);

    /**
     * 总会员数
     * @return
     */
    Integer getTotalMemberCount();

    /**
     * 本周新增会员/本月新增会员
     * @param start
     * @param end
     * @return
     */
    Integer getThisWeekOrMonthMemberCount(@Param("start") String start, @Param("end") String end);
}
