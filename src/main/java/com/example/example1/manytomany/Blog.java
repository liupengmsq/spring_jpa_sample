package com.example.example1.manytomany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/*
blog表：
CREATE TABLE `blog_many_to_many` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `description` varchar(45) DEFAULT NULL,
  `content` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

关联关系表：
CREATE TABLE `blog_tag_relation_many_to_many` (
  `blog_id` int NOT NULL,
  `tag_id` int NOT NULL,
  PRIMARY KEY (`tag_id`,`blog_id`),
  KEY `fk_blog_idx` (`blog_id`),
  KEY `fk_tag_idx` (`tag_id`),
  CONSTRAINT `fk_blog` FOREIGN KEY (`blog_id`) REFERENCES `blog_many_to_many` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag_many_to_many` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

 */
@Entity
@Table(name = "blog_many_to_many")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name= "title", nullable = false, length = 45)
    private String title;

    @Column(name= "description", nullable = true, length = 45)
    private String description;

    @Column(name= "content", nullable = true, length = 45)
    private String content;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    @JoinTable(name="blog_tag_relation_many_to_many",
            joinColumns=@JoinColumn(name="blog_id", referencedColumnName="id"),
            inverseJoinColumns=@JoinColumn(name="tag_id", referencedColumnName="id"))
    private List<Tag> tags = new ArrayList<Tag>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
