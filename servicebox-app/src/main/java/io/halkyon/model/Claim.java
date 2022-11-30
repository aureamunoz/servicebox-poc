package io.halkyon.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.halkyon.services.ClaimStatus;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import org.jboss.resteasy.reactive.MultipartForm;
import org.jboss.resteasy.reactive.RestForm;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@MultipartForm
public class Claim extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @NotBlank(message = "Name must not be empty")
    @RestForm
	public String name;
    @NotBlank(message = "Service Requested must not be empty")
    @RestForm
    public String serviceRequested;
    @RestForm
    public String description;
    @RestForm
    public String status;
    @RestForm
    public String owner;
    @RestForm
    public Date created;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "service_id", referencedColumnName = "id")
    public Service service;
    public Integer attempts = 0;

    @JsonManagedReference
    @OneToOne
    public Credential credential;
    public String url;

    public static Claim findByName(String name) {
        return find("name", name).firstResult();
    }

    public static List<Claim> listAll() {
        return findAll(Sort.ascending("name")).list();
    }

    public static List<Claim> listAvailable() {
        return find("status=:status", Collections.singletonMap("status", ClaimStatus.BIND.toString())).list();
    }

    public static List<Claim> getClaims(String name, String serviceRequested) {
        Map<String, Object> parameters = new HashMap<>();
        addIfNotNull(parameters, "name", name );
        addIfNotNull(parameters, "servicerequested", serviceRequested);

        if ( parameters.isEmpty() ) {
            return listAll();
        }

        String query = parameters.entrySet().stream()
                .map( entry -> "LOWER(" + entry.getKey() + ") " + "like :" + entry.getKey() )
                .collect( Collectors.joining(" AND ") );

        // TODO: To be reviewed in order to generate the query with multiple parameters where we could use like or not, etc
        // String query = "name like ?1 or servicerequested = ?2";
        return Claim.list(query, parameters);
    }

    private static void addIfNotNull(Map<String, Object> map, String key, String value) {
        if (value != null && !value.isEmpty()) {
            map.put(key, "%" + value.toLowerCase(Locale.ROOT) + "%");
        }
    }
}
