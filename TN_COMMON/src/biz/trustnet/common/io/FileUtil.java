/* 
 * Project Name	:   TN_COMMON
 * File Name		:	FileUtil.java
 * Date				:	ø¿»ƒ 8:31:40
 * History			:	&{date}
 * Version			:	1.0
 * Author			:   ¿”¡÷º∑	
 * Comment      	:    
 */
package biz.trustnet.common.io;


import java.io.File;
public class FileUtil {


	public FileUtil(){
	}
	
	
	public File getFile(String path,String name){
		try{
			if(existFile(path,name)){
				File file = new File(path,name);
				return file;
			}else{
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	
	public boolean existDirectory(String path){
		try{
			File file = new File(path);
			return file.exists();
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean existFile(String path,String name){
		try{
			File file = new File(path,name);
			return file.exists();
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean existFile(String fileName){
		try{
			File file = new File(fileName);
			return file.exists();
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean createDirectory(String path){
		try{
			File file = new File(path);
			return file.mkdir();
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteFile(String path,String name){
		try{
			File file = new File(path+File.separator+name);
			if(existFile(path,name)){
				file.delete();
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}
	
	public boolean deleteFile(String fileName){
		try{
			File file = new File(fileName);
			if(existFile(fileName)){
				file.delete();
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

	public boolean deleteDirectory(String path){
		try{
			File file = new File(path);
			if(existDirectory(path)){
				file.delete();
				return true;
			}
			return false;
		}catch(Exception e){
			return false;
		}
	}

	public long getFileSizeLong(String path,String name){
		
		if(name.equals("") || name == null || name.equalsIgnoreCase("null")) 
			return 0;
		else{
			try{
				File file = new File(path+File.separator+name);
				if(existFile(path,name))
					return file.length();
				else
					return 0;
			}catch(Exception e){
				return 0;
			}
		}
	}
	 
	public int getFileSize(String path,String name){
		return new Long(getFileSizeLong(path,name)).intValue();	
	}
	
	public String getFileName(File file){
		try{
			if(file == null)
				return "";
			else{
				if(file.exists()){
					return file.getName();
				}else{
					return "";
				}	
			}
		}catch(Exception e){
			return "";	
		}
				
	}
	
	public File[] getFileList(String path){
		if(path == null || path.equalsIgnoreCase("null"))
			return null;	
		else{
			File file = new File(path);
			if(file.exists()){
				return file.listFiles();
			}else{
				return null;
			}
		}
	}
	
	public String[] getFileNameList(String path){
		if(existDirectory(path)){
			File[] file = getFileList(path);
			String[] fileList = new String[file.length];
			
			for(int i = 0 ; i < file.length ; i++){
				fileList[i] = file[i].getName();
			}
			return fileList;
		}else{
			return new String[0];
		}
	}

	public void makeDirectory (String path){
		FileUtil file =  new FileUtil();
		if(!file.existDirectory(path)){
			file.createDirectory(path);
		}
	}
	
	public boolean renameTo(String fileName,String destFileName){
		boolean isSuccess = false;
		try{
			if(existFile(fileName)){
				File srcFile = new File(fileName);
				isSuccess = srcFile.renameTo(new File(destFileName));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return isSuccess;
	}

}
