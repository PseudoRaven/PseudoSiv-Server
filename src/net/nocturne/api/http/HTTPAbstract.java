package net.nocturne.api.http;

public abstract class HTTPAbstract {

	/**
	 * @author: Pax M
	 */

	private String parameters;
	private Object[] data;
	private boolean failed;

	public HTTPAbstract(String parameters, Object... data) {
		this.parameters = parameters;
		this.data = data;
	}

	public String getParameters() {
		return parameters;
	}

	public Object[] getData() {
		return data;
	}

	public boolean getFailed() {
		return failed;
	}

	public boolean getUpdate() {
		return false;
	}

	public void setFailed(boolean failed) {
		this.failed = failed;
	}
}