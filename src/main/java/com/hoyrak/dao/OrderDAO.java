package com.hoyrak.dao;

import java.util.List;

import com.hoyrak.model.CartInfo;
import com.hoyrak.model.OrderInfo;
import com.hoyrak.model.PaginationResult;
import com.hoyrak.model.OrderDetailInfo;

public interface OrderDAO 
{
	public void saveOrder(CartInfo cartinfo) ;
	public PaginationResult<OrderInfo> listOfOrdersInfo(int page,int maxResult, //
				int maxNavigationPage);
	public OrderInfo getOrderInfo(String orderID);
	public List<OrderDetailInfo> listOfOrderDetailsInfo(String orderID);
}
