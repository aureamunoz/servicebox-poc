package io.halkyon.resource.requests;

import java.io.InputStream;

import javax.validation.constraints.NotBlank;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;

public class NewClusterRequest {
    @NotBlank
    @RestForm
    public String name;

    @NotBlank
    @RestForm
    public String url;

    @NotBlank
    @RestForm
    public String environment;

    @RestForm
    @PartType(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream kubeConfig;
}
