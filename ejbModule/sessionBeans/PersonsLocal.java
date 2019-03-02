package sessionBeans;

import java.util.List;

import javax.ejb.Local;

import entity.Person;

@Local
public interface PersonsLocal {

	public void add(Person p) throws Exception;
	
	public void remove(Person p) throws Exception;
	
	public List<Person> getList() throws Exception;
	
	public Person getPersonById(int id) throws Exception;
}
