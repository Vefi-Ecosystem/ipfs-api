package org.vefi.ipfs.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vefi.ipfs.interfaces.IPFSServiceImpl;

@Service
public class IPFSService implements IPFSServiceImpl {

  @Autowired
  IPFS ipfs;

  @Override
  public String saveFile(MultipartFile file) {
    try {
      InputStream stream = new ByteArrayInputStream(file.getBytes());
      NamedStreamable.InputStreamWrapper iStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);
      MerkleNode res = ipfs.add(iStreamWrapper).get(0);
      return res.hash.toBase58();
    } catch (Exception exc) {
      throw new RuntimeException(exc.getMessage());
    }
  }

  @Override
  public String saveJson(Map<String, Object> json) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public byte[] getItem(String hash) {
    // TODO Auto-generated method stub
    return null;
  }
}
