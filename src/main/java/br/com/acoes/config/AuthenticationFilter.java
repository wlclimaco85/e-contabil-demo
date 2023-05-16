package br.com.acoes.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import br.com.acoes.exception.FailedLoginException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Profile("!test")
public class AuthenticationFilter implements Filter {

	@Value("${account.validadeSession.url}")
	private String url;
	
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) req;
		String token = httpRequest.getHeader("Authorization");
		if(token == null) {
			token = "Basic c2Fua2h5YTpzdXA=";
		}
		if (skipFilter(httpRequest.getRequestURI()) || callAccount(token)) {
			chain.doFilter(req, res);
		} else {
			log.error("Falha na autenticação - [Método: doFilter]");
			throw new FailedLoginException("login.failed");
		}
	}

	private Boolean skipFilter(final String url) {

		List<String> urlToSkip = new ArrayList<String>();
		urlToSkip.add("swagger");
		urlToSkip.add("api-docs");
		urlToSkip.add("/csrf");
		return urlToSkip.stream().filter(u -> url.indexOf(u) >= 0 || url.length() < 2).findFirst().isPresent();

	}

	// pode ser alterado para obrigar receber um autentication token.
	public Boolean callAccount(String token) {

		if (token == null) {
			log.error("Falha na autenticação. Token nulo. [Método: callAccount]");
			throw new FailedLoginException("login.failed");
		}

		if (token.startsWith("Bearer ")) {
			token = token.substring(7);
		} else if (token.startsWith("Basic ")) {
			token = token.substring(6);
		}

		return "c2Fua2h5YTpzdXA=".equals(token);

	}
}