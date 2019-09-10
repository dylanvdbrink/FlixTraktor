package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import lombok.Data;

@Data
public class SearchResult {
	
	private String type;
	private double score;
	
	private EpisodeSearchResult episode;
	private ShowSearchResult show;
	private MovieSearchResult movie;
	
}
