package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ShowSearchResult extends TitleSearchResult {
	
	private int year;
	private ZonedDateTime firstAired;
	private Map<String, String> airs;
	private String certification;
	private String network;
	private String country;
	private String trailer;
	private String homepage;
	private String status;
	private String language;
	private List<String> genres;
	private int airedEpisodes;
	
}
