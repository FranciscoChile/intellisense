package com.intelli.demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.intelli.demo.model.AssetResponse;

import reactor.core.publisher.Flux;

@Service
public class DataService {

    @Value("${intellisense.url}") 
    String urlString;

    public Map process(int p) {        
        
        var result = getIntellisenseData(urlString);
        var assetResponse = getData(p, result.blockFirst() );
        return assetResponse;
    }


    public Map getData(int p, AssetResponse result) {

        var addTimes = 0.0;
        var assetId = "";

        LinkedHashMap<String, List<Object>> l = new LinkedHashMap<>();
        LinkedHashMap<String, LinkedHashMap<String, List<Object>>> assetMap = new LinkedHashMap<>();
        LinkedHashMap<String, List<Object>> valueMap = new LinkedHashMap<>(); 

        var parentObj = result.getList();

        if (parentObj.size() > 0)
            assetId = (String)parentObj.keySet().toArray()[0];

        var list = parentObj
            .entrySet()
            .stream()
            .map(Map.Entry::getValue)
            .collect(Collectors.toList());

        if (list.size() > 0) {
            l = (LinkedHashMap<String, List<Object>>) list.get(0);
            
            var keys = l.keySet();
            
            for (String key : keys) {

                var averages = new ArrayList<>();

                if (!key.equals("time")) {
                    addTimes = 0.0;
                    var valueList = (List<Object>)l.get(key);
                    
                    for (var i=0; i< valueList.size() ; i++) {                  
                        var resto = i % p;
                        
                            if (resto != 0 || i == 0) {
                                var n = "0";
                                if (valueList.get(i) != null) n = String.valueOf(valueList.get(i));
                                addTimes = addTimes + Double.parseDouble(n);
                            }                
                            else {
                                averages.add(addTimes / p);
                                var n = "0";
                                if (valueList.get(i) != null) n = String.valueOf(valueList.get(i));
                                addTimes = Double.parseDouble(n);
                            }
                        
                    }                                                
    
                    valueMap.put(key, averages);

                } else {

                    List<Object> timeList = new ArrayList<>();
                    List<Object> valueList = (List<Object>)l.get(key);

                    for (var i=0; i< valueList.size() ; i++) {                  
                        var resto = i % p;
                        if (valueList.get(i) != null) {
                            if (resto == 0 && i > 0) {
                                var n = String.valueOf(valueList.get(i));
                                timeList.add(n);
                            }                        
                        }
                    }

                    valueMap.put(key, timeList);

                }       
            }                
        }

        assetMap.put(assetId, valueMap);        

        return assetMap;
    }


    public Flux<AssetResponse> getIntellisenseData(String urlString) {

        WebClient webClient = WebClient.builder()
            .baseUrl(urlString)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();

        Flux<AssetResponse> result = webClient.get()
            .retrieve()
            .bodyToFlux(AssetResponse.class);

        return result;

    }

}
