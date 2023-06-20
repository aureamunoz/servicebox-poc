package io.halkyon.services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import org.jboss.logging.Logger;

import io.halkyon.model.Credential;
import io.halkyon.model.CredentialParameter;

@ApplicationScoped
public class CredentialService {

    private static final Logger LOG = Logger.getLogger(CredentialService.class);

    @Transactional
    public void doSave(Credential credential) {
        if (credential.id != null) {
            Credential edited = credential;
            credential = Credential.findById(credential.id);
            if (credential == null) {
                throw new NotFoundException(String.format("Credential not found for id: %d%n", credential.id));
            }
            credential = mergeEntities(credential, edited);

        }
        credential.persist();
    }

    private Credential mergeEntities(Credential old, Credential edited) {
        old.name = !old.name.equals(edited.name) ? edited.name : old.name;
        old.username = !old.username.equals(edited.username) ? edited.username : old.username;
        old.password = !old.password.equals(edited.password) ? edited.password : old.password;
        old.vaultKvPath = old.vaultKvPath != null && !old.vaultKvPath.equals(edited.vaultKvPath) ? edited.vaultKvPath
                : old.vaultKvPath;
        old.service = old.service != null && !old.service.equals(edited.service) ? edited.service : old.service;

        old.params.clear();
        if (edited.params != null) {
            for (CredentialParameter cp : edited.params) {
                CredentialParameter paramEntity = new CredentialParameter();
                paramEntity.credential = cp.credential;
                paramEntity.paramName = cp.paramName;
                paramEntity.paramValue = cp.paramValue;
                old.params.add(paramEntity);
            }
        }

        return old;
    }

}
