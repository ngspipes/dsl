package dsl.managers;

import repository.GithubRepository;
import repository.IRepository;
import repository.LocalRepository;
import repository.UriBasedRepository;
import exceptions.RepositoryException;


public class RepositoryManager {
	
    public static IRepository createUriBased(String location) throws RepositoryException {
    	try{
    		return new UriBasedRepository(location);	
    	} catch(Exception e){
    		throw new RepositoryException("Error instanciating uri based repository!", e);
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

