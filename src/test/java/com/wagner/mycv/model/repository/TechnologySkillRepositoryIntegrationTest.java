package com.wagner.mycv.model.repository;

import com.wagner.mycv.model.entity.TechnologySkill;
import com.wagner.mycv.testutil.TechnologySkillTestUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
class TechnologySkillRepositoryIntegrationTest {

  private final TechnologySkill technologySkill = TechnologySkillTestUtil.createTestEntity();

  @Autowired private DataSource dataSource;
  @Autowired private JdbcTemplate jdbcTemplate;
  @Autowired private EntityManager entityManager;
  @Autowired private TechnologySkillRepository technologySkillRepository;

  @Test
  void injectedComponentsAreNotNull(){
    assertThat(dataSource).isNotNull();
    assertThat(jdbcTemplate).isNotNull();
    assertThat(entityManager).isNotNull();
    assertThat(technologySkillRepository).isNotNull();
  }

  @Test
  void test_find_technology_skill() {
    entityManager.persist(technologySkill);
    Optional<TechnologySkill> result = technologySkillRepository.findById(1L);
    assertThat(result.isPresent()).isTrue();

    //noinspection OptionalGetWithoutIsPresent
    TechnologySkill tehnologySkillResponse = result.get();

    assertThat(tehnologySkillResponse).isNotNull();
    assertThat(tehnologySkillResponse.getId()).isEqualTo(1L);
    assertThat(tehnologySkillResponse.getCategory()).isEqualTo(technologySkill.getCategory());
    assertThat(tehnologySkillResponse.getSkillNames()).isEqualTo(technologySkill.getSkillNames());
    assertThat(tehnologySkillResponse.getUserId()).isEqualTo(technologySkill.getUserId());
  }
}