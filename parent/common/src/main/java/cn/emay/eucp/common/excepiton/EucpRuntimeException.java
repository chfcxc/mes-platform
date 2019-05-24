package cn.emay.eucp.common.excepiton;

public class EucpRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EucpRuntimeException() {
		super();
	}

	public EucpRuntimeException(String message) {
		super(message);
	}

	public EucpRuntimeException(Throwable throwable) {
		super(throwable);
	}

	public EucpRuntimeException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
