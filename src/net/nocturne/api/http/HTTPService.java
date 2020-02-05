package net.nocturne.api.http;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import net.nocturne.Settings;
import net.nocturne.utils.Logger;

import com.sun.net.httpserver.HttpServer;

public class HTTPService {

	/**
	 * @author: Pax M
	 * @author: Lukafurlan
	 */

	protected static String IPV4_ADDRESS = "0:0:0:0:0:0:0:1";

	public static Map<String, Method> serviceRead = new HashMap<>();
	public static Map<String, Method> serviceMethod = new HashMap<>();

	private void execute() throws IOException, NoSuchMethodException,
			SecurityException {
		serviceRead.put("check",
				HTTPInstruction.class.getMethod("isOnline", String.class));
		serviceRead.put("online", HTTPInstruction.class.getMethod(
				"getPlayersCount", String.class));
		serviceRead.put("staff",
				HTTPInstruction.class.getMethod("getStaffCount", String.class));
		serviceRead.put("uptime",
				HTTPInstruction.class.getMethod("getUptime", String.class));
		serviceRead.put("logger",
				HTTPInstruction.class.getMethod("getLogger", String.class));
		serviceRead.put("players",
				HTTPInstruction.class.getMethod("getPlayers", String.class));
		serviceRead.put("saveAll",
				HTTPInstruction.class.getMethod("saveAll", String.class));
		serviceRead
				.put("friendsChat", HTTPInstruction.class.getMethod(
						"getFriendsChat", String.class));

		serviceMethod.put("sendMessage",
				HTTPInstruction.class.getMethod("sendMessage", String.class));
		serviceMethod.put("updateServer",
				HTTPInstruction.class.getMethod("updateServer", String.class));

		HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
		for (Map.Entry<String, Method> entry : serviceRead.entrySet()) {
			Method method = entry.getValue();
			server.createContext("/" + entry.getKey(), new HTTPReadHandler(
					method, ""));
		}
		for (Map.Entry<String, Method> entry : serviceMethod.entrySet()) {
			Method method = entry.getValue();
			server.createContext("/" + entry.getKey(), new HTTPMethodHandler(
					method, ""));
		}
		server.setExecutor(null);
		server.start();
	}

	public static void create() throws IOException, NoSuchMethodException,
			SecurityException {
		if (!Settings.HTTP) {
			Logger.logErr("HTTPService",
					"HTTPService executor denied access, service is closed.");
			return;
		}
		HTTPService service = new HTTPService();
		service.execute();
	}

	public static Map<String, String> queryToMap(String query) {
		Map<String, String> result = new HashMap<String, String>();
		for (String param : query.split("&")) {
			String pair[] = param.split("=");
			if (pair.length > 1)
				result.put(pair[0], pair[1]);
			else
				result.put(pair[0], "");
		}
		return result;
	}
}