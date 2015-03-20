package com.cho.chove.support.poi.helper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.cho.chove.core.annotation.excel.ExcelColumn;

/**
 * Excel 파일 생성
 * @author 조호영
 * @since 2014.01.21
 * @version 0.1
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *  수정일                   수정자         수정내용
 *  ----------  ----    ---------------------------
 *  2014.01.21  조호영          최초 작성
 *
 * </pre>
 */

public class PoiViewer extends AbstractExcelView {
	
	@Override
	protected void buildExcelDocument(Map<String, Object> ModelMap,
			HSSFWorkbook workbook, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// TODO Auto-generated method stub
		List<Object> list = (List<Object>) ModelMap.get("list");
        HSSFSheet sheet = null;
        
        if(list != null && list.size() > 0){
        	Object titleObj = list.get(1);
        	sheet = workbook.createSheet(titleObj.getClass().getName()+ " WorkSheet");
           
        	Method[] tm = titleObj.getClass().getDeclaredMethods();
    		HSSFRow titleRow = sheet.createRow(0);
    		
    		setHeader(titleObj, titleRow, 0);
    		
    		for(int i=0; i < list.size(); i++) {
            	int bodyIdx = 0;
            	Object bodyObj = list.get(i);
            	HSSFRow bodyRow = sheet.createRow(i+1);
            	setBody(bodyObj, bodyRow, 0);
            }	
        }
	}
	
	private int setHeader(Object titleObj, HSSFRow titleRow, int pTitleIdx) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException, SecurityException {
		
		Method[] tm = titleObj.getClass().getDeclaredMethods();
		int titleIdx = pTitleIdx;
		for (int i = 0; i < tm.length; i++) {
        	if(tm[i].getName().substring(0,3).equals("get")) {
        		if(tm[i].invoke(titleObj, null) == null) {
        			titleRow.createCell(titleIdx++).setCellValue(((ExcelColumn) titleObj.getClass().getDeclaredField(capitalize(tm[i].getName().substring(3))).getAnnotation(ExcelColumn.class)).value());
        			continue;
        		}
        		
        		if (tm[i].invoke(titleObj, null) instanceof java.lang.Integer) {
        			titleRow.createCell(titleIdx++).setCellValue(((ExcelColumn) titleObj.getClass().getDeclaredField(capitalize(tm[i].getName().substring(3))).getAnnotation(ExcelColumn.class)).value());
        		} else if (tm[i].invoke(titleObj, null) instanceof java.lang.String) {
        			titleRow.createCell(titleIdx++).setCellValue(((ExcelColumn) titleObj.getClass().getDeclaredField(capitalize(tm[i].getName().substring(3))).getAnnotation(ExcelColumn.class)).value());
        		} else {
        			//System.out.println(tm[i].invoke(titleObj, null).getClass().getName());
        			titleIdx = setHeader(tm[i].invoke(titleObj, null), titleRow, titleIdx);
        		}
        	}
        }
		return titleIdx;
	}
	
	private int setBody(Object bodyObj, HSSFRow bodyRow, int pTitleIdx) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		int bodyIdx = pTitleIdx;
		Method[] bm = bodyObj.getClass().getDeclaredMethods();
    	for (int j = 0; j < bm.length; j++) {
        	if(bm[j].getName().substring(0,3).equals("get")){
        		Object c = bm[j].invoke(bodyObj, null);
        		if(bm[j].invoke(bodyObj, null) == null) {
        			bodyRow.createCell(bodyIdx++).setCellValue("");
        			continue;
        		}
        		if (c instanceof java.lang.Integer) {
        			bodyRow.createCell(bodyIdx++).setCellValue((Integer)bm[j].invoke(bodyObj, null));
        		} else if (c instanceof java.lang.String) {
        			bodyRow.createCell(bodyIdx++).setCellValue((String) bm[j].invoke(bodyObj, null));
        		} else {
        			bodyIdx = setBody(bm[j].invoke(bodyObj, null), bodyRow, bodyIdx);
        		}
        	}
        }
		return bodyIdx;
	}
	
	private String capitalize(String str) {
	    int strLen;
	    if (str == null || (strLen = str.length()) == 0) {
	        return str;
	    }
	    return new StringBuilder(strLen)
	        .append(Character.toLowerCase(str.charAt(0)))
	        .append(str.substring(1))
	        .toString();
	}
}
