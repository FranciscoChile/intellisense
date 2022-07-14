package com.intelli.demo.model;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {
    
    private Map<String, Object> list = new HashMap<>();

    @JsonAnySetter
    public void setList(String name, Object value) {
        list.put(name, value);
    }
}
