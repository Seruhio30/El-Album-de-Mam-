package com.album_de_mama.back_end.memory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "memories")
public class Memory {

    @Id
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(name = "memory_date", nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 100)
    private String place;

    @Column(name = "file_path", nullable = false, length = 255)
    private String file;

    @Column(nullable = false, length = 255)
    private String thumbnail;

    @Column(nullable = false, length = 500)
    private String description;

    protected Memory() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public String getCategory() {
        return category;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getPlace() {
        return place;
    }

    public String getFile() {
        return file;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getDescription() {
        return description;
    }
}