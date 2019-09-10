package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetTokenResponse {
	
	@JsonProperty("access_token")
	private String accessToken;
	
	@JsonProperty("token_type")
	private String tokenType;
	
	@JsonProperty("expires_in")
	private int expiresIn;
	
	@JsonProperty("refresh_token")
	private String refreshToken;
	
	@JsonProperty("scope")
	private String scope;
	
	@JsonProperty("created_at")
	private int createdAt;
	
}
