package nl.jemaja.weekmenu.service;

import lombok.extern.slf4j.Slf4j;
import nl.jemaja.weekmenu.model.Settings;
import nl.jemaja.weekmenu.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SettingsService {

    @Autowired
    SettingsRepository sRepo;

    public Settings getSettings() {
        return sRepo.findAll().stream()
                .findFirst()
                .orElseGet(this::createDefaultSettings);
    }

    private Settings createDefaultSettings() {
        Settings defaults = new Settings();
        defaults.setHealthWeight(0.2);
        defaults.setPreferenceWeight(0.3);
        defaults.setRecencyWeight(0.25);
        defaults.setVariatyWeight(0.1);
        defaults.setQOneWeight(1.0);
        defaults.setQTwoWeight(0.5);
        defaults.setQThreeWeight(0.25);
        defaults.setQFourWeight(0.1);
        defaults.setOneWeekPenalty(-0.5);
        defaults.setTwoWeekPenalty(-0.3);
        defaults.setThreeWeekPenalty(-0.05);
        // ... set other defaults
        return sRepo.save(defaults);
    }
}
