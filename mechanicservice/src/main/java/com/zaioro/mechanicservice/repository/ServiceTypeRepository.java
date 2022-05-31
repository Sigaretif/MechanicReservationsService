package com.zaioro.mechanicservice.repository;

import com.zaioro.mechanicservice.model.serviceType.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, Integer> {
}
