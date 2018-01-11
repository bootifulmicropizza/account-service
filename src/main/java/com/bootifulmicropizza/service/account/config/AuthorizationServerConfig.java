package com.bootifulmicropizza.service.account.config;

import com.bootifulmicropizza.service.account.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("accountUserDetailsService")
    private UserDetailsService userDetailsService;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new CustomerTokenEnhancer();
        KeyStoreKeyFactory keyStoreKeyFactory =
            new KeyStoreKeyFactory(new ClassPathResource("auth-server.jks"), "bootifulmicropizza".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("pizza");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
            // Client for the website-app via the website-gateway
//            .withClient("web-client")
//            .authorizedGrantTypes("password")
//            .secret("web-client-secret")
//            // Roles permitted by this client
////            .authorities("")
//            // Allowed actions
//            .scopes("READ_CATALOGUE", "READ_PRODUCTS", "READ_ACCOUNT", "WRITE_ACCOUNT", "REGISTER", "CREATE_ORDER")
//            .and()
//
//            .withClient("trusted-client")
//            .authorizedGrantTypes("authorization_code", "password", "refresh_token")
//            .secret("secret")
//            .authorities("ROLE_CLIENT", "ROLE_TRUSTED_CLIENT")
//            .scopes("read", "write")
//            .and()

            .withClient("website-client")
            .autoApprove(true)
            .authorizedGrantTypes("password", "client_credentials")
            .secret("website-client-secret")
            .authorities("INVENTORY_READ", "ACCOUNT_READ", "ACCOUNT_WRITE")
            .scopes("website-api-gateway", "inventory-service", "account-service")
            .and()

            .withClient("admin-client")
            .autoApprove(true)
            .authorizedGrantTypes("password", "client_credentials")
            .secret("admin-client-secret")
            .authorities("INVENTORY_READ", "INVENTORY_WRITE", "ACCOUNT_READ", "ACCOUNT_WRITE")
            .scopes("admin-api-gateway", "inventory-service", "account-service")
            .and()

            .withClient("api-client")
            .autoApprove(true)
            .authorizedGrantTypes("password", "client_credentials")
            .secret("api-client-secret")
            .authorities("public-api-gateway", "INVENTORY_READ", "ACCOUNT_READ", "ACCOUNT_WRITE")
               // authorities would come from the database for the given user of the API using an API key
            .scopes("inventory-service", "account-service");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
            .tokenStore(tokenStore())
            .authenticationManager(authenticationManager)
            .userDetailsService(userDetailsService)
            .accessTokenConverter(accessTokenConverter());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()");
    }

    private class CustomerTokenEnhancer extends JwtAccessTokenConverter {

        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            final Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());

            final Object principalObj = authentication.getPrincipal();
            if (principalObj instanceof User) {
                User user = (User) principalObj;
                info.put("account_id", user.getAccountId());
            }

            DefaultOAuth2AccessToken customAccessToken = new DefaultOAuth2AccessToken(accessToken);
            customAccessToken.setAdditionalInformation(info);
            return super.enhance(customAccessToken, authentication);
        }

        @Override
        public OAuth2Authentication extractAuthentication(Map<String, ?> claims) {
            OAuth2Authentication authentication = super.extractAuthentication(claims);
            authentication.setDetails(claims);
            return authentication;
        }
    }
}