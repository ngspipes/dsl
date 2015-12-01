package dsl.managers;

import repository.GithubRepository;
import repository.IRepository;
import repository.LocalRepository;
import repository.RemoteRepository;
import exceptions.RepositoryException;


public class RepositoryManager {
	
    public static IRepository createRemote(String location) throws RepositoryException {
    	try{
    		return new RemoteRepository(location);	
    	} catch(Exception e){
    		throw new RepositoryException("Error instanciating remote repository!", e);
    	}
    }
    
    public static IRepository createLocal(String location) throws RepositoryException {
    	try{
    		return new LocalRepository(location);	
    	} catch(Exception e){
    		throw new RepositoryException("Error instanciating local repository!", e);
    	}
    }
    
    public static IRepository createGithub(String location) throws RepositoryException {
    	try{
    		return new GithubRepository(location);	
    	} catch(Exception e){
    		throw new RepositoryException("Error instanciating local repository!", e);
    	}
    }
   
}

