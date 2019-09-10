package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MovieSearchResult {
	
	private String tagline;
	private LocalDate released;
	private String country;
	private String trailer;
	private String homepage;
	private String language;
	private String[] genres;
	private String certification;

}