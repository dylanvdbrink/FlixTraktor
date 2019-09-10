package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo;

public enum AuthenticationErrorType {
	PENDING(400),				// Waiting for the user to authorize your app
	INVALID_DEVICECODE(404),	// Invalid device_code
	ALREADY_APPROVED(409),		// User already approved this code
	EXPIRED(410),				// The tokens have expired, restart the process
	USER_DENIED(418),			// User explicitly denied this code
	SLOW_DOWN(429);				// App is polling too quickly
	
	public final int errorCode;
	
	private AuthenticationErrorType(int errorCode) {
		this.errorCode = errorCode;
	}
}
