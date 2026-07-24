package com.pagepulse.pagepulse.service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.pagepulse.pagepulse.dto.AuditRequest;
import com.pagepulse.pagepulse.dto.AuditResponse;

@Service
public class AuditService {

    public AuditResponse audit(AuditRequest request) {

        String url = request.getUrl().trim();

        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }

        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid URL. Please enter a valid URL.");
        }

        String scheme = uri.getScheme();
        String host = uri.getHost();

        if (scheme == null
                || host == null
                || !host.contains(".")
                || !(scheme.equalsIgnoreCase("http")
                || scheme.equalsIgnoreCase("https"))) {

            throw new IllegalArgumentException(
                    "Invalid URL. Please enter a valid URL.");
        }

        try {

            long start = System.currentTimeMillis();

            Connection.Response connectionResponse = Jsoup.connect(url)
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute();

            long end = System.currentTimeMillis();

            String contentType = connectionResponse.contentType();

            if (contentType == null
                    || !contentType.toLowerCase().startsWith("text/html")) {

                throw new IllegalArgumentException(
                        "The provided URL does not point to an HTML webpage.");
            }

            Document document = connectionResponse.parse();

            if (document.body() == null) {
                throw new IllegalArgumentException(
                        "The provided URL does not point to a valid HTML webpage.");
            }

            AuditResponse response = new AuditResponse();

            response.setUrl(url);
            response.setHttpStatus(connectionResponse.statusCode());
            response.setResponseTime(end - start);
            response.setPageTitle(document.title());

            Element metaDescription = document.selectFirst("meta[name=description]");

            response.setMetaDescription(
                    metaDescription != null
                            ? metaDescription.attr("content")
                            : "Not Available");

            response.setH1Count(document.select("h1").size());

            int missingAlt = 0;

            Elements images = document.select("img");

            for (Element image : images) {
                if (!image.hasAttr("alt")
                        || image.attr("alt").isBlank()) {
                    missingAlt++;
                }
            }

            response.setMissingAltImages(missingAlt);

            String bodyText = document.body() != null
                    ? document.body().text()
                    : "";

            int wordCount = bodyText.isBlank()
                    ? 0
                    : bodyText.trim().split("\\s+").length;

            response.setApproximateWordCount(wordCount);

            return response;

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
