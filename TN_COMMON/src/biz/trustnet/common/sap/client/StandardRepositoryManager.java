/* 
 * Project Name : TN_COMMON
 * Project      : ARSJCO
 * File Name    : biz.trustnet.common.sap.client.StandardRepositoryManager.java
 * Date	        : Dec 30, 2008
 * Version      : 2.0
 * Author       : ginaida@ginaida.net
 * Comment      :  
 */

package biz.trustnet.common.sap.client;

import java.util.TreeMap;

import com.sap.mw.jco.JCO;

public class StandardRepositoryManager {

	static private StandardRepositoryManager repositoryManager = null;
	static private TreeMap items = null;
	
	
	protected StandardRepositoryManager() {
		items = new TreeMap();
	}
	
	static public synchronized StandardRepositoryManager getSingleInstance(){
		if(repositoryManager == null){
			repositoryManager = new StandardRepositoryManager();
		}
		return repositoryManager;
	}
	
	public synchronized JCO.Repository createRepository(JCO.Pool pool)throws Exception {
		JCO.Client client = null;
		try{
			
			client = JCO.getClient(pool.getName());
			String name = client.getAttributes().getSystemID();
			JCO.releaseClient(client);
			client = null;
			if( items.containsKey(name))throw new Exception("A repository for system '"+name+"' already exists.");
			JCO.Repository repository = new JCO.Repository(name,pool.getName());
			items.put(name, repository);
			return repository;
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			if(client != null){
				JCO.releaseClient(client);
			}
		}
	}
	
	public boolean existRepository(String systemId){
		JCO.Repository repository = (JCO.Repository)items.get(systemId);
		return (repository !=null);
	}
	
	public boolean existsRepository(JCO.Pool pool)throws Exception{
		JCO.Client client = null;
		try{
			client = JCO.getClient(pool.getName());
			String name = client.getAttributes().getSystemID();
			JCO.releaseClient(client);
			client = null;
			return this.existRepository(name);
		}catch(Exception e){
			throw new Exception(e);
		}finally{
			if(client !=null){
				JCO.releaseClient(client);
			}
		}
	}
	
	public synchronized JCO.Repository getRepository(JCO.Pool pool)throws Exception {
		return this.getRepository(pool,false);
	}
	
	public synchronized JCO.Repository getRepository(JCO.Pool pool,boolean createIfItDoesNotExist)throws Exception{
		JCO.Client client = null;
		try{
			client = JCO.getClient(pool.getName());
			String name = client.getAttributes().getSystemID();
			JCO.releaseClient(client);
			client = null;
			try{
			return this.getRepository(name);
			}catch(Exception ax){
				if(createIfItDoesNotExist){
					return this.createRepository(pool);
				}else{
					throw ax;
				}
			}
		}catch(Exception e){
			if(createIfItDoesNotExist){
				return this.createRepository(pool);
			}else{
				throw e;
			}
		}finally{
			if(client !=null){
				JCO.releaseClient(client);
			}
		}
	}
	
	public synchronized JCO.Repository getRepository(String systemId) throws Exception{
		JCO.Repository repository = (JCO.Repository)items.get(systemId);
		if(repository == null){
			throw new Exception("No repository exists for system '"+systemId+"'.");
		}
		return repository;
	}
	
	public synchronized void removeRepository(JCO.Repository repository){
		String name = repository.getName();
		if(items.containsKey(name)){
			items.remove(name);
		}
	}
	
	
}
