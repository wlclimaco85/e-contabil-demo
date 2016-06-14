package br.com.emmanuelneri.app.model;


import java.util.Map;

/**
 * The Class TokenModel.
 */
public class ModelToken
{

	/** The auth token. */
	private String token;

	/** The user name. */
	private  String name;



	/** The roles. */
	private  Map<String, Boolean> roles;




	public ModelToken() {
		super();
	}



	public ModelToken(String token, String name, Map<String, Boolean> roles) {
		super();
		this.token = token;
		this.name = name;
		this.roles = roles;
	}



	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Boolean> getRoles() {
		return roles;
	}

	public void setRoles(Map<String, Boolean> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "ModelToken [getToken()=" + getToken() + ", getName()=" + getName() + ", getRoles()=" + getRoles()
				+ ", toString()=" + super.toString() + "]";
	}

}
