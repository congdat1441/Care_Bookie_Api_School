package com.spring.carebookie.service;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.mappers.HospitalMapper;
import com.spring.carebookie.dto.HospitalGetAllDto;
import com.spring.carebookie.dto.response.HospitalResponseDto;
import com.spring.carebookie.dto.save.HospitalSaveDto;
import com.spring.carebookie.entity.HospitalEntity;
import com.spring.carebookie.repository.HospitalRepository;
import com.spring.carebookie.repository.ServiceRepository;
import com.spring.carebookie.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    private final CommonService commonService;

    private final UserRepository userRepository;

    private static final HospitalMapper HOSPITAL_MAPPER = HospitalMapper.INSTANCE;

    public List<HospitalGetAllDto> getAllHospital() {

        return hospitalRepository.getAllHospital()
                .stream()
                .map(p -> new HospitalGetAllDto(p, hospitalRepository.getWardsByHospitalId(p.getHospitalId()),
                        hospitalRepository.getAllServiceByHospitalId(p.getHospitalId())))
                .collect(Collectors.toList());
    }

    public List<HospitalResponseDto> getAllHospitals() {
        Map<String, Double> stars = commonService.getHospitalStar();
        List<HospitalEntity> entities = hospitalRepository.findAll();
        List<HospitalResponseDto> dtos = HOSPITAL_MAPPER.convertEntitiesToDtos(entities);
        dtos.forEach(dto -> {
            dto.setStar(stars.get(dto.getHospitalId()) == null ? 0 : stars.get(dto.getHospitalId()));
            dto.setServices(commonService.getAllServiceByHospitalId(dto.getHospitalId()));
            dto.setWorkingDayDetails(commonService.getAllWorkingDayDetailByHospitalId(dto.getHospitalId()));
            dto.setAdminInformation(userRepository.findByUserId(dto.getAdminId()));
        });
        return dtos;
    }

    public HospitalResponseDto getHospitalByHospitalId(String hospitalId) {
        return getAllHospitals().stream()
                .filter(dto -> dto.getHospitalId().equals(hospitalId))
                .findFirst().get();
    }

    @Transactional
    public HospitalEntity saveHospital(HospitalSaveDto dto) {

        HospitalEntity entity = HOSPITAL_MAPPER.convertSaveDtoToEntity(dto);

        entity.setHospitalId(generateHospitalId(entity.getHospitalName()));
        entity.setStatus(false);

        log.info("Finished save {} hospital into database", entity.getHospitalName());
        return hospitalRepository.save(entity);

    }

    private String generateHospitalId(String hospitalName) {
        String[] arr = hospitalName.split(" ");
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (String a : arr) {
            builder.append(a.toCharArray()[0]);
        }
        System.out.println(builder.append(random.nextInt(10)).append(random.nextInt(10))
                .append(random.nextInt(10)).append(random.nextInt(10)));
        return builder.toString();
    }
}
