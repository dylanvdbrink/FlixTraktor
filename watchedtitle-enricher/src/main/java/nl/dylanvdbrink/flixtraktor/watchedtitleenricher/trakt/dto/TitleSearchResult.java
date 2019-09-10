package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.time.ZonedDateTime;

import lombok.Data;

@Data
public class TitleSearchResult {
	
	private String title;
	private Ids ids;
	private String overview;
	private double rating;
	private int commentCount;
	private int votes;
	private int runtime;
	private ZonedDateTime updatedAt;
	private String[] availableTranslations;
	
}
