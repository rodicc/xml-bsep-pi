package security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

// Custom implementation for Authentication objects
public class CustomAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = -632841933436741867L;
	private String token;
	private UserDetails principal;
	
	public CustomAuthenticationToken(UserDetails principal, String token) {
		super((principal != null) ? principal.getAuthorities() : null);
		this.principal = principal;
		this.token = token;
	}
	
	@Override
	public String getCredentials() {
		return token;
	}

	@Override
	public UserDetails getPrincipal() {
		return principal;
	}
	
	@Override
	public boolean isAuthenticated() {
		return true;
	}
}
