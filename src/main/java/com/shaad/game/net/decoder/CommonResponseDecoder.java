package com.shaad.game.net.decoder;

import com.shaad.game.net.response.HtmlResponse;
import com.shaad.game.net.response.JsonResponse;
import com.shaad.game.net.response.Response;

import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

public class CommonResponseDecoder implements ResponseDecoder {
    private static final Map<Class<? extends Response>, ResponseDecoder> responseDecoders = new HashMap<>();

    static {
        responseDecoders.put(JsonResponse.class, new JsonResponseDecoder());
        responseDecoders.put(HtmlResponse.class, new HtmlResponseDecoder());
    }

    @Override
    public String decode(Response response) {
        ResponseDecoder responseDecoder = responseDecoders.get(response.getClass());
        if (responseDecoder == null) {
            //fixme: implement another solution
            responseDecoder = responseDecoders.get(response.getClass().getSuperclass());
        }
        checkNotNull(responseDecoder, "Can't find decoder for class {}", response.getClass());
        return responseDecoder.decode(response);
    }
}
