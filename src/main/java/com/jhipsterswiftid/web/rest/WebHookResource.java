package com.jhipsterswiftid.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.jhipsterswiftid.web.rest.dto.CapitalOneWebHookAccess;
import com.jhipsterswiftid.web.rest.dto.WebHook;
import com.jhipsterswiftid.web.rest.dto.WebHookRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WebHookResource {

    private final Logger log = LoggerFactory.getLogger(WebHookResource.class);

    @Inject
    private Environment environment;

    @RequestMapping(value = "/webhook",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void registerWebHook() {
        RestTemplate restTemplate = new RestTemplate();

        String capitalOneId = environment.getProperty("spring.social.capitalOne.clientId");
        String capitalOneSecret = environment.getProperty("spring.social.capitalOne.clientSecret");
        URI capitalOneEndpoint = UriComponentsBuilder.fromUriString("https://api-sandbox.capitalone.com/oauth/oauth20/token")
            .build()
            .toUri();

        MultiValueMap<String, String> capitalOneRequest = new LinkedMultiValueMap<String, String>();
        capitalOneRequest.add("client_id", capitalOneId);
        capitalOneRequest.add("client_secret", capitalOneSecret);
        capitalOneRequest.add("grant_type", "client_credentials");

        HttpHeaders accessTokenHeaders = new HttpHeaders();
        accessTokenHeaders.set("Accept", "*/*");
        accessTokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> accessTokenEntity = new HttpEntity<>(capitalOneRequest, accessTokenHeaders);

        ResponseEntity<CapitalOneWebHookAccess> capitalOneResponse = restTemplate.exchange(capitalOneEndpoint, HttpMethod.POST, accessTokenEntity, CapitalOneWebHookAccess.class);
        CapitalOneWebHookAccess capitalOneWebHookAccess = capitalOneResponse.getBody();

        log.debug("{}", capitalOneWebHookAccess);
        requestAccessHook(capitalOneWebHookAccess);
    }

    public void requestAccessHook(CapitalOneWebHookAccess capitalOneWebHookAccess) {
        RestTemplate restTemplate = new RestTemplate();

        URI capitalOneWebhookEndpoint = UriComponentsBuilder.fromUriString("https://api-sandbox.capitalone.com/identity/webhooks")
            .build()
            .toUri();

        WebHookRegistration webHookRegistration = new WebHookRegistration();
        webHookRegistration.setCallbackUrl("http://73.148.65.44:3002/api/callback-url");
        webHookRegistration.setEventType("EnhancedAuthentication");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + capitalOneWebHookAccess.getAccessToken());
        headers.set("Accept", MediaType.APPLICATION_JSON.toString() + ";v=1");
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<WebHookRegistration> request = new HttpEntity<>(webHookRegistration, headers);

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
        messageConverters.add(new MappingJackson2HttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        ResponseEntity<WebHook> webhookResponse = restTemplate.exchange(capitalOneWebhookEndpoint, HttpMethod.POST, request, WebHook.class);

        WebHook webHook = webhookResponse.getBody();
        log.debug("{}", webHook);
    }

    @RequestMapping(value = "/callback-url",
        method = RequestMethod.POST)
    @Timed
    public ResponseEntity callbackUrl() {
        String capitalOneId = environment.getProperty("spring.social.capitalOne.clientId");
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-C1-Verification", capitalOneId);
        return new ResponseEntity(responseHeaders, HttpStatus.OK);
    }

}
