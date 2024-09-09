package org.example.task_manager.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.example.task_manager.models.body.Authorization;
import org.example.task_manager.service.KeyCloakService;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RoleMappingResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeyCloakServiceImpl implements KeyCloakService {
    private final Keycloak keycloak;

    @Value("${app.keycloak.realm}")
    private String realm;

    @Override
    public void createUser(Authorization authorization) {
        String username = authorization.getUsername();

        CredentialRepresentation credential = createCredentials(authorization.getPassword());

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(authorization.getUsername());
        userRepresentation.setEmail(authorization.getEmail());
        userRepresentation.setEnabled(true);
        userRepresentation.setCredentials(List.of(credential));

        UsersResource userResource = keycloak.realm(realm).users();

        try (Response response = userResource.create(userRepresentation)) {
            addRealmToRole(username, authorization.getRole());
        }
    }

    private void addRealmToRole(String username, String roleName) {
        RealmResource realmResource = keycloak.realm(realm);

        List<UserRepresentation> userRepresentations = realmResource.users().search(username);

        UserResource userResource = realmResource.users().get(userRepresentations.get(0).getId());
        RoleRepresentation roleRepresentation = realmResource.roles().get(roleName).toRepresentation();
        RoleMappingResource roleMappingResource = userResource.roles();
        roleMappingResource.realmLevel().add(List.of(roleRepresentation));
    }

    private CredentialRepresentation createCredentials(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setValue(password);

        return credentialRepresentation;
    }
}
