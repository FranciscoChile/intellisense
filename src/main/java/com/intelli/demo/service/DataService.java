package com.intelli.demo.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.intelli.demo.model.AssetResponse;

@Service
public class DataService {
    

    public Map process(int p) {
         
        Map assetResponse = getData(p);

        return assetResponse;
    }


private Map getData(int p) {

    String urlString = "https://reference.intellisense.io/test.dataprovider";

    Double addTimes = new Double(0);
    String assetId = "";
    LinkedHashMap<String, List<Object>> l = new LinkedHashMap<>();

    LinkedHashMap<String, LinkedHashMap<String, List<Object>>> assetMap = new LinkedHashMap<>();
    LinkedHashMap<String, List<Object>> valueMap = new LinkedHashMap<>(); 

    WebClient webClient = WebClient.builder()
        .baseUrl(urlString)
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .build();

    AssetResponse result = webClient.get()
		.retrieve()
		.bodyToMono(AssetResponse.class).block();

    Map<String, Object> parentObj = result.getList();

    if (parentObj.size() > 0)
        assetId = (String)parentObj.keySet().toArray()[0];

    List<Object> list = parentObj
        .entrySet()
        .stream()
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());

    if (list.size() > 0) {
        l = (LinkedHashMap<String, List<Object>>) list.get(0);
        
        Set<String> keys = l.keySet();
        
        for (String key : keys) {

            List<Object> averages = new ArrayList<>();

            if (!key.equals("time")) {
                //System.out.println(key);
                addTimes = 0.0;
                List<Object> valueList = (List<Object>)l.get(key);
                
                for (int i=0; i< valueList.size() ; i++) {                  
                    int resto = i % p;
                    if (valueList.get(i) != null) {
                        if (resto != 0 || i == 0) {
                            String n = String.valueOf(valueList.get(i));
                            addTimes = addTimes + Double.parseDouble(n);
                        }                
                        else {
                            //System.out.println(addTimes);
                            averages.add(addTimes / p);
                            String n = String.valueOf(valueList.get(i));
                            addTimes = Double.parseDouble(n);
                        }
                    }
                }                                                
  
                valueMap.put(key, averages);

            } else {

                List<Object> timeList = new ArrayList<>();
                List<Object> valueList = (List<Object>)l.get(key);

                for (int i=0; i< valueList.size() ; i++) {                  
                    int resto = i % p;
                    if (valueList.get(i) != null) {
                        if (resto == 0 && i > 0) {
                            String n = String.valueOf(valueList.get(i));
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


}
