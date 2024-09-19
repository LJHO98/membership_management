package com.membership.Service;

import com.membership.Config.CaptchaConfig;
import com.membership.Util.RecaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class CaptchaService {

    @Autowired
    private CaptchaConfig captchaConfig;

    public boolean verifyCaptcha(String response) {
        String url = "https://www.google.com/recaptcha/api/siteverify";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", captchaConfig.getSecret());
        params.add("response", response);

        ResponseEntity<Map> responseEntity = restTemplate.postForEntity(url, params, Map.class);
        Map<String, Object> body = responseEntity.getBody();
        return (Boolean) body.get("success");
    }
}
