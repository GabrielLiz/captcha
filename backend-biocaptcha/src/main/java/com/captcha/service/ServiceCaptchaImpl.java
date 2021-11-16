package com.captcha.service;

import java.util.Random;

import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import com.captcha.entity.Settings;
import com.captcha.mapper.SettingMapper;
import com.captcha.model.Captcha;
import com.captcha.model.SettingsDTO;
import com.captcha.model.SettingsDTO.ModeEnum;
import com.captcha.model.Validation;

import reactor.core.publisher.Mono;

/**
 * The Class ServiceCaptchaImpl.
 */
@Service
public class ServiceCaptchaImpl implements ServiceCaptcha {

    /** The template. */
    private ReactiveMongoTemplate template;
    
	/**
	 * Instantiates a new service captcha impl.
	 *
	 * @param tempplate the tempplate
	 */
	public ServiceCaptchaImpl(ReactiveMongoTemplate tempplate) {
		this.template=tempplate;
	}	

	/**
	 * Generate captcha.
	 *
	 * @return the mono
	 */
	@Override
	public Mono<Captcha> generateCaptcha() {
		return template.findById("setting",Settings.class).map(data -> {
			String code = modeValues(ModeEnum.valueOf(data.getMode()));
			StringBuffer captchaBuffer = new StringBuffer();
			Random random = new Random();
			while (captchaBuffer.length() < data.getCaptchalength()) {
				int index = (int) (random.nextFloat() * code.length());
				captchaBuffer.append(code.substring(index, index + 1));
			}
			return  new Captcha().value(captchaBuffer.toString());
		});

	}

	/**
	 * Validate.
	 *
	 * @param captcha the captcha
	 * @return the mono
	 */
	@Override
	public Mono<Validation> validate(Mono<Captcha> captcha) {
		return Mono.zip(template.findById("setting",Settings.class), captcha , (a, b)-> {
			if(a.isCaseeSensitive()) {
				 return new Validation().correct(b.getValue().equals(b.getCaptchaValue()));
			}else {
				 return new Validation().correct(b.getValue().toLowerCase().equals(b.getCaptchaValue().toLowerCase()));
			}
	
		});
		
	}

	/**
	 * Gets the settings.
	 *
	 * @return the settings
	 */
	@Override
	public Mono<SettingsDTO> getSettings() {
	 return template.findById("setting",Settings.class).map(data -> SettingMapper.toSettingsDTO(data));
	}

	/**
	 * Update settings.
	 *
	 * @param setting the setting
	 * @return the mono
	 */
	@Override
	public Mono<SettingsDTO> updateSettings(Mono<SettingsDTO> setting) {
		return template.save(setting.map(SettingMapper::toSettings)).map(SettingMapper::toSettingsDTO);
	}
	
	/**
	 * Mode values.
	 *
	 * @param mode the mode
	 * @return the string
	 */
	private String modeValues(ModeEnum mode) {
		String Alpha = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String num = "1234567890";

		switch (mode) {
		case ALPHANUMERIC:
			return Alpha + num;
		case ALPHA:
			return Alpha;
		case NUM:
			return num;
		}
		return Alpha + num;
	}

}
