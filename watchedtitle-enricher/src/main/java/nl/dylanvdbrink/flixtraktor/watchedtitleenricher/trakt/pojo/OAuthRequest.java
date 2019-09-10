package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class OAuthRequest {
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("client_id")
	private String clientId;
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("client_secret")
	private String clientSecret;
	
	@JsonInclude(Include.NON_NULL)
	@JsonProperty("code")
	private String deviceCode;
}
