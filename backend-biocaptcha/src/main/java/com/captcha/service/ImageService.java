package com.captcha.service;

import java.util.List;

import com.captcha.model.ImageDescription;

import reactor.core.publisher.Flux;

public interface ImageService {
	
	
	public Flux<ImageDescription> getImageDescription(List<String>urls);
}
