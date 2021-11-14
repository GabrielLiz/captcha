package com.captcha.mapper;

import org.springframework.beans.BeanUtils;

import com.captcha.entity.Settings;
import com.captcha.model.SettingsDTO;
import com.captcha.model.SettingsDTO.ModeEnum;

public class SettingMapper {

	public static SettingsDTO toSettingsDTO(Settings settings) {
		SettingsDTO settingDTO = new SettingsDTO();
		settingDTO.setMode(ModeEnum.valueOf(settings.getMode()));
		BeanUtils.copyProperties(settings, settingDTO);
		return settingDTO;
	}
	
	public static Settings toSettings(SettingsDTO DTO) {
		Settings settings = new Settings();
		settings.setMode(DTO.getMode().toString());
		BeanUtils.copyProperties(DTO, settings);
		return settings;
	}
		
}
