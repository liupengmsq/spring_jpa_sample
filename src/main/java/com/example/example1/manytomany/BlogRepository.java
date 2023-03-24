package com.example.example1.manytomany;

import com.example.example1.onetomany.ClassOfStudent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Blog findByTitle(String title);

    @Modifying
    @Query("delete from Blog b where b.title = ?1")
    void deleteByTitle(String title);
}
