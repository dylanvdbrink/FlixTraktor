package nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.io.FileUtils;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import lombok.extern.apachecommons.CommonsLog;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.pojo.*;
import nl.dylanvdbrink.flixtraktor.watchedtitleenricher.trakt.utils.QRCodeGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
@CommonsLog
public class TraktAuthenticationService {

    @Value("${trakt.clientId}")
    private String clientId;

    @Value("${trakt.clientSecret}")
    private String clientSecret;

    private static final String BASE_URL = "https://api.trakt.tv/";
    private static final String DEVICECODE = "oauth/device/code";
    private static final String TOKEN = "oauth/device/token";

    public String doInitialTraktAuthentication() throws TraktAuthException, IOException {
        // Get user code and print it
        DeviceCodeGenerationResponse deviceCode = this.generateDeviceCode();
        System.out.println(this.getPrintableDeviceCode(deviceCode));

        // Poll getToken to check if the user has authorized the account
        boolean tryAgain = true;
        int interval = 3000;
        GetTokenResponse token = null;
        while (tryAgain) {
            // Wait x seconds before trying again
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                log.error(e);
                Thread.currentThread().interrupt();
            }

            try {
                token = this.getToken(deviceCode);
            } catch (TraktAuthException e) {
                if (e.getErrorType() == AuthenticationErrorType.PENDING) {
                    log.info(e.getMessage());
                } else if (e.getErrorType() == AuthenticationErrorType.ALREADY_APPROVED) {
                    tryAgain = false;
                } else if (e.getErrorType() == AuthenticationErrorType.EXPIRED ||
                        e.getErrorType() == AuthenticationErrorType.INVALID_DEVICECODE ||
                        e.getErrorType() == AuthenticationErrorType.USER_DENIED) {
                    throw new TraktAuthException(e.getMessage(), e);
                } else if (e.getErrorType() == AuthenticationErrorType.SLOW_DOWN) {
                    if (interval == 10000) {
                        throw new TraktAuthException("Interval was increased too much, stopped trying");
                    }
                    interval += 1000;
                    log.info(e.getMessage() + ". Setting interval to " + interval);
                } else {
                    throw new TraktAuthException(e.getMessage(), e);
                }
            }
        }

        if (token != null) {
            return token.getAccessToken();
        } else {
            throw new TraktAuthException("Could not get access token");
        }
    }

    private DeviceCodeGenerationResponse generateDeviceCode() throws TraktAuthException, IOException {
        OAuthRequest request = new OAuthRequest()
                .setClientId(clientId);

        ObjectMapper objectmapper = new ObjectMapper();

        try {
            String s = Unirest.post(BASE_URL + DEVICECODE)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(objectmapper.writeValueAsString(request))
                    .asString()
                    .getBody();

            return objectmapper.readValue(s, DeviceCodeGenerationResponse.class);
        } catch (UnirestException e) {
            throw new TraktAuthException("Error while getting device code", e);
        }
    }

    private GetTokenResponse getToken(DeviceCodeGenerationResponse deviceCode) throws TraktAuthException, IOException {
        OAuthRequest request = new OAuthRequest()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setDeviceCode(deviceCode.getDeviceCode());

        ObjectMapper objectmapper = new ObjectMapper();

        try {
            HttpResponse<String> response = Unirest.post(BASE_URL + TOKEN)
                    .header("Content-Type", "application/json; charset=utf-8")
                    .body(objectmapper.writeValueAsString(request))
                    .asString();

            switch (response.getStatus()) {
                case 200:
                    return objectmapper.readValue(response.getBody(), GetTokenResponse.class);
                case 400:
                    throw new TraktAuthException("Waiting for user authorization", AuthenticationErrorType.PENDING);
                case 404:
                    throw new TraktAuthException("Invalid device_code", AuthenticationErrorType.INVALID_DEVICECODE);
                case 409:
                    throw new TraktAuthException("User already approved this code", AuthenticationErrorType.ALREADY_APPROVED);
                case 410:
                    throw new TraktAuthException("The tokens has expired, restart the process", AuthenticationErrorType.EXPIRED);
                case 418:
                    throw new TraktAuthException("User explicitly denied this code", AuthenticationErrorType.USER_DENIED);
                case 429:
                    throw new TraktAuthException("App is polling too quickly", AuthenticationErrorType.SLOW_DOWN);
                default:
                    throw new TraktAuthException("Got unexpected status code: " + response.getStatus());
            }

        } catch (UnirestException e) {
            throw new TraktAuthException("Error while getting token", e);
        }
    }

    private String getPrintableDeviceCode(DeviceCodeGenerationResponse deviceCode) throws IOException {
        File f = ResourceUtils.getFile("classpath:authmessage.txt");
        String message = FileUtils.getContentsAsString(f);
        message = message
                .replace("URL", deviceCode.getVerificationUrl())
                .replace("USERCODE", deviceCode.getUserCode())
                .replace("QRCODE", QRCodeGenerator.generate(deviceCode.getVerificationUrl()));

        return message;
    }

}
