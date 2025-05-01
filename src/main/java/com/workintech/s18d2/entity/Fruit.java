package com.workintech.s18d2.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "fruit", schema = "fsweb")
public class Fruit extends Plant {



    @Column(name="fruit_type")
    @Enumerated(EnumType.STRING)
    @NotNull
    private  FruitType fruitType;
}
