package com.workintech.s18d2.dao;

import com.workintech.s18d2.entity.Fruit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;


public interface FruitRepository extends JpaRepository<Fruit, Long> {
    @Query(value="select f.id , f.name, f.price, f.fruit_type from fsweb.fruit f Order by f.price ASC", nativeQuery = true)
    List<Fruit> getByPriceAsc();

    @Query(value="select f.id , f.name, f.price, f.fruit_type from fsweb.fruit f Order by f.price DESC", nativeQuery = true)
    List<Fruit> getByPriceDesc();

    @Query("SELECT f FROM Fruit f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Fruit> searchByName(@Param("name") String name);

}