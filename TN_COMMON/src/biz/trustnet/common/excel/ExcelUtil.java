/*
 * Prodject Name	:   TN_COMMON
 * File Name		:	ExcelUtil.java
 * Date				:	���� 6:53:29
 * History			:	2007. 02. 22
 * Version			:	1.0
 * Author			:	(���ּ�)ginaida@ginaida.net
 * Comment      	:	JExcel ; �̿��� Excel �а� ���� Utility  				 
 */


package biz.trustnet.common.excel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import jxl.Cell;
import jxl.CellType;
import jxl.DateCell;
import jxl.DateFormulaCell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import biz.trustnet.common.log.Log;
import biz.trustnet.common.util.BeanUtil;
import biz.trustnet.common.util.CommonUtil;

public class ExcelUtil {

	private String fileName 	= "";
	private String title    	= "";
	private String sheetName 	= "";
	private int fontSize 			= 0;
	public ExcelUtil(){
		
	}
	
	public void setExcelProperty(String fileName,String title,String sheetName){
		this.fileName = fileName;
		this.title = title;
		this.sheetName = sheetName;
	}
	
	public void titleStyle(int fontSize){
		this.fontSize  = fontSize;
	}
	
	public boolean writeExcel(String[] column,Object[] object)throws Exception{
		boolean isSuccess = false;
		try{
			//create WriteableWorkbook 
			WritableWorkbook workbook = Workbook.createWorkbook(new File(fileName)); 
			//create WritableSheet 
			WritableSheet sheet = workbook.createSheet(sheetName, 0);
			
			int columnSize = 0;
			
			for(int row=0 ; row < object.length ; row++){
				ArrayList list = BeanUtil.getBeanValue(object[row]);
				columnSize =  list.size();
				
				WritableCellFormat cellFormat = getCellFormat(WritableFont.ARIAL,10);
				cellFormat.setAlignment(Alignment.LEFT);
				cellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.ICE_BLUE );
				cellFormat.setBackground(Colour.WHITE);
				for(int i=0 ; i < columnSize ; i++){
					sheet.addCell(new Label(i,row,(String)list.get(i),cellFormat));
				}
			
			}
			
			if(column != null && column.length > 0){
				sheet.insertRow(0);
				WritableCellFormat cellFormat = getCellFormat(WritableFont.ARIAL,10);
				cellFormat.setAlignment(Alignment.LEFT);
				cellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK );
				cellFormat.setBackground(Colour.WHITE);
				for(int i=0 ; i < column.length ; i++){
					sheet.addCell(new Label(i,0,column[i],cellFormat));
				}
			}
			
			if(!title.equals("")){
				sheet.insertRow(0);
				WritableCellFormat cellFormat = getCellFormat(WritableFont.ARIAL,fontSize);
				cellFormat.setAlignment(Alignment.CENTRE);
				cellFormat.setBorder(Border.ALL,BorderLineStyle.THIN,Colour.BLACK);
				cellFormat.setBackground(Colour.WHITE);
				sheet.addCell(new Label(0,0,title,cellFormat));
				sheet.mergeCells(0, 0, columnSize -1, 0);
			}
			
			workbook.write(); 
			workbook.close(); 
			
			isSuccess =  true;
		}catch(Exception e){
			isSuccess = false;
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
			throw new Exception(e);
		}
		
		return isSuccess;
	}
	
	
	public WritableCellFormat getCellFormat(WritableFont.FontName  font,int size){
		if(size < 10){	size = 10; }
		return new WritableCellFormat (new WritableFont(font, size));
	}
	
	public ArrayList readExcel(String fileName){
		return readExcel(new File(fileName));
	}
	
	public ArrayList readExcel(String fileName,int sheet){
		return readExcel(new File(fileName),sheet);
	}
	
	public ArrayList readExcelSheet(String fileName){
		return readExcelSheet(new File(fileName));
	}
	
	public ArrayList readExcelSheet(File file){
		ArrayList list = new ArrayList();
		try{
			Workbook workbook = Workbook.getWorkbook(file);
			String[] sheetName = workbook.getSheetNames();
			for(int i=0 ; i < sheetName.length ; i++){
				list.add(sheetName[i]);
			}
			
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		
		return list;
	}
	
	
	public ArrayList readExcel(File file){
		ArrayList list = new ArrayList();
		try{
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(0);
			
			int totalRow 	= sheet.getRows();
			int totalColumn = sheet.getColumns();
			
			for(int r=0;r<totalRow;r++){
				String[] column = new String[totalColumn];
				for(int c=0;c<totalColumn;c++){
					Cell cell = sheet.getCell(c,r);
					
					if(cell == null){
						column[c] = "";
					}else{
						if(cell.getType() == CellType.DATE ){
							SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							column[c] =simpledateformat.format(((DateCell)cell).getDate());
						}else if(cell.getType() == CellType.DATE_FORMULA ){
							column[c] = ((DateFormulaCell)cell).getDate().toString(); 
						}else{
							column[c] = cell.getContents();
						}
					}
				}
				list.add(column);
			}
			
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		
		return list;
	}
	
	public ArrayList readExcel(File file,int sheetNumber){
		ArrayList list = new ArrayList();
		try{
			Workbook workbook = Workbook.getWorkbook(file);
			Sheet sheet = workbook.getSheet(sheetNumber);
			
			int totalRow 	= sheet.getRows();
			int totalColumn = sheet.getColumns();
			
			for(int r=0;r<totalRow;r++){
				String[] column = new String[totalColumn];
				for(int c=0;c<totalColumn;c++){
					Cell cell = sheet.getCell(c,r);
					
					if(cell == null){
						column[c] = "";
					}else{
						if(cell.getType() == CellType.DATE ){
							SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							column[c] =simpledateformat.format(((DateCell)cell).getDate());
						}else if(cell.getType() == CellType.DATE_FORMULA ){
							column[c] = ((DateFormulaCell)cell).getDate().toString(); 
						}else{
							column[c] = cell.getContents();
						}
					}
				}
				list.add(column);
			}
			
		}catch(Exception e){
			Log.debug("log.root",CommonUtil.getExceptionMessage(e),this);
		}
		
		return list;
	}
	
	
	
	

}
