package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto;

import java.time.ZonedDateTime;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EpisodeSearchResult extends TitleSearchResult {
	
	private int season;
	private int number;
	private ZonedDateTime firstAired;
	
}
