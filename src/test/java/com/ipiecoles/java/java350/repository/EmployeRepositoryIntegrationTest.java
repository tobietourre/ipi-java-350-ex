package com.ipiecoles.java.java350.repository;


import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.Entreprise;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmployeRepositoryIntegrationTest {

    @Autowired
    private EmployeRepository employeRepository;

    @BeforeEach
    @AfterEach
    public void setup(){
        employeRepository.deleteAll();
    }

    @Test
    public void integrationAvgPerformanceWhereMatriculeStartsWith(){
        //Given
        employeRepository.save(new Employe("Doe1", "John1", "C00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0));
        employeRepository.save(new Employe("Doe2", "John2", "C00002", LocalDate.now(), Entreprise.SALAIRE_BASE, 5, 1.0));
        employeRepository.save(new Employe("Doe3", "John3", "C00003", LocalDate.now(), Entreprise.SALAIRE_BASE, 6, 1.0));
        employeRepository.save(new Employe("Doe4", "John4", "C00004", LocalDate.now(), Entreprise.SALAIRE_BASE, 4, 1.0));
        employeRepository.save(new Employe("Doe5", "John5", "C00005", LocalDate.now(), Entreprise.SALAIRE_BASE, 12, 1.0));

        //When
        Double average = employeRepository.avgPerformanceWhereMatriculeStartsWith("C");

        //Then
        Assertions.assertEquals(5.6, (double)average);
    }

}
