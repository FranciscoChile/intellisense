package com.intelli.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.intelli.demo.model.AssetResponse;
import com.intelli.demo.service.DataService;

@ExtendWith(MockitoExtension.class)
class DataControllerTest {

	@Mock
	private DataService dataService;


	AssetResponse resultData = new AssetResponse();
	ObjectMapper mapper = new ObjectMapper();
	LinkedHashMap<String, LinkedHashMap<String, List<Object>>> assetMap = new LinkedHashMap<>();
	
	File intelli = new File(getClass().getResource("/intelli.json").getFile()); 
	File intelliResult = new File(getClass().getResource("/intelliResult.json").getFile()); 

	@BeforeEach
	void init() throws StreamReadException, DatabindException, IOException {
		resultData = mapper.readValue(intelli, AssetResponse.class);
		assetMap =  (LinkedHashMap<String, LinkedHashMap<String, List<Object>>>) mapper.readValue(intelliResult, LinkedHashMap.class);
	}

	@Test
	void shouldProcessDataAndGetKeySet() throws Exception {

		given(dataService.getData(60, resultData) ).willReturn(this.assetMap);
        Map expected = dataService.getData(60, resultData);
        assertEquals(expected.keySet(), this.assetMap.keySet());

	}

	@Test
	void shouldProcessDataAndGetKeySetSize() throws Exception {

		given(dataService.getData(60, resultData) ).willReturn(this.assetMap);
        var expected = dataService.getData(60, resultData);

		var parentObj = resultData.getList();

		var list = parentObj
			.entrySet()
			.stream()
			.map(Map.Entry::getValue)
			.collect(Collectors.toList());

		var l = (LinkedHashMap<String, List<Object>>) list.get(0);
		var keys = l.keySet().size();

		var sizeExpected = ((LinkedHashMap) expected.values().toArray()[0]).size();	

		var obj = (LinkedHashMap) expected.values().toArray()[0];
		
		((ArrayList)obj.values().toArray()[0]).size() ;

        assertEquals(sizeExpected, keys);

	}


	@Test
	void shouldProcessDataAndGetAverages() throws Exception {

		given(dataService.getData(60, resultData) ).willReturn(this.assetMap);
        var expected = dataService.getData(60, resultData);
		
		var obj = (LinkedHashMap) expected.values().toArray()[0];		
		var averages = ((ArrayList)obj.values().toArray()[0]).size();

        assertEquals(averages, 3);

	}


}
