package nl.jemaja.weekmenu.repository;

import nl.jemaja.weekmenu.model.Settings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    // That's it! JpaRepository gives you findAll(), save(), etc.
}
