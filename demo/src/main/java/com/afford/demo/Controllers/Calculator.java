package com.afford.demo.Controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/numbers")
public class Calculator {
      int windowSize=10;
      List<Integer> window=new ArrayList<>();
      RestTemplate restTemplate=new RestTemplate();
        @GetMapping("/{numberId}")
        public ResponseEntity<?> getNumbers(@PathVariable String numberId) {
            if (!Arrays.asList("p", "f", "e", "r").contains(numberId)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid ID");
            }
            List<Integer> newNumbers = fetchNumbers(numberId);
            List<Integer> prevState = new ArrayList<>(window);
            for (Integer number : newNumbers) {
                if (!window.contains(number)) {
                    window.add(number);
                    if (window.size() > windowSize) {
                        window.remove(0);
                    }
                }
            }
            List<Integer> currState = new ArrayList<>(window);
            double avg = window.stream().mapToInt(Integer::intValue).average().orElse(0);
            Map<String, Object> response = new HashMap<>();
            response.put("numbers", newNumbers);
            response.put("windowPrevState", prevState);
            response.put("windowCurrState", currState);
            response.put("avg", Math.round(avg * 100.0) / 100.0);
            return ResponseEntity.ok(response);
    }
        private List<Integer> fetchNumbers(String numberId) {
                String numId="";
                switch(numberId){
                    case "e":
                    numId="even";
                    break;
                    case "f":
                    numId="fibbo";
                    break;
                    case "p":
                    numId="primes";
                    break;
                    case "r":
                    numId="rand";
                    break;
                }
                String token="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJNYXBDbGFpbXMiOnsiZXhwIjoxNzE4MzUwODE1LCJpYXQiOjE3MTgzNTA1MTUsImlzcyI6IkFmZm9yZG1lZCIsImp0aSI6IjcyOGJmMGExLTkxZjMtNDZlYi1iMjNmLWZkYTQ2ZmFkODM1YSIsInN1YiI6IjcyNzgyMXR1aXQxMzlAc2tjdC5lZHUuaW4ifSwiY29tcGFueU5hbWUiOiJTcmlLcmlzaG5hIiwiY2xpZW50SUQiOiI3MjhiZjBhMS05MWYzLTQ2ZWItYjIzZi1mZGE0NmZhZDgzNWEiLCJjbGllbnRTZWNyZXQiOiJNS1FIVFZOSExpRXZncFlaIiwib3duZXJOYW1lIjoiU2l2YXNhbmthci5LIiwib3duZXJFbWFpbCI6IjcyNzgyMXR1aXQxMzlAc2tjdC5lZHUuaW4iLCJyb2xsTm8iOiI3Mjc4MjFUVUlUMTM5In0.rRKWnXRgLZ4wV45or2N0FAUwxdmoHIusLZDLJoyomzw";
                String url = "http://20.244.56.144/test/" + numId;        
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
                HttpEntity<?> entity = new HttpEntity<>(headers);
                
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
                
                if (response.getStatusCode() == HttpStatus.OK) {
                    return (List<Integer>) response.getBody().get("numbers");
                } else {
                    System.out.println("HTTP error code: " + response.getStatusCodeValue());
                    return null; 
                }
            }
}



