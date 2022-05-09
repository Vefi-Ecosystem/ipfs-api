package org.vefi.ipfs.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vefi.ipfs.service.IPFSService;

@RestController
@RequestMapping("ipfs")
public class IPFSRestController {

  @Autowired
  private IPFSService service;

  @PostMapping(value = "/file")
  public ResponseEntity<Map<String, Object>> uploadFile(@RequestParam("file") MultipartFile file) {
    try {
      Map<String, Object> res = new HashMap<>();
      HttpHeaders headers = new HttpHeaders();
      String hash = service.saveFile(file);
      res.put("response", hash);
      headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res);
    } catch (Exception e) {
      Map<String, Object> errorRes = new HashMap<>();
      errorRes.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }
  }

  @PostMapping(value = "/json")
  public ResponseEntity<Map<String, Object>> storeJson(@RequestBody Map<String, Object> body) {
    try {
      Map<String, Object> res = new HashMap<>();
      HttpHeaders headers = new HttpHeaders();
      String hash = service.saveJson(body);
      res.put("response", hash);
      headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
      return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body(res);
    } catch (Exception e) {
      Map<String, Object> errorRes = new HashMap<>();
      errorRes.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }
  }

  @GetMapping(value = "/{hash}")
  public ResponseEntity<?> getItem(@PathVariable("hash") String hash) {
    try {
      HttpHeaders headers = new HttpHeaders();
      byte[] bytes = service.getItem(hash);
      headers.add("Content-Type", MediaType.ALL_VALUE);
      return ResponseEntity.status(HttpStatus.OK).headers(headers).body(bytes);
    } catch (Exception e) {
      Map<String, Object> errorRes = new HashMap<>();
      errorRes.put("error", e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorRes);
    }
  }
}
