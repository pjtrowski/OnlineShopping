package com.hoyrak.util;

import javax.servlet.http.HttpServletRequest;

import com.hoyrak.model.CartInfo;
public class Utils 
{
	public static CartInfo getCartInSession(HttpServletRequest req) 
	{
		CartInfo cartInfo=(CartInfo)req.getSession().getAttribute("myCart");
		if(cartInfo==null) 
		{
			cartInfo=new CartInfo();
			req.getSession().setAttribute("myCart", cartInfo);
		}
		return cartInfo;
	}
	
	public static void removeCartInfoSession(HttpServletRequest req) 
	{
		req.getSession().removeValue("myCart");
	}

	public static void storeLastOrderInCartSession(HttpServletRequest req,CartInfo cartinfo) 
	{
		req.getSession().setAttribute("lastOrderedCart", cartinfo);
	}

	public static CartInfo getLastOrderedCartInfoSession(HttpServletRequest req) 
	{
		return (CartInfo) req.getSession().getAttribute("lastOrderedCart");
	}
}
