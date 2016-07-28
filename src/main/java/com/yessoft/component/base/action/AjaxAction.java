package com.yessoft.component.base.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * ajax访问Action基类
 */
public class AjaxAction {

	protected ObjectMapper mapper = new ObjectMapper();

	public void returnAjaxString(String text,HttpServletResponse response){
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/html;charset=UTF-8"); 
			writer = response.getWriter();
			writer.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer!=null){
				writer.close();
			}
		}
	}
	
	
	public void returnAjaxJson(Object bean,HttpServletResponse response){
		PrintWriter writer = null;
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json;charset=UTF-8");
			writer =  response.getWriter();
			writer.println(mapper.writeValueAsString(bean));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(writer != null){
				writer.close();
			}
		}
	}
	
	public void returnAjaxJson(Object bean,HttpServletResponse response,String dateFormat){
		PrintWriter writer = null;
		mapper.setDateFormat(new SimpleDateFormat(dateFormat));
		try {
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json;charset=UTF-8");
			writer =  response.getWriter();
			writer.println(mapper.writeValueAsString(bean));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
