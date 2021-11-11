package com.deviseworks.util;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Internet {
    // Function: downloadFile
    // Argument: URL, Path
    // Description: Download the file to the path.
    public boolean downloadFile(URL url, Path path){
        final String urlPath = url.getPath();
        final String fileName = urlPath.substring(urlPath.lastIndexOf("/") + 1);

        try {
            Files.copy(url.openStream(), Paths.get(path + "/" + fileName), REPLACE_EXISTING);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    // Function: connectResponse
    // Argument: URL, (Timeout Seconds)
    // Description: Return the HttpResponse got from the url.
    public HttpResponse<String> connectResponse(String url, int timeout){
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(timeout)).build();
        HttpRequest  request = HttpRequest.newBuilder().uri(URI.create(url)).build();

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            return null;
        }
    }
    // Overload
    public HttpResponse<String> connectResponse(String url){
        return connectResponse(url, 20);
    }
}
