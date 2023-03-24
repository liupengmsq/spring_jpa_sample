package com.example.example1.manytomany;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);

    @Modifying
    @Query("delete from Tag t where t.name = ?1")
    void deleteByName(String name);
}
