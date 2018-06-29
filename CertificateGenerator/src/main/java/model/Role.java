package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	private String name;
	@ManyToMany(mappedBy = "roles")
	private List<User> users = new ArrayList<>();
	
	public Role() {	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (!(this instanceof Role)) return false;
		return id != null && id.equals(((Role)obj).id);
	}
	
	@Override
	public int hashCode() {
		return 45;
	}
	
}
