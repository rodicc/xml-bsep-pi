package ftn.xmlwebservisi.firme.model;

import java.util.HashSet;
import java.util.Set;

public class Role {
	
	private Integer id;
	private String name;
	
	private Set<User> users = new HashSet<>();
}
