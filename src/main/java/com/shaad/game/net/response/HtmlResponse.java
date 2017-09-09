package com.shaad.game.net.response;

import com.google.common.io.Resources;
import com.shaad.game.net.HttpStatus;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Getter
public abstract class HtmlResponse implements Response {
    private final HttpStatus status;
    private final Map<String, String> substituteParams;

    public HtmlResponse(HttpStatus httpStatus, Map<String, String> substituteParams) {
        this.status = httpStatus;
        this.substituteParams = substituteParams;
    }

    public HtmlResponse(HttpStatus httpStatus) {
        this.status = httpStatus;
        this.substituteParams = new HashMap<>();
    }

    @Override
    public List<String> getHeaders() {
        return new ArrayList<>();
    }

    @Override
    public String getBody() {
        String page = getPageTemplate();
        for (Map.Entry<String, String> entry : substituteParams.entrySet()) {
            page = page.replace("%%" + entry.getKey() + "%%", entry.getValue());
        }
        return page;
    }

    protected abstract String getPageTemplate();

    protected static String readPageTemplateFromResources(String htmlName) {
        try {
            return Resources.toString(
                    Resources.getResource(String.format("html/%s.html", htmlName)),
                    Charset.defaultCharset());
        } catch (IOException e) {
            log.error("Failed to get {} html", htmlName);
            throw new RuntimeException(e);
        }
    }
}
