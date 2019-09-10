package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.util.List;

import lombok.Data;

@Data
public class AddToHistoryRequest {
	
	private List<TraktTitle> movies;
	private List<TraktTitle> shows;
	private List<TraktTitle> episodes;
	
}
