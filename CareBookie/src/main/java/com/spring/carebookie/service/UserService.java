package com.spring.carebookie.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.carebookie.common.constants.EmployeeStatus;
import com.spring.carebookie.common.mappers.UserMapper;
import com.spring.carebookie.dto.DoctorGetAllDto;
import com.spring.carebookie.dto.LoginRequest;
import com.spring.carebookie.dto.edit.DoctorUpdateInformationDto;
import com.spring.carebookie.dto.response.DoctorResponseDto;
import com.spring.carebookie.dto.response.EmployeeResponseDto;
import com.spring.carebookie.dto.save.AdministrativeSaveDto;
import com.spring.carebookie.dto.save.DoctorSaveDto;
import com.spring.carebookie.dto.save.EmployeeSaveDto;
import com.spring.carebookie.dto.save.UserSaveDto;
import com.spring.carebookie.entity.UserEntity;
import com.spring.carebookie.exception.ResourceNotFoundException;
import com.spring.carebookie.repository.UserRepository;
import com.spring.carebookie.repository.projection.DoctorGetAllProjection;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final CommonService commonService;

    private static final UserMapper USER_MAPPER = UserMapper.INSTANCE;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public List<UserEntity> getAllPatients() {
        return userRepository.findAllByHospitalIdIsNull();
    }

    public UserEntity getUserByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    /**
     * User
     */
    @Transactional
    public UserEntity save(UserSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);

        // Set some information into entity
        String userId = generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail());
        entity.setUserId(userId);
        entity.setRoleId(5L);
        entity.setDisable(false);
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));

        return userRepository.save(entity);

    }

    /**
     * Doctor
     */
    @Transactional
    public UserEntity saveDoctor(DoctorSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setDisable(false);
        entity.setRoleId(4L);
        entity.setDoctor(true);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStatus(EmployeeStatus.WORKING.toString());
        return userRepository.save(entity);
    }

    @Transactional
    public UserEntity updateDoctor(DoctorUpdateInformationDto dto) {
        userRepository.updateDoctor(dto);
        return Optional.of(userRepository.findByUserId(dto.getUserId()))
                .orElseThrow(() -> new ResourceNotFoundException("Doctor {} not found".replace("{}", dto.getUserId())));
    }

    public List<DoctorResponseDto> getDoctorByHospitalId(String hospitalId) {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllByHospitalId(hospitalId);
        List<DoctorResponseDto> dtos = entities
                .stream()
                .map(entity -> USER_MAPPER.convertEntityToDto(entity))
                .collect(Collectors.toList());
        for (int i = 0; i < entities.size(); i++) {
            dtos.get(i).setStar(stars.get(dtos.get(i).getUserId()) == null ? 0 : stars.get(dtos.get(i).getUserId()));
            if (dtos.get(i).getKnowledges() != null)
                dtos.get(i).setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
        }
        return dtos;
    }

    public DoctorResponseDto getDoctorByDoctorId(String doctorId) {
        return getAllDoctor().stream()
                .filter(d -> d.getUserId().equals(doctorId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Doctor {} not found".replace("{}", doctorId)));
    }

    public List<DoctorResponseDto> getAllDoctor() {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllByDoctorIsTrue();
        List<DoctorResponseDto> dtos = entities
                .stream()
                .map(entity -> USER_MAPPER.convertEntityToDto(entity))
                .collect(Collectors.toList());
        for (int i = 0; i < entities.size(); i++) {
            DoctorResponseDto dto = dtos.get(i);
            dto.setStar(stars.get(dto.getUserId()));
            if (entities.get(i).getKnowledge() != null) {
                dto.setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
            }
        }
        dtos.sort(Comparator.nullsLast(Comparator.comparing(DoctorResponseDto::getStar,
                Comparator.nullsFirst(Double::compareTo)).reversed()));

        return dtos;
    }


    /**
     * Administrative
     */
    @Transactional
    public UserEntity saveAdministrative(AdministrativeSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setDisable(false);
        entity.setRoleId(3L);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setStatus(EmployeeStatus.WORKING.toString());
        return userRepository.save(entity);
    }

    public List<EmployeeResponseDto> getAllEmployeeByHospitalId(String hospitalId) {
        Map<String, Double> stars = commonService.getDoctorStar();
        List<UserEntity> entities = userRepository.findAllEmployeesByHospitalId(hospitalId);
        List<EmployeeResponseDto> dtos = entities.stream()
                .map(entity -> USER_MAPPER.convertEntityToEDto(entity))
                .collect(Collectors.toList());

        for (int i = 0; i < entities.size(); i++) {
            EmployeeResponseDto dto = dtos.get(i);
            dto.setStar(stars.get(dto.getUserId()));
            if (entities.get(i).getKnowledge() != null) {
                dto.setKnowledges(Arrays.stream(entities.get(i).getKnowledge().split(",")).collect(Collectors.toList()));
            }
        }

        // Remove admin
        dtos.removeIf(dto -> dto.getRoleId() == 2);
        return dtos;
    }

    /**
     * Employee
     */
    public UserEntity saveEmployee(EmployeeSaveDto dto) {

        UserEntity entity = USER_MAPPER.convertSaveToEntity(dto);
        entity.setUserId(generateUserId(entity.getFirstName(), entity.getLastName(), entity.getEmail()));
        entity.setStatus(EmployeeStatus.WORKING.toString());
        entity.setPassword(passwordEncoder.encode(entity.getPassword()));
        entity.setDisable(false);
        entity.setRoleId(dto.isDoctor() ? 4L : 3L);
        return userRepository.save(entity);
    }

    /**
     * Login
     */

    public UserEntity login(LoginRequest loginRequest) {
        UserEntity entity = userRepository.findByPhone(loginRequest.getPhone());
        if (entity != null && passwordEncoder.matches(loginRequest.getPassword(), entity.getPassword())) {
            return entity;
        }

        throw new ResourceNotFoundException("User {} not found".replace("{}", loginRequest.getPhone()));
    }

    private List<DoctorGetAllDto> convertProjectionToDto(List<DoctorGetAllProjection> projections) {
        return projections
                .stream()
                .map(projection -> {
                    List<String> knowledges = Arrays.stream(projection.getKnowledge().split(","))
                            .collect(Collectors.toList());
                    DoctorGetAllDto dto = USER_MAPPER.convertProjectToDto(projection);
                    dto.setKnowledges(knowledges);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    /**
     * Private
     */

    /**
     * Generate userId <br>
     * Example: firstName = "Oanh", lastName = "Pham Van", email = "poanh1002@gmail.com" <br>
     * Result is "OPpoanh1002"
     *
     * @param firstName
     * @param lastName
     * @param email
     * @return String
     */
    private String generateUserId(String firstName, String lastName, String email) {
        return firstName.toCharArray()[0] + String.valueOf(lastName.toCharArray()[0]) + email.split("@")[0];

    }
}
