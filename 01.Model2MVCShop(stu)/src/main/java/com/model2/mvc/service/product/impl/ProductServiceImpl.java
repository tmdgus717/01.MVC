package com.model2.mvc.service.product.impl;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.product.dao.ProductDAO;
import com.model2.mvc.service.product.vo.ProductVO;

public class ProductServiceImpl implements ProductService {

	private ProductDAO productDAO;
	
	public ProductServiceImpl() {
		productDAO = new ProductDAO(); //다오 인스턴스 생성
	}
	@Override
	public ProductVO addProduct(ProductVO productVO) throws Exception {
		productDAO.insertProduct(productVO);
		return productVO;
	}

	@Override
	public ProductVO getProduct(int prod_no) throws Exception {
		// TODO Auto-generated method stub
		return productDAO.findProduct(prod_no);
	}

	@Override
	public HashMap<String, Object> getProductList(SearchVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return productDAO.getProductList(searchVO);
	}

	@Override
	public ProductVO updateProduct(ProductVO prodVO) throws Exception {
		// TODO Auto-generated method stub
		productDAO.updateProduct(prodVO);;
		return prodVO;
	}

}
