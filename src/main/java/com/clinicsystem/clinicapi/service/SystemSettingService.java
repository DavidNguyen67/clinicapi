package com.clinicsystem.clinicapi.service;

import com.clinicsystem.clinicapi.model.SystemSettingDocument;
import com.clinicsystem.clinicapi.repository.SystemSettingMongoRepository;
import com.clinicsystem.clinicapi.util.NullSafetyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SystemSettingService {
    private final SystemSettingMongoRepository systemSettingRepository;

    public String getSettingValue(String settingKey, String defaultValue) {
        if (NullSafetyUtil.isNullOrEmpty(settingKey)) {
            log.warn("Setting key is null or empty, returning default value");
            return defaultValue;
        }
        return systemSettingRepository.findBySettingKey(settingKey)
                .map(SystemSettingDocument::getSettingValue)
                .orElse(defaultValue);
    }

    public String getSettingValue(String settingKey) {
        return getSettingValue(settingKey, null);
    }

    public boolean settingExists(String settingKey) {
        if (NullSafetyUtil.isNullOrEmpty(settingKey)) {
            return false;
        }
        return systemSettingRepository.findBySettingKey(settingKey).isPresent();
    }

    public SystemSettingDocument saveSetting(String settingKey, String settingValue, String description,
            Boolean isPublic) {
        NullSafetyUtil.requireNonEmpty(settingKey, "Setting key");

        SystemSettingDocument setting = systemSettingRepository.findBySettingKey(settingKey)
                .orElse(new SystemSettingDocument());

        setting.setSettingKey(settingKey);
        setting.setSettingValue(NullSafetyUtil.getOrEmpty(settingValue));
        setting.setDescription(description);
        setting.setIsPublic(NullSafetyUtil.getOrDefault(isPublic, false));

        return systemSettingRepository.save(setting);
    }
}
