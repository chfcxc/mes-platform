package cn.emay.eucp.common.excepiton;

public class EucpException extends Exception {

	private static final long serialVersionUID = 1L;

	public EucpException() {
		super();
	}

	public EucpException(String message) {
		super(message);
	}

	public EucpException(Throwable throwable) {
		super(throwable);
	}

	public EucpException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
