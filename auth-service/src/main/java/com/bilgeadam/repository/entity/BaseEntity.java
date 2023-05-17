package com.bilgeadam.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;


@Data
@NoArgsConstructor
@AllArgsConstructor
// alttaki ikisini ekliyoruz:
@SuperBuilder
@MappedSuperclass
public class BaseEntity {
    private Long createDate;
    private Long updateDate;
}
