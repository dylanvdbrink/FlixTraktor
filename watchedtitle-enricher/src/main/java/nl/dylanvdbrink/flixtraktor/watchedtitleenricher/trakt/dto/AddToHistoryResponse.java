package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.util.List;

import lombok.Data;

@Data
public class AddToHistoryResponse {
	private AddedHistory added;
	private NotFoundHistory notFound;
	
	@Data
	public class AddedHistory {
		private int movies;
		private int episodes;
	}
	
	@Data
	public class NotFoundHistory {
		private List<TraktTitle> movies;
		private List<TraktTitle> shows;
		private List<TraktTitle> seasons;
		private List<TraktTitle> episodes;
		private List<TraktTitle> people;
	}
}
