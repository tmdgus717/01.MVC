package com.model2.mvc.service.purchase.impl;

import java.util.HashMap;

import com.model2.mvc.common.SearchVO;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.dao.PurchaseDAO;
import com.model2.mvc.service.purchase.vo.PurchaseVO;
import com.model2.mvc.service.user.dao.UserDAO;

public class PurchaseServiceImpl implements PurchaseService {
	private PurchaseDAO purchaseDAO;
	
	public PurchaseServiceImpl() {
		purchaseDAO=new PurchaseDAO();
	}
	@Override
	public PurchaseVO addPurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.insertPurchase(purchaseVO);
		return purchaseVO;
	}//¿Ï·á

	@Override
	public PurchaseVO getPuchase(int pur_no) throws Exception {
		// TODO Auto-generated method stub
		return purchaseDAO.findPurchase(pur_no);
		//return purchaseVO;
	}

	@Override
	public HashMap<String, Object> getPurchaseList(SearchVO searchVO, String userId) throws Exception {
		return purchaseDAO.getPurchaseList(searchVO,userId);
	}

	@Override
	public HashMap<String, Object> getSaleList(SearchVO searchVO) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PurchaseVO updatePurchase(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		
		purchaseDAO.updatePurchase(purchaseVO);
		return purchaseVO;
	}

	@Override
	public void updateTranCode(PurchaseVO purchaseVO) throws Exception {
		// TODO Auto-generated method stub
		purchaseDAO.updateTranCode(purchaseVO);
	}
	public void updateTranCodeByProd(int prodNo, PurchaseVO purchaseVO) throws Exception {
		purchaseDAO.updateTranCodeByProd(prodNo,purchaseVO);
		
	}
}
