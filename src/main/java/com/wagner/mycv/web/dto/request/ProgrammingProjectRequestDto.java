package com.wagner.mycv.web.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ProgrammingProjectRequestDto {

  @NotBlank
  private String name;
  private String description;

  @NotNull
  private List<String> technologiesUsed;

  private String vcsUrl;

  public Map<String, Object> toMap() {
    Map<String, Object> map = new HashMap<>();
    map.put("name", name);
    map.put("description", description);
    map.put("technologiesUsed", technologiesUsed);
    map.put("vcsUrl", vcsUrl);

    return map;
  }

}
