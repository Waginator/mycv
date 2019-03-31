package com.wagner.mycv.web.controller;

import com.wagner.mycv.model.exception.RestRequestValidationException;
import com.wagner.mycv.service.CertificationService;
import com.wagner.mycv.testutil.StubFactory;
import com.wagner.mycv.web.dto.CertificationDto;
import com.wagner.mycv.web.dto.request.CertificationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class CertificationRestControllerTest {

  @Mock
  private CertificationService certificationService;

  @Mock
  private BindingResult bindingResult;

  @InjectMocks
  private CertificationRestController certificationRestController;

  private CertificationDto ocaCertificationDto;
  private CertificationDto ocpCertificationDto;
  private CertificationRequestDto certificationRequestDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    ocaCertificationDto     = StubFactory.testCertificationDto("OCA Certification","2017-07-01");
    ocpCertificationDto     = StubFactory.testCertificationDto("OCP Certification","2018-03-08");
    certificationRequestDto = StubFactory.testCertificationRequestDto("OCA certification", LocalDate.of(2019, 1, 1));
  }

  @Test
  void test_get() {
    // given
    when(certificationService.find(anyLong())).thenReturn(Optional.of(ocaCertificationDto));

    // act
    ResponseEntity<CertificationDto> responseEntity = certificationRestController.get(1);

    // assert
    assertNotNull(responseEntity);
    assertNotNull(responseEntity.getBody());
    assertEquals(ocaCertificationDto, responseEntity.getBody());
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  void test_get_resource_not_found() {
    // given
    when(certificationService.find(anyLong())).thenReturn(Optional.empty());

    // act
    ResponseEntity<CertificationDto> responseEntity = certificationRestController.get(1);

    // assert
    assertNotNull(responseEntity);
    assertNull(responseEntity.getBody());
    assertEquals(404, responseEntity.getStatusCodeValue());
  }

  @Test
  void test_get_all() {
    // given
    when(certificationService.findAll()).thenReturn(Arrays.asList(ocaCertificationDto, ocpCertificationDto));

    // act
    ResponseEntity<List<CertificationDto>> responseEntity = certificationRestController.getAll();

    // assert
    assertNotNull(responseEntity);
    assertEquals(200, responseEntity.getStatusCodeValue());
    assertNotNull(responseEntity.getBody());
    assertEquals(Arrays.asList(ocaCertificationDto, ocpCertificationDto), responseEntity.getBody());
  }

  @Test
  void test_create_without_validation_errors() {
    // given
    when(certificationService.create(any(CertificationRequestDto.class))).thenReturn(ocaCertificationDto);
    when(bindingResult.hasErrors()).thenReturn(false);

    // act
    ResponseEntity<CertificationDto> responseEntity = certificationRestController.create(certificationRequestDto, bindingResult);

    // assert
    assertNotNull(responseEntity);
    assertEquals(201, responseEntity.getStatusCodeValue());
    assertNotNull(responseEntity.getBody());
    assertEquals(ocaCertificationDto, responseEntity.getBody());
  }

  @Test
  void test_create_with_validation_errors() {
    // given
    when(certificationService.create(any(CertificationRequestDto.class))).thenReturn(ocaCertificationDto);
    when(bindingResult.hasErrors()).thenReturn(true);

    // act
    assertThrows(RestRequestValidationException.class, () -> certificationRestController.create(certificationRequestDto, bindingResult));
  }

  @Test
  void test_update_without_validation_errors() {
    // given
    when(certificationService.update(anyLong(), any(CertificationRequestDto.class))).thenReturn(Optional.of(ocaCertificationDto));
    when(bindingResult.hasErrors()).thenReturn(false);

    // act
    ResponseEntity<CertificationDto> responseEntity = certificationRestController.update(1L, certificationRequestDto, bindingResult);

    // assert
    assertNotNull(responseEntity);
    assertEquals(200, responseEntity.getStatusCodeValue());
    assertNotNull(responseEntity.getBody());
    assertEquals(ocaCertificationDto, responseEntity.getBody());
  }

  @Test
  void test_update_with_validation_errors() {
    // given
    when(certificationService.update(anyLong(), any(CertificationRequestDto.class))).thenReturn(Optional.of(ocaCertificationDto));
    when(bindingResult.hasErrors()).thenReturn(true);

    // act
    assertThrows(RestRequestValidationException.class, () -> certificationRestController.update(1L, certificationRequestDto, bindingResult));
  }

  @Test
  void test_update_not_existing_resource() {
    // given
    when(certificationService.update(anyLong(), any(CertificationRequestDto.class))).thenReturn(Optional.empty());
    when(bindingResult.hasErrors()).thenReturn(false);

    // act
    ResponseEntity<CertificationDto> responseEntity = certificationRestController.update(1L, certificationRequestDto, bindingResult);

    // assert
    assertNotNull(responseEntity);
    assertEquals(404, responseEntity.getStatusCodeValue());
    assertNull(responseEntity.getBody());
  }

  @Test
  void test_delete_existing_resource() {
    // when
    when(certificationService.delete(anyLong())).thenReturn(true);

    // act
    ResponseEntity<Void> responseEntity = certificationRestController.delete(1L);

    // assert
    assertNotNull(responseEntity);
    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  void test_delete_not_existing_resource() {
    // when
    when(certificationService.delete(anyLong())).thenReturn(false);

    // act
    ResponseEntity<Void> responseEntity = certificationRestController.delete(1L);

    // assert
    assertNotNull(responseEntity);
    assertEquals(404, responseEntity.getStatusCodeValue());
  }
}