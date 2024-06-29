package br.com.easytax.easytax.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HackathonResponseDTO {

    @JsonProperty("_id")
    private String id;

    private String cpf;

    @JsonProperty("app_name")
    private String appName;

    private Integer valor;

    @JsonProperty("__v")
    private Integer version;
}
