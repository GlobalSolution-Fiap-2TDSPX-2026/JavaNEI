package br.com.fiap.global_solution.services;

import br.com.fiap.global_solution.enums.AlertLevel;
import br.com.fiap.global_solution.models.RiskAssessment;
import br.com.fiap.global_solution.models.RiskZone;
import br.com.fiap.global_solution.repositories.RiskZoneRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class RiskZoneService {

    private final RiskZoneRepository riskZoneRepository;

    public RiskZoneService(RiskZoneRepository riskZoneRepository) {
        this.riskZoneRepository = riskZoneRepository;
    }

    public Optional<RiskZone> findById(Long id) {
        return riskZoneRepository.findById(id);
    }

    @Cacheable(value = "riskZone")
    public Page<RiskZone> getRiskZones(Pageable pageable) {
        return riskZoneRepository.findAll(pageable);
    }

    @Cacheable(value = "alertLevel")
    public Page<RiskZone> getZoneByAlertLevel(AlertLevel alertLevel, Pageable pageable) {
        return riskZoneRepository.findByAlertLevel(alertLevel, pageable);
    }



    @Cacheable(value = "regionName")
    public Page<RiskZone> getZoneByRegionName(String region, Pageable pageable) {
        return riskZoneRepository.findByRegionNameContainingIgnoreCase(region, pageable);
    }

    @CacheEvict(value = {"riskZone", "alertLevel", "regionName"}, allEntries = true)
    public RiskZone addRiskZone(RiskZone riskZone) {
        return riskZoneRepository.save(riskZone);
    }

    @CacheEvict(value = {"riskZone", "alertLevel", "regionName"}, allEntries = true)
    public void deleteRiskZone(Long id) {
        var optionalRiskZone = riskZoneRepository.findById(id);
        if (optionalRiskZone.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskZone not found");
        }
        riskZoneRepository.deleteById(id);
    }

    @CacheEvict(value = {"riskZone", "alertLevel", "regionName"}, allEntries = true)
    public RiskZone updateRiskZone(Long id, RiskZone newRiskZone) {
        var optionalRiskZone = riskZoneRepository.findById(id);
        if (optionalRiskZone.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "RiskZone not found");
        }
        newRiskZone.setId(id);
        return riskZoneRepository.save(newRiskZone);
    }
}