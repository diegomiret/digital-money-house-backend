package com.dh.digitalMoneyHouse.usersservice.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

public class KeycloakJwtAuthConverter implements Converter <Jwt, Collection<GrantedAuthority>>{

    private static final String ROLES_CLAIM = "roles";
    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String USERS_CLAIM = "users";
    private static final String SCOPES_CLAIM = "scope";
    private static final String GROUPS_CLAIM = "group";


    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // ROLES
        Map<String, Map<String, List<String>>> resourcesAccess =
                (Map<String, Map<String, List<String>>>) source.getClaims().get(RESOURCE_ACCESS_CLAIM);
        if (resourcesAccess != null && !resourcesAccess.isEmpty()) {
            Map<String, List<String>> clientAccessRoles = resourcesAccess.get(USERS_CLAIM);
            authorities.addAll(clientAccessRoles != null ? extractRoles(clientAccessRoles) : List.of());
        }

        // SCOPES
        String scopes = (String) source.getClaims().get(SCOPES_CLAIM);
        if (scopes != null) {
            authorities.addAll(extractScopes(scopes));
        }

        // GROUPS
        List<String> groups = (List<String>) source.getClaims().get(GROUPS_CLAIM);
        if (groups != null && !groups.isEmpty()) {
            authorities.addAll(extractGroups(groups));
        }

        return authorities;
    }

    private static Collection<GrantedAuthority> extractRoles(Map<String, List<String>> roles) {
        return  roles.get(ROLES_CLAIM)
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }

    private static Collection<GrantedAuthority> extractScopes(String scopes) {
        return Arrays.stream(scopes.split(" "))
                .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                .collect(Collectors.toList());
    }

    private static Collection<GrantedAuthority> extractGroups(List<String> groups) {
        return groups.stream()
                .map(groupName -> groupName.substring(1))
                .map(groupName -> "GROUP_" + groupName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
