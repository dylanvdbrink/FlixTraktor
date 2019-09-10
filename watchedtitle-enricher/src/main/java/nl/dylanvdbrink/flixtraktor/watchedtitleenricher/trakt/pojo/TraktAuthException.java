package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo;

import lombok.Getter;

public class TraktAuthException extends Exception {
	private static final long serialVersionUID = -3531364588275506627L;
	
	@Getter
	private final AuthenticationErrorType errorType;

	public TraktAuthException() {
		super();
		errorType = null;
	}
	
	public TraktAuthException(String message) {
		super(message);
		errorType = null;
	}
	
	public TraktAuthException(String message, AuthenticationErrorType aet) {
		super(message);
		errorType = aet;
	}
	
	public TraktAuthException(String message, Throwable t) {
		super(message, t);
		errorType = null;
	}
	
	public TraktAuthException(String message, Throwable t, AuthenticationErrorType aet) {
		super(message, t);
		errorType = aet;
	}
}
