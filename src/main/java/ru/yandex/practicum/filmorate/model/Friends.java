package ru.yandex.practicum.filmorate.model;

import lombok.Data;

//@Data
public class Friends {
    Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString() {
        return "{id:" + id + "}";
    }
}
