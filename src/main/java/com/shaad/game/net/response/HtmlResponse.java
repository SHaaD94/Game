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

import static com.google.common.base.Preconditions.checkNotNull;

@Slf4j
@Getter
public abstract class HtmlResponse implements Response {
    private final HttpStatus status;
    private final Map<String, String> substituteParams;
    protected final List<String> headers = new ArrayList<>();

    public HtmlResponse(HttpStatus httpStatus, Map<String, String> substituteParams) {
        this.status = httpStatus;
        this.substituteParams = substituteParams;
    }

    public HtmlResponse(HttpStatus httpStatus) {
        this.status = httpStatus;
        this.substituteParams = new HashMap<>();
    }

    @Override
    public String getBody() {
        String page = getPageTemplate();
        for (Map.Entry<String, String> entry : substituteParams.entrySet()) {
            page = page.replace("%%" + entry.getKey() + "%%", entry.getValue());
        }
        return page;
    }

    public void setCookie(String name, String value) {
        checkNotNull(name, "Cookie name should not be null");
        checkNotNull(value, "Cookie value should not be null");
        this.headers.add(String.format("Set-Cookie: %s=%s", name, value));
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
