package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;

import com.health.Constant.MessageConstant;
import com.health.Exception.OrderException;
import com.health.dao.MemberDao;
import com.health.dao.OrderDao;
import com.health.dao.OrderSettingDao;
import com.health.interfaces.OrderService;
import com.health.pojo.Member;
import com.health.pojo.Order;
import com.health.pojo.OrderSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    /**
     * 提交订单
     *
     * @param map
     * @return
     */
    @Transactional
    @Override
    public Order submitOrder(Map<String, String> map) throws OrderException {
        /*
        1、先根据日期查询预约设置表（t_ordersetting）表，判断当天是否可以预约
		    -- 如果不可以预约，则终止程序，抛出异常，提示当天不可预约。
				查询当前的套餐项是否已经设置预约，和number是否大于reservations
		    -- 如果可以预约，则判断是否是会员
		2、根据号码查询是否是会员
			-- 如果不是会员，则自动注册，返回会员id
			-- 如果是，也是返回id
		3、根据会员id、套餐id、预约日期查询是否已经预约
			-- 如果已经预约了，则终止程序，提示已经预约了
			-- 如果没有预约，则插入数据，预约，预约成功后返回order对象
         */
        // 1、先根据日期查询预约设置表（t_ordersetting）表，判断当天是否可以预约
        String orderDate = map.get("orderDate");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = dateFormat.parse(orderDate);
        } catch (ParseException e) {
            e.printStackTrace();
            //格式转换异常
            throw new OrderException("日期格式转换异常");
        }
        //调用dao层根据日期查询预约设置
        OrderSetting orderSetting = orderSettingDao.CheckOrderSettingByOrderDate(date);
        if (orderSetting == null) {
            //当天未设置预约,不可以预约
            throw new OrderException(MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        //判断当天是否已经约满
        if (orderSetting.getNumber() <= orderSetting.getReservations()) {
            //当天已经预约满了
            throw new OrderException(MessageConstant.ORDER_FULL);
        }
        //判断是否是会员
        //调用MemberDao根据手机号码和身份证号码查询是否是会员
        Member member = memberDao.FindMemberByTelephoneAndIdCard(map.get("idCard"), map.get("telephone"));
        if (member == null) {
            //不是会员,则自动注册
            member = new Member();
            //如果不是会员,则自动自动注册会员
            member.setName(map.get("name"));
            member.setIdCard(map.get("idCard"));
            member.setPhoneNumber(map.get("telephone"));
            member.setSex(map.get("sex"));
            member.setRegTime(new Date());
            //注册用户,并把自增长的id赋值到对象中
            memberDao.AddMember(member);
        }
        //获取会员的id
        Integer memberId = member.getId();
        String id = map.get("packageId");
        //根据会员id、套餐id、预约日期查询是否已经预约
        Integer PackageId = Integer.valueOf(id);
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("date", date);
        orderMap.put("memberId", memberId);
        orderMap.put("PackageId", PackageId);
        Order order = orderDao.FindOrderByOrderDateAndMemberIdAndPackageId(orderMap);
        if (order != null) {
            //说明已经预约了,不能重复预约
            throw new OrderException(MessageConstant.HAS_ORDERED);
        }
        //到这里，说明可以预约
        order=new Order();
        order.setMemberId(memberId);
        order.setOrderDate(date);
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setPackageId(PackageId);
        order.setOrderType(map.get("orderType"));
        //往下单表添加一条数据
        orderDao.AddOrder(order);
        //更新预约设置表的预约人数
        orderSettingDao.UpdateOrderSettingByOrderDate(date);
        return order;
    }

    /**
     * 根据id查询订单信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> FindById(int id) {
        return orderDao.FindById(id);
    }



}
