package com.pruebas.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.security.web.authentication.
	 *      SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.
	 *      http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
	 *      org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		// Generar un mensaje cuando se vaya a logear correctamente
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		FlashMap flashMap = new FlashMap();
		flashMap.put("success", String.format("Hola %s, ha iniciado sesión con éxito!", authentication.getName()));

		flashMapManager.saveOutputFlashMap(flashMap, request, response);

		if (authentication != null) {
			logger.info(String.format("El usuario '%s' ha iniciado la sesión con éxito", authentication.getName()));
		}

		super.onAuthenticationSuccess(request, response, authentication);
	}

}
