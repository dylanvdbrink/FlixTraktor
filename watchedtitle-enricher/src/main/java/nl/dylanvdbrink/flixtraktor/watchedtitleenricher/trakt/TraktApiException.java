package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt;

public class TraktApiException extends Exception {
	private static final long serialVersionUID = 7224140223183142603L;

	public TraktApiException() {
		super();
	}
	
	public TraktApiException(String message) {
		super(message);
	}
	
	public TraktApiException(String message, Throwable t) {
		super(message, t);
	}
}
