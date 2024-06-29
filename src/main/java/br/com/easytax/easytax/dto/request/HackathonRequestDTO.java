package br.com.easytax.easytax.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HackathonRequestDTO {

    private String cpf;

    @JsonProperty("app_name")
    private String appName;

    private Integer valor;
}
