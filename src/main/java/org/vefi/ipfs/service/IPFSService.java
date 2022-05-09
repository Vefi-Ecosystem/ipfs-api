package org.vefi.ipfs.service;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;
import org.json.simple.JSONObject;
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
    try {
      JSONObject jsonObject = new JSONObject(json);
      String content = jsonObject.toJSONString();
      LocalDateTime now = LocalDateTime.now();
      String name = (String) json.get("name");
      Path utfFile = Files.createTempFile(
        String.format(
          "%s-%s-%s",
          now.format(DateTimeFormatter.ofPattern("yyyy:MMMM:dd:hh:mm")),
          name,
          UUID.randomUUID().toString()
        ),
        ".json"
      );

      Files.write(utfFile, content.getBytes(StandardCharsets.UTF_8));

      InputStream stream = new ByteArrayInputStream(Files.readAllBytes(utfFile));
      NamedStreamable.InputStreamWrapper iStreamWrapper = new NamedStreamable.InputStreamWrapper(stream);
      MerkleNode res = ipfs.add(iStreamWrapper).get(0);

      // System.out.println(utfFile.toString());

      Files.delete(utfFile);

      return res.hash.toBase58();
    } catch (Exception exc) {
      throw new RuntimeException(exc.getMessage());
    }
  }

  @Override
  public byte[] getItem(String hash) {
    try {
      Multihash multihash = Multihash.fromBase58(hash);
      return ipfs.cat(multihash);
    } catch (Exception exc) {
      throw new RuntimeException(exc.getMessage());
    }
  }
}
