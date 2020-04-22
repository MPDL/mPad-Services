package de.mpg.mpdl.mpadmanager.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

@RestController
public class EbooksController {

  @Autowired
  RestTemplate restTemplate;

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public String searchEbooks(String lookfor) {
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    HttpEntity <String> entity = new HttpEntity<String>(headers);
    String url = "https://ebooks4-qa.mpdl.mpg.de/ebooks/api/v1/search?lookfor=" + lookfor + "&type=AllFields&sort=relevance&page=1&limit=20&prettyPrint=false&lng=en";
    return restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();
  }
}