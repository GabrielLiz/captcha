package com.captcha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.captcha.model.ImageDescription;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ImageServiceImpl implements ImageService {

	private final static String FACE_PARAMETER = "/analyze?language=es&descriptionExclude=Celebrities&visualFeatures=Faces&details=Celebrities";
	private final static String DESCRIPTION = "/analyze?language=es&descriptionExclude=Celebrities&visualFeatures=Description&details=Celebrities";

	WebClient client;

	public ImageServiceImpl() {
		client = WebClient.builder().baseUrl("https://microsoft-computer-vision3.p.rapidapi.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("x-rapidapi-host", "microsoft-computer-vision3.p.rapidapi.com")
				.defaultHeader("x-rapidapi-key", "0518310d8bmshcbe780f085740dep16d246jsn87ef6922955c").build();
	}

	public Mono<ImageDescription> request(String url) {
		Map<String, String> bodyValues = new HashMap<String, String>();
		bodyValues.put("url", url);

		return client.post()
				.uri(FACE_PARAMETER)
				.bodyValue(bodyValues)
				.retrieve()
				.onStatus(HttpStatus::isError, clientResponse -> {
					return Mono.error(new Exception("error"));
				})
				.bodyToMono(ImageDescription.class)
				.flatMap(face -> {
					if (face.getFaces().size() != 0) {
						return client.post()
								.uri(DESCRIPTION)
								.bodyValue(bodyValues)
								.retrieve()
								.onStatus(HttpStatus::isError, clientResponse -> {
									return Mono.error(new Exception("error"));
								})
								.bodyToMono(ImageDescription.class)
								.doOnNext(des -> des.faces(face.getFaces()));
					}
					return Mono.just(new ImageDescription());
				});
	}

	@Override
	public Flux<ImageDescription> getImageDescription(List<String> urls) {
		return Flux.fromIterable(urls).flatMap(this::request);
	}

}
