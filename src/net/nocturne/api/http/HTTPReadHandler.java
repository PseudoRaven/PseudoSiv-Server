package net.nocturne.api.http;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class HTTPReadHandler implements HttpHandler {

	/**
	 * @author: Pax M
	 * @author: Lukafurlan
	 */

	private final Method method;
	private String response;

	public HTTPReadHandler(Method method, String response) {
		this.method = method;
		this.response = response;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		if (t.getRemoteAddress().getAddress().getHostAddress().toString()
				.equals(HTTPService.IPV4_ADDRESS)) {
			try {
				Object value = method
						.invoke(null, t.getRequestURI().getQuery());
				this.response = (String) value;
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
			t.sendResponseHeaders(200, response.length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(response.getBytes());
			}
		}
	}
}