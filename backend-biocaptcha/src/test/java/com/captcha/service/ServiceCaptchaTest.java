package com.captcha.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.captcha.entity.Settings;
import com.captcha.mapper.SettingMapper;
import com.captcha.model.Captcha;
import com.captcha.model.SettingsDTO;
import com.captcha.model.SettingsDTO.ModeEnum;
import com.captcha.model.Validation;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(SpringExtension.class)
public class ServiceCaptchaTest {
    private Settings setMock;
    @Mock
    private ReactiveMongoTemplate template;
    @InjectMocks
    private  ServiceCaptchaImpl services;
    
    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        setMock= new Settings();
    	setMock.setAttempts(4);
    	setMock.setCaptchalength(4);
    	setMock.setCaseeSensitive(true);
    	setMock.setMode(ModeEnum.ALPHANUMERIC.toString());
    		
    }
    
    @Test
    public void generateCaptchaTest() {
        when(template.findById("setting",Settings.class)).thenReturn(Mono.just(setMock));
        Mono<Captcha> captcha = services.generateCaptcha();
        StepVerifier.create(captcha).expectNextMatches(data -> data.getValue().length()==4 ).verifyComplete();
    }
    
    @Test
    public void getSettingsTest(){
        
        when(template.findById("setting",Settings.class)).thenReturn(Mono.just(setMock));
        Mono<SettingsDTO> settingsDTO = services.getSettings();
        StepVerifier.create(settingsDTO).expectNextMatches(data -> data.getMode().equals(ModeEnum.ALPHANUMERIC)).verifyComplete();
        
    }
    
    @Test
    public void getUpdateSettingsTest(){
        
        when(template.save(Mockito.any())).thenReturn(Mono.just(setMock));
        Mono<SettingsDTO> update = services.updateSettings(Mono.just(SettingMapper.toSettingsDTO(setMock)));
        StepVerifier.create(update).expectNextMatches(data -> data.getMode().equals(ModeEnum.ALPHANUMERIC)).verifyComplete();
        
    }
    
    @Test
    public void validateCaptchaTest() {
        when(template.findById("setting",Settings.class)).thenReturn(Mono.just(setMock));
        Mono<Validation> validation = services.validate(Mono.just(new Captcha().captchaValue("capt2").value("capt2")));
        StepVerifier.create(validation).expectNextMatches(data -> data.getCorrect() ).verifyComplete();
    }
    
    

}
