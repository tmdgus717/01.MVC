package com.model2.mvc.view.product;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUpload;

import com.model2.mvc.framework.Action;

public class UpdateProductAction extends Action{
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int prodNo=0;
		if(FileUpload.isMultipartContent(request)) {
			
		}
		return "redirect://getProduct.do?prodNo="+prodNo+"&menu=ok";
	}//execute
}//class
