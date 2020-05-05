package de.mpg.mpdl.mpadmanager.controller;

import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import de.mpg.mpdl.mpadmanager.dto.RecordResponseDTO;
import de.mpg.mpdl.mpadmanager.dto.SearchItem;
import de.mpg.mpdl.mpadmanager.dto.SearchResponseDTO;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import de.mpg.mpdl.mpadmanager.dto.RecordDTO;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import de.mpg.mpdl.mpadmanager.logging.LoggingRequestInterceptor;

@RestController
public class EbooksController {

  @Autowired
  RestTemplate restTemplate;

  String thumbnailPrefix = "https://ebooks4-qa.mpdl.mpg.de/ebooks/Cover/Show?size=small&isbn=";

  @RequestMapping(value = "/search", method = RequestMethod.GET)
  public SearchResponseDTO searchEbooks(final String lookfor) {
    
    final HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    final HttpEntity <String> entity = new HttpEntity<String>(headers);
    String url = "https://ebooks4-qa.mpdl.mpg.de/ebooks/api/v1/search";
    UriComponentsBuilder builder=  
      UriComponentsBuilder.fromUriString(url)
                          .queryParam("lookfor", lookfor)
                          .queryParam("type", "AllFields")
                          .queryParam("filter[]", "~prodcode_str_mv:Springer")
                          .queryParam("sort", "relevance")
                          .queryParam("page", "1")
                          .queryParam("limit", "20")
                          .queryParam("prettyPrint", "false")
                          .queryParam("lng", "en");
    
    // RestTemplate restTemplate1 = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
    // ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
    // interceptors.add(new LoggingRequestInterceptor());
    // restTemplate1.setInterceptors(interceptors);

    final ResponseEntity<String> resp = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, entity, String.class);
    final Gson gson = new GsonBuilder().create();
    final SearchResponseDTO responseDTO = gson.fromJson(resp.getBody(), SearchResponseDTO.class);
    for (SearchItem searchItem: responseDTO.getRecords()) {
      if (!searchItem.getIsbns().isEmpty()) {
        searchItem.setThumbnail(thumbnailPrefix + searchItem.getIsbns().get(0));
      }
    }
    return responseDTO;
  }

  @RequestMapping(value = "/record", method = RequestMethod.GET)
  public RecordResponseDTO getRecordById(final String id) {
    
    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
      HttpEntity <String> entity = new HttpEntity<String>(headers);
      String url = "https://ebooks4-qa.mpdl.mpg.de/ebooks/api/v1/record";
      UriComponentsBuilder builder=  
        UriComponentsBuilder.fromUriString(url)
                            .queryParam("id", id)
                            .queryParam("prettyPrint", "false")
                            .queryParam("lng", "en");
      ResponseEntity<String> resp = restTemplate.exchange(builder.buildAndExpand().toUri(), HttpMethod.GET, entity, String.class);
      Gson gson = new GsonBuilder().create();
      RecordResponseDTO responseDTO = gson.fromJson(resp.getBody(), RecordResponseDTO.class);

      String[] urls = new String[] {"https://keeper.mpdl.mpg.de/f/af7817784ee142e08ecd/?dl=1", "https://keeper.mpdl.mpg.de/f/055e7a51f96a4202843e/?dl=1"};
      
      //todo: remove dummy data
      boolean[] formats = new boolean[] {true, false};
      for (RecordDTO record: responseDTO.getRecords()) {
        int format = record.getTitle().length() % 2; 
        record.setIsPdf(formats[format]);
        record.setDownloadUrl(urls[format]);
        if (!record.getIsbns().isEmpty()) {
          record.setThumbnail(thumbnailPrefix + record.getIsbns().get(0));
        }
      }
      return responseDTO;
  }

}