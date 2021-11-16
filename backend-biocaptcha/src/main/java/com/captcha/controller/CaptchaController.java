package com.captcha.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ServerWebExchange;

import com.captcha.ImageApi;
import com.captcha.V1Api;
import com.captcha.model.Captcha;
import com.captcha.model.ImageDescription;
import com.captcha.model.SettingsDTO;
import com.captcha.model.Validation;
import com.captcha.service.ImageService;
import com.captcha.service.ServiceCaptcha;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Class CaptchaController.
 */
@CrossOrigin(origins = "*")
@RestController
public class CaptchaController implements V1Api,ImageApi{

	/** The captcha service. */
	private ServiceCaptcha captchaService;
	
	/** The image service. */
	private ImageService imageService;

	/**
	 * Instantiates a new captcha controller.
	 *
	 * @param service the service
	 * @param image the image
	 */
	public CaptchaController(ServiceCaptcha service, ImageService image) {
		this.captchaService = service;
		this.imageService=image;
	}
	/**
	 * Recover captcha.
	 *
	 * @param exchange the exchange
	 * @return the mono
	 */
	@Override
	public Mono<ResponseEntity<Captcha>> recoverCaptcha(ServerWebExchange exchange) {
		return captchaService.generateCaptcha()
				.onErrorResume(e -> Mono.error(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
				.<ResponseEntity<Captcha>>map(data -> new ResponseEntity<Captcha>(data, HttpStatus.OK));
	}

	/**
	 * Validate captcha.
	 *
	 * @param validCaptcha the valid captcha
	 * @param exchange the exchange
	 * @return the mono
	 */
	@Override
	public Mono<ResponseEntity<Validation>> validateCaptcha(@Valid Mono<Captcha> validCaptcha,
			ServerWebExchange exchange) {
		return captchaService.validate(validCaptcha)
				.onErrorResume(e -> Mono.error(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
				.<ResponseEntity<Validation>>map(data -> new ResponseEntity<Validation>(data, HttpStatus.OK));
	}

	/**
	 * Save settings.
	 *
	 * @param settingsDTO the settings DTO
	 * @param exchange the exchange
	 * @return the mono
	 */
	@Override
	public Mono<ResponseEntity<SettingsDTO>> saveSettings(@Valid Mono<SettingsDTO> settingsDTO,
			ServerWebExchange exchange) {
		return captchaService.updateSettings(settingsDTO)
				.onErrorResume(e -> Mono.error(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR)))
				.<ResponseEntity<SettingsDTO>>map(data -> new ResponseEntity<SettingsDTO>(data, HttpStatus.OK));
	}

	/**
	 * Gets the settings.
	 *
	 * @param exchange the exchange
	 * @return the settings
	 */
	@Override
	public Mono<ResponseEntity<SettingsDTO>> getSettings(ServerWebExchange exchange) { 
		return captchaService.getSettings().map(data -> new ResponseEntity<SettingsDTO>(data, HttpStatus.OK));
	}
	
	/**
	 * Image description.
	 *
	 * @param url the url
	 * @param exchange the exchange
	 * @return the mono
	 */
	@Override
	public Mono<ResponseEntity<Flux<ImageDescription>>> imageDescription(@Valid List<String> url,
			ServerWebExchange exchange) {
		return Mono.just(new ResponseEntity<Flux<ImageDescription>>(imageService.getImageDescription(url),HttpStatus.OK));
	}




}
