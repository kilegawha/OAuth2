package com.cos.security1.config.oauth.provider;

public interface OAuth2UserInfo {
	String getProvideerId();
	String getProvider();
	String getEmail();
	String getName();
}
