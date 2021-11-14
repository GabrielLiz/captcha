package com.captcha.service;

import com.captcha.model.Captcha;
import com.captcha.model.SettingsDTO;
import com.captcha.model.Validation;

import reactor.core.publisher.Mono;

/**
 * The Interface ServiceCaptcha.
 */
public interface ServiceCaptcha {

	/**
	 * Generate captcha.
	 *
	 * @return the mono
	 */
	public Mono<Captcha> generateCaptcha();
	
	/**
	 * Validate.
	 *
	 * @param captcha the captcha
	 * @return the mono
	 */
	public Mono<Validation> validate(Mono<Captcha> captcha);
	
	/**
	 * Gets the settings.
	 *
	 * @return the settings
	 */
	public Mono<SettingsDTO> getSettings();
	
	/**
	 * Update settings.
	 *
	 * @param setting the setting
	 * @return the mono
	 */
	public Mono<SettingsDTO> updateSettings(Mono<SettingsDTO> setting);
}
