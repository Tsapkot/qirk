package org.wrkr.clb.statistics.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.wrkr.clb.common.util.strings.CharSet;
import org.wrkr.clb.common.util.strings.JsonUtils;
import org.wrkr.clb.statistics.services.EventDispatcherService;

import com.fasterxml.jackson.databind.JsonNode;


@Controller
@RequestMapping(path = "event", produces = MediaType.TEXT_PLAIN_VALUE + ";charset=" + CharSet.UTF8)
public class EventController {

    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);

    private static class Response {
        public static final String OK = "OK";
        public static final String EMPTY = "";
        public static final String ERROR = "ERROR";
    }

    @Autowired
    private EventDispatcherService dispatcherService;

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    private @ResponseBody String handleHttpMessageNotReadableException(
            @SuppressWarnings("unused") HttpMessageNotReadableException e) {
        return Response.EMPTY;
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    private @ResponseBody String handleNotAcceptable(@SuppressWarnings("unused") HttpMediaTypeNotAcceptableException e) {
        return Response.EMPTY;
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    private @ResponseBody String handleMediaTypeNotSupported(@SuppressWarnings("unused") HttpMediaTypeNotSupportedException e) {
        return Response.EMPTY;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    private @ResponseBody String handleException(Exception e) {
        LOG.error("Exception caught at controller", e);
        return Response.ERROR;
    }

    @PostMapping(value = "/")
    public @ResponseBody String post(@RequestBody JsonNode json) throws Exception {
        dispatcherService.onMessage(JsonUtils.convertJsonToMapUsingLongForInts(json));
        return Response.OK;
    }
}
