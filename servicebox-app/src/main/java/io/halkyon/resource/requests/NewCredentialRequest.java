package io.halkyon.resource.requests;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.jboss.resteasy.reactive.RestForm;

public class NewCredentialRequest {

    @RestForm
    public Long id;

    @NotBlank
    @RestForm
    public String name;

    @NotNull
    @RestForm
    public Long serviceId;

    @NotBlank
    @RestForm
    public String username;

    @NotBlank
    @RestForm
    public String password;

    @RestForm
    public List<String> params;
}
