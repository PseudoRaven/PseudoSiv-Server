package net.nocturne.api.http;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

class HTTPMethodHandler implements HttpHandler {

	/**
	 * @author: Pax M
	 * @author: Lukafurlan
	 */

	private final Method method;
	private String response;

	public HTTPMethodHandler(Method method, String response) {
		this.method = method;
		this.response = response;
	}

	@Override
	public void handle(HttpExchange t) throws IOException {
		if (t.getRemoteAddress().getAddress().getHostAddress().toString()
				.equals(HTTPService.IPV4_ADDRESS)) {
			try {
				method.invoke(null, t.getRequestURI().getQuery());
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			t.sendResponseHeaders(200, response.length());
			try (OutputStream os = t.getResponseBody()) {
				os.write(response.getBytes());
			}
		}
	}
}