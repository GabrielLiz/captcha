package com.captcha.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.captcha.model.ImageDescription;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The Class ImageServiceImpl.
 */
@Service
public class ImageServiceImpl implements ImageService {

	/** The Constant FACE_PARAMETER. */
	private final static String FACE_PARAMETER = "/analyze?language=es&descriptionExclude=Celebrities&visualFeatures=Faces&details=Celebrities";
	
	/** The Constant DESCRIPTION. */
	private final static String DESCRIPTION = "/analyze?language=es&descriptionExclude=Celebrities&visualFeatures=Description&details=Celebrities";

	/** The client. */
	private WebClient client;
	
	/**
	 * Instantiates a new image service impl.
	 */
	public ImageServiceImpl() {
		client = WebClient.builder().baseUrl("https://microsoft-computer-vision3.p.rapidapi.com")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("x-rapidapi-host", "microsoft-computer-vision3.p.rapidapi.com")
				.defaultHeader("x-rapidapi-key", "###").build();
	}

	/**
	 * Request.
	 *
	 * @param url the url
	 * @return the mono
	 */
	private Mono<ImageDescription> request(String url) {
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

	/**
	 * Gets the image description.
	 *
	 * @param urls the urls
	 * @return the image description
	 */
	@Override
	public Flux<ImageDescription> getImageDescription(List<String> urls) {
		return Flux.fromIterable(urls).flatMap(this::request);
	}

}
