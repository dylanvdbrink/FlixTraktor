package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.time.ZonedDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TraktTitle {
	
	@JsonProperty("collected_at")
	private ZonedDateTime watchedAt;
	private Ids ids;
	
}
