package coding.challenge.shop.apotheke;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Repository {

	private String id;
	private String name;

	@JsonCreator
	public Repository(String id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	

}