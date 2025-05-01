package com.workintech.s18d2;

import com.workintech.s18d2.entity.Fruit;
import com.workintech.s18d2.entity.FruitType;
import com.workintech.s18d2.entity.Vegetable;
import com.workintech.s18d2.exceptions.PlantException;
import com.workintech.s18d2.dao.FruitRepository;
import com.workintech.s18d2.services.FruitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@ExtendWith(ResultAnalyzer.class)
class MainTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FruitRepository fruitRepository;

    @Mock
    private FruitRepository mockFruitRepository;



    private FruitServiceImpl fruitService;

    private Fruit sampleFruitForFruitServiceTest;

    @BeforeEach
    void setup() {

        Fruit apple = new Fruit();
        apple.setName("Apple");
        apple.setPrice(15.0);
        apple.setFruitType(FruitType.SWEET);
        entityManager.persist(apple);

        Fruit lemon = new Fruit();
        lemon.setName("Lemon");
        lemon.setPrice(25.0);
        lemon.setFruitType(FruitType.SOUR);
        entityManager.persist(lemon);

        entityManager.flush();

        sampleFruitForFruitServiceTest = new Fruit();
        sampleFruitForFruitServiceTest.setId(1L);
        sampleFruitForFruitServiceTest.setName("Apple");

        fruitService = new FruitServiceImpl(mockFruitRepository);
    }

    @Test
    @DisplayName("Fruit properties are set correctly")
    void testFruitProperties() {
        Fruit fruit = new Fruit();
        fruit.setId(1L);
        fruit.setName("Apple");
        fruit.setPrice(15.0);
        fruit.setFruitType(FruitType.SWEET);

        assertEquals(1L, fruit.getId());
        assertEquals("Apple", fruit.getName());
        assertEquals(15, fruit.getPrice());
        assertEquals(FruitType.SWEET, fruit.getFruitType());
    }

    @Test
    @DisplayName("Enum should contain expected values")
    void enumShouldContainExpectedValues() {
        assertTrue(FruitType.valueOf("SWEET") == FruitType.SWEET);
        assertTrue(FruitType.valueOf("SOUR") == FruitType.SOUR);
    }

    @Test
    @DisplayName("FruitType Enum should contain exact number of values")
    void enumShouldContainExactNumberOfValues() {
        assertTrue(FruitType.values().length == 2);
    }

    @Test
    @DisplayName("Vegetable getters and setters are set correctly")
    void testVegetableProperties() {
        Vegetable vegetable = new Vegetable();
        vegetable.setId(2L);
        vegetable.setName("Carrot");
        vegetable.setPrice(20.0);
        vegetable.setGrownOnTree(false);

        assertEquals(2L, vegetable.getId());
        assertEquals("Carrot", vegetable.getName());
        assertEquals(20, vegetable.getPrice());
        assertFalse(vegetable.isGrownOnTree());


        vegetable.setGrownOnTree(true);
        assertTrue(vegetable.isGrownOnTree());
    }

    @Test
    @DisplayName("FruitRepository::getByPriceDesc should return fruits in descending order of price")
    void testGetByPriceDesc() {
        List<Fruit> fruits = fruitRepository.getByPriceDesc();
        assertEquals(2, fruits.size());
        assertTrue(fruits.get(0).getPrice() >= fruits.get(1).getPrice());
    }

    @Test
    @DisplayName("FruitRepository::getByPriceAsc should return fruits in ascending order of price")
    void testGetByPriceAsc() {
        List<Fruit> fruits = fruitRepository.getByPriceAsc();
        assertEquals(2, fruits.size());
        assertTrue(fruits.get(0).getPrice() <= fruits.get(1).getPrice());
    }

    @Test
    @DisplayName("FruitRepository::searchByName should return fruits with matching name")
    void testSearchByName() {
        List<Fruit> fruits = fruitRepository.searchByName("Apple");
        assertEquals(1, fruits.size());
        assertEquals("Apple", fruits.get(0).getName());
    }

    @Test
    @DisplayName("FruitService::getById() should return a fruit when a fruit with the given id exists")
    void testGetByIdFoundFruitService() {
        when(mockFruitRepository.findById(anyLong())).thenReturn(Optional.of(sampleFruitForFruitServiceTest));

        Fruit foundFruit = fruitService.getById(1L);

        assertNotNull(foundFruit);
        assertEquals(sampleFruitForFruitServiceTest.getName(), foundFruit.getName());
    }

    @Test
    @DisplayName("FruitService::getById() should throw PlantException when a fruit with the given id does not exist")
    void testGetByIdNotFoundFruitService() {
        when(mockFruitRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(PlantException.class, () -> fruitService.getById(1L));
    }

    @Test
    @DisplayName("FruitService::getAll() should return all fruits")
    void testGetByPriceAscFruitService() {
        when(mockFruitRepository.getByPriceAsc()).thenReturn(Arrays.asList(sampleFruitForFruitServiceTest));

        List<Fruit> fruits = fruitService.getByPriceAsc();

        assertFalse(fruits.isEmpty());
        assertEquals(1, fruits.size());
    }

    @Test
    @DisplayName("FruitService::getAll() should return all fruits")
    void testSaveFruitService() {
        when(mockFruitRepository.save(any(Fruit.class))).thenReturn(sampleFruitForFruitServiceTest);

        Fruit savedFruit = fruitService.save(new Fruit());

        assertNotNull(savedFruit);
        assertEquals(sampleFruitForFruitServiceTest.getName(), savedFruit.getName());
    }

    @Test
    @DisplayName("FruitService::delete() should return the deleted fruit")
    void testDeleteFruitService() {
        when(mockFruitRepository.findById(anyLong())).thenReturn(Optional.of(sampleFruitForFruitServiceTest));
        doNothing().when(mockFruitRepository).delete(any(Fruit.class));

        Fruit deletedFruit = fruitService.delete(1L);

        assertNotNull(deletedFruit);
        assertEquals(sampleFruitForFruitServiceTest.getName(), deletedFruit.getName());
    }

    @Test
    @DisplayName("FruitService::searchByName() should return fruits with the given name")
    void testSearchByNameFruitService() {
        when(mockFruitRepository.searchByName(anyString())).thenReturn(Arrays.asList(sampleFruitForFruitServiceTest));

        List<Fruit> fruits = fruitService.searchByName("Apple");

        assertFalse(fruits.isEmpty());
        assertEquals(1, fruits.size());
        assertEquals(sampleFruitForFruitServiceTest.getName(), fruits.get(0).getName());
    }



}
