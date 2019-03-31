package com.wagner.mycv.service.impl;

import com.wagner.mycv.model.entity.Certification;
import com.wagner.mycv.model.repository.CertificationRepository;
import com.wagner.mycv.service.CertificationService;
import com.wagner.mycv.web.dto.request.CertificationRequestDto;
import com.wagner.mycv.web.dto.CertificationDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificationServiceImpl implements CertificationService {

  private final CertificationRepository certificationRepository;
  private final ModelMapper             modelMapper;

  @Autowired
  public CertificationServiceImpl(CertificationRepository certificationRepository) {
    this.certificationRepository = certificationRepository;
    this.modelMapper             = new ModelMapper();
  }

  @Override
  public List<CertificationDto> findAll() {
    Sort sort = new Sort(Sort.Direction.DESC, "dateOfAchievement");

    return certificationRepository.findAll(sort)
            .stream()
            .map(certification -> modelMapper.map(certification, CertificationDto.class))
            .collect(Collectors.toList());
  }

  @Override
  public Optional<CertificationDto> find(long id) {
    Optional<Certification> certification = certificationRepository.findById(id);

    return certification.map(value -> modelMapper.map(value, CertificationDto.class));
  }

  @Override
  public CertificationDto create(CertificationRequestDto request) {
    Certification certification = modelMapper.map(request, Certification.class);
    certificationRepository.save(certification);

    return modelMapper.map(certification, CertificationDto.class);
  }

  @Override
  public List<CertificationDto> createAll(Iterable<CertificationRequestDto> request) {
    List<Certification> certifications = new ArrayList<>();
    request.forEach(certificationRequestDto -> certifications.add(modelMapper.map(certificationRequestDto, Certification.class)));

    certificationRepository.saveAll(certifications);

    return certifications.stream()
            .map(certification -> modelMapper.map(certification, CertificationDto.class))
            .collect(Collectors.toList());
  }

  @Override
  public Optional<CertificationDto> update(long id, CertificationRequestDto request) {
    Optional<Certification> certificationOptional = certificationRepository.findById(id);
    CertificationDto certificationResponse        = null;

    if (certificationOptional.isPresent()) {
      Certification certification = certificationOptional.get();
      modelMapper.map(request, certification);

      certificationRepository.save(certification);
      certificationResponse = modelMapper.map(certification, CertificationDto.class);
    }

    return Optional.ofNullable(certificationResponse);
  }

  @Override
  public boolean delete(long id) {
    if (certificationRepository.existsById(id)) {
      certificationRepository.deleteById(id);
      return true;
    }

    // entity doesn't exist
    return false;
  }
}
