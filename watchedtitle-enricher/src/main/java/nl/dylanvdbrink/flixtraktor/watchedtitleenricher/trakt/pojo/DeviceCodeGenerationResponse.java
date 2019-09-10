package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DeviceCodeGenerationResponse {
	
	@JsonProperty("device_code")
	private String deviceCode;
	
	@JsonProperty("user_code")
	private String userCode;
	
	@JsonProperty("verification_url")
	private String verificationUrl;
	
	@JsonProperty("expires_in")
	private int expiresIn;
	
	@JsonProperty("interval")
	private int interval;
	
}
