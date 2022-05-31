package com.zaioro.mechanicservice.model.serviceType;

import com.zaioro.mechanicservice.repository.ServiceTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceTypePopulateRunner implements CommandLineRunner {

    private final ServiceTypeRepository serviceTypeRepository;

    @Override
    public void run(String... args) throws Exception {
        List<ServiceType> serviceTypeList = serviceTypeRepository.findAll();
        if(serviceTypeList.isEmpty()){
            serviceTypeRepository.saveAll(serviceTypeInitialization());
        }
        log.info("Service types have been initialized");
    }

    private List<ServiceType> serviceTypeInitialization(){
        List<String> serviceTypeNamesList = List.of("REPAIR", "REVIEW", "BODYWORK", "DETAILING");
        List<ServiceType> serviceTypeList = new ArrayList<>();
        for(String serviceTypeName: serviceTypeNamesList){
            serviceTypeList.add(ServiceType.builder()
                    .name(serviceTypeName)
                    .build());
        }
        return serviceTypeList;
    }
}
