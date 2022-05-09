package org.vefi.ipfs.interfaces;

import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

public interface IPFSServiceImpl {
  String saveFile(MultipartFile file);
  String saveJson(Map<String, Object> json);
  byte[] getItem(String hash);
}
