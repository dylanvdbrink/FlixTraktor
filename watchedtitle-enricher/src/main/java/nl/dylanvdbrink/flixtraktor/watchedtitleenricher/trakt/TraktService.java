package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.apachecommons.CommonsLog;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto.AddToHistoryRequest;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto.AddToHistoryResponse;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.dto.SearchResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SuppressWarnings("UnstableApiUsage")
@Service
@CommonsLog
public class TraktService {

    @Value("${trakt.clientId}")
    private String clientId;

    private final String accessToken;
    private static final String BASE_URL = "https://api.trakt.tv";

    public TraktService(String accessToken) {
        this.accessToken = accessToken;
    }

    /**
     * Query for a title
     *
     * @param type The type of the title. E.g.: movie, show, episode
     * @param query The query
     * @return
     * @throws UnirestException
     * @throws TraktApiException
     */
    @SuppressWarnings("serial")
    public List<SearchResult> searchTitles(String type, String query) throws TraktApiException {

        if (accessToken == null) {
            throw new TraktApiException("Access token was not specified.");
        }

        List<SearchResult> titles;
        Gson gson = new Gson();

        try {
            query = URLEncoder.encode(query, StandardCharsets.UTF_8.displayName());
        } catch (UnsupportedEncodingException e) {
            log.error(e);
        }
        String url = BASE_URL + MessageFormat.format("/search/{0}?query={1}&extended=full", type, query);

        HttpResponse<String> response;
        try {
            response = Unirest.get(url)
                    .headers(getTraktHeaders())
                    .asString();

            String responsebody = response.getBody();

            Type t = new TypeToken<List<SearchResult>>(){}.getType();
            titles = gson.fromJson(responsebody, t);
        } catch (UnirestException e) {
            throw new TraktApiException("Error while doing trakt request", e);
        }

        return titles;
    }

    /**
     * Add items to the user's watch history
     *
     * @param request
     * @throws UnirestException
     */
    public AddToHistoryResponse addToHistory(AddToHistoryRequest request) throws TraktApiException {
        String url = BASE_URL + "/sync/history";

        Gson gson = new Gson();
        String responseBody = gson.toJson(request);
        HttpResponse<String> response;
        try {
            response = Unirest.post(url)
                    .headers(getTraktHeaders())
                    .body(responseBody)
                    .asString();

            if (response.getStatus() == 201) {
                return gson.fromJson(response.getBody(), AddToHistoryResponse.class);
            } else {
                throw new TraktApiException("Add to history returned something other than 201");
            }
        } catch (UnirestException e) {
            throw new TraktApiException("Error while doing trakt request", e);
        }
    }

    private Map<String, String> getTraktHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);
        headers.put("Content-Type", "application/json");
        headers.put("trakt-api-key", clientId);
        headers.put("trakt-api-version", "2");

        return headers;
    }

}
