package com.hoyrak.dao;

import com.hoyrak.entity.Product;
import com.hoyrak.model.PaginationResult;
import com.hoyrak.model.ProductInfo;

public interface ProductDAO 
{
	public ProductInfo findProductInfo(String code);
	public Product findProduct(String code); 
	public PaginationResult<ProductInfo>queryProducts( int Page,int maxResult,//
			int maxNavigationPage,String likeName);
	public void save(ProductInfo productInfo);
	
}
