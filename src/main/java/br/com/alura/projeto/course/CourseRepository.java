package br.com.alura.projeto.course;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Long> {
  boolean existsByCode(String code);

  Optional<Course> findByCode(String code);

  @Query(
      "select distinct cat.name "
          + "from Course c "
          + "join c.category cat "
          + "where c.status = br.com.alura.projeto.course.CourseStatus.ACTIVE "
          + "order by cat.name asc")
  List<String> findActiveCategoryNames();

  @Query(
      "select new br.com.alura.projeto.course.CourseDTO(c) "
          + "from Course c "
          + "join c.category cat "
          + "join c.instructor inst "
          + "where c.status = br.com.alura.projeto.course.CourseStatus.ACTIVE "
          + "and (:categoryName is null or :categoryName = '' or cat.name = :categoryName) "
          + "order by c.name asc")
  List<CourseDTO> findActiveByCategoryAsDTO(@Param("categoryName") String categoryName);

  @Query(
      "select new br.com.alura.projeto.course.CourseDTO(c) "
          + "from Course c "
          + "join c.category cat "
          + "join c.instructor inst "
          + "where c.status = br.com.alura.projeto.course.CourseStatus.ACTIVE "
          + "order by c.name asc")
  List<CourseDTO> findAllActiveAsDTO();
}
