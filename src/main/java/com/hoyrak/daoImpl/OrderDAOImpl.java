package com.hoyrak.daoImpl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

import com.hoyrak.dao.OrderDAO;
import com.hoyrak.dao.ProductDAO;
import com.hoyrak.entity.Order;
import com.hoyrak.entity.OrderDetail;
import com.hoyrak.entity.Product;
import com.hoyrak.model.CartInfo;
import com.hoyrak.model.CartLineInfo;
import com.hoyrak.model.CustomerInfo;
import com.hoyrak.model.OrderDetailInfo;
import com.hoyrak.model.OrderInfo;
import com.hoyrak.model.PaginationResult;

public class OrderDAOImpl implements OrderDAO {

	@Autowired
	private SessionFactory sessionfactory;
	
	@Autowired
	private ProductDAO productDAO;
	
	private int getMaxOrderNum() 
	{
		String sql = "SELECT max(o.orderNum) from " + Order.class.getName() + " o ";
        Session session = sessionfactory.getCurrentSession();
        Query query = session.createQuery(sql);
        Integer value = (Integer) query.uniqueResult();
        if (value == null) {
            return 0;
        }
        return value;
    }
	
	@Override
	public void saveOrder(CartInfo cartinfo) {
		 Session session = sessionfactory.getCurrentSession();
		 
	        int orderNum = this.getMaxOrderNum() + 1;
	        Order order = new Order();
	 
	        order.setId(UUID.randomUUID().toString());
	        order.setOrderNum(orderNum);
	        order.setOrderDate(new Date());
	        order.setAmount(cartinfo.getAmountTotal());
	 
	        CustomerInfo customerInfo = cartinfo.getCustomerInfo();
	        order.setCustomerName(customerInfo.getName());
	        order.setCustomerEmail(customerInfo.getEmail());
	        order.setCustomerPhone(customerInfo.getPhone());
	        order.setCustomerAddress(customerInfo.getAddress());
	 
	        session.persist(order);
	 
	        List<CartLineInfo> lines = cartinfo.getCartLines();
	 
	        for (CartLineInfo line : lines) {
	            OrderDetail detail = new OrderDetail();
	            detail.setId(UUID.randomUUID().toString());
	            detail.setOrder(order);
	            detail.setAmount(line.getAmount());
	            detail.setPrice(line.getProductInfo().getPrice());
	            detail.setQuanity(line.getQuantity());
	 
	            String code = line.getProductInfo().getCode();
	            Product product = this.productDAO.findProduct(code);
	            detail.setProduct(product);
	 
	            session.persist(detail);
	        }
	        // Set OrderNum for report.
	        cartinfo.setOrderNum(orderNum);
	    }
	 

	@Override
	public PaginationResult<OrderInfo> listOfOrdersInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName()//
                + "(ord.id, ord.orderDate, ord.orderNum, ord.amount, "
                + " ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone) " + " from "
                + Order.class.getName() + " ord "//
                + " order by ord.orderNum desc";
        Session session = this.sessionfactory.getCurrentSession();
 
        Query query = session.createQuery(sql);
 
        return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}
	public Order findOrder(String orderId) {
        Session session = sessionfactory.getCurrentSession();
        Criteria crit = session.createCriteria(Order.class);
        crit.add(Restrictions.eq("id", orderId));
        return (Order) crit.uniqueResult();
    }

	@Override
	public OrderInfo getOrderInfo(String orderID) {
	    Order order = this.findOrder(orderID);
        if (order == null) {
            return null;
        }
        return new OrderInfo(order.getId(), order.getOrderDate(), //
                order.getOrderNum(), order.getAmount(), order.getCustomerName(), //
                order.getCustomerAddress(), order.getCustomerEmail(), order.getCustomerPhone());
	}

	@Override
	public List<OrderDetailInfo> listOfOrderDetailsInfo(String orderID) {
		 String sql = "Select new " + OrderDetailInfo.class.getName() //
	                + "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount) "//
	                + " from " + OrderDetail.class.getName() + " d "//
	                + " where d.order.id = :orderId ";
	 
	        Session session = this.sessionfactory.getCurrentSession();
	 
	        Query query = session.createQuery(sql);
	        query.setParameter("orderId", orderID);
	 
	        return query.list();
	}

}
