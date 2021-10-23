package com.freddiemac.housing.service.request;


import java.net.URI;
import java.util.Objects;

public final class UriAndRequest {
    private final URI url;
    private final SuggestionRequest suggestionRequest;

    UriAndRequest(URI url, SuggestionRequest suggestionRequest)
    {
        this.url = url;
        this.suggestionRequest = suggestionRequest;
    }

    public URI url()
    {
        return url;
    }

    public SuggestionRequest suggestionRequest()
    {
        return suggestionRequest;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (UriAndRequest) obj;
        return Objects.equals(this.url, that.url) &&
                Objects.equals(this.suggestionRequest, that.suggestionRequest);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url, suggestionRequest);
    }

    @Override
    public String toString()
    {
        return "UriAndRequest[" +
                "url=" + url + ", " +
                "suggestionRequest=" + suggestionRequest + ']';
    }

}
