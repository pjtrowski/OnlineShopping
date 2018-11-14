package com.hoyrak.daoImpl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.hoyrak.dao.ProductDAO;
import com.hoyrak.entity.Product;
import com.hoyrak.model.PaginationResult;
import com.hoyrak.model.ProductInfo;
@Transactional
public class ProductDAOImpl implements ProductDAO {

	@Autowired
	SessionFactory sessionfactory;
	
	@Override
	public ProductInfo findProductInfo(String code) {
		Product product=this.findProduct(code);
		if(product==null) 
		{
			return null;
		}
		return new ProductInfo(product.getCode(),product.getName(),product.getPrice());
	}

	@Override
	public Product findProduct(String code) {
		Session session=sessionfactory.getCurrentSession();
		Criteria crit=session.createCriteria(Product.class);
		crit.add(Restrictions.eq("code",code));
		return (Product)crit.uniqueResult();
	}

	@Override
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
			String likeName) 
	{
		String sql="Select new " + ProductInfo.class.getName() //
                + " (p.code, p.name, p.price) " + " FROM "//
                + Product.class.getName() + " p ";
		System.out.println("select product sql: "+sql);
		if(likeName!=null&&likeName.length()>0) 
		{
			sql +="WHERE lower(p.name) like:likeName";
		}
		sql+="ORDER BY p.createDate DESC";
		
		Session session=sessionfactory.getCurrentSession();
		Query query=session.createQuery(sql);
		if(likeName!=null&&likeName.length()>0) 
		{
			query.setParameter("likeName", "%"+likeName.toLowerCase()+"%");
		}
		return new PaginationResult<ProductInfo>(query,page,maxResult,maxNavigationPage);
	}

	@Override
	public void save(ProductInfo productInfo) 
	{
		String code=productInfo.getCode();
		Product product;
		boolean isNew=false;
		if(code !=null) 
		{
			product=this.findProduct(code);
			if(product==null) 
			{
				isNew=true;
				product=new Product();
				product.setCreateDate(new Date());
			}
		product.setCode(code);
		product.setName(productInfo.getName());
		product.setPrice(productInfo.getPrice());
		
		
		if(productInfo.getFileData()!=null) 
		{
			byte[]image=productInfo.getFileData().getBytes();
			if(image==null&&image.length>0) 
			{
				product.setImage(image);
			}
		}
	   }
	}
}
