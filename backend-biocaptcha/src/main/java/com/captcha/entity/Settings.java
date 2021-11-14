package com.captcha.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.captcha.model.SettingsDTO.ModeEnum;

import lombok.Getter;
import lombok.Setter;

@Document("settings_document")
@Getter
@Setter
public class Settings {
	
    @Id
    private String id="setting";
    private Integer captchalength;
    private Integer attempts;
    private boolean caseeSensitive;
    private String mode;
}
