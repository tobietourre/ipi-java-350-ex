package com.ipiecoles.java.java350.model;

import cucumber.api.java.it.Ma;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void getNombreAnneeAncienneteNow(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now());

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNminus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().minusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(2, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNull(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(null);

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @Test
    public void getNombreAnneeAncienneteNplus2(){
        //Given
        Employe e = new Employe();
        e.setDateEmbauche(LocalDate.now().plusYears(2L));

        //When
        Integer anneeAnciennete = e.getNombreAnneeAnciennete();

        //Then
        Assertions.assertEquals(0, anneeAnciennete.intValue());
    }

    @ParameterizedTest
    @CsvSource({
            "1, 'T12345', 0, 1.0, 1000.0",
            "1, 'T12345', 2, 0.5, 600.0",
            "1, 'T12345', 2, 1.0, 1200.0",
            "2, 'T12345', 0, 1.0, 2300.0",
            "2, 'T12345', 1, 1.0, 2400.0",
            "1, 'M12345', 0, 1.0, 1700.0",
            "1, 'M12345', 5, 1.0, 2200.0",
            "2, 'M12345', 0, 1.0, 1700.0",
            "2, 'M12345', 8, 1.0, 2500.0"
    })
    public void getPrimeAnnuelle(Integer performance, String matricule, Long nbYearsAnciennete, Double tempsPartiel, Double primeAnnuelle){
        //Given
        Employe employe = new Employe("Doe", "John", matricule, LocalDate.now().minusYears(nbYearsAnciennete), Entreprise.SALAIRE_BASE, performance, tempsPartiel);

        //When
        Double prime = employe.getPrimeAnnuelle();

        //Then
        Assertions.assertEquals(primeAnnuelle, prime);

    }

    @ParameterizedTest
    @CsvSource({
            "1521.22, 0.5, 2281.83",
            "1521.22, 0, 1521.22",
            "1521.22, -0.5, 1521.22",
            "1521.22, 1.5, 3803.05",
    })
    public void testAugmenterSalaire(Double salaire, Double augmentation, Double salaireAugemnte){
        //Given
        Employe employe = new Employe("Doe", "John", "T00001", LocalDate.now(), salaire, 1, 1.0);

        //When
        employe.augmenterSalaire(augmentation);

        //Then
        org.assertj.core.api.Assertions.assertThat(employe.getSalaire()).isEqualTo(salaireAugemnte);
    }

    @Test
    public void testAugmenterSalaireNull(){
        //Given
        Employe employe = new Employe("Doe", "John", "T00001", LocalDate.now(), Entreprise.SALAIRE_BASE, 1, 1.0);
        Double augmentation = null;

        //When
        Assertions.assertThrows(NullPointerException.class, () -> employe.augmenterSalaire(augmentation));

        //Then
        org.assertj.core.api.Assertions.assertThat(employe.getSalaire()).isEqualTo(Entreprise.SALAIRE_BASE);
    }

    @ParameterizedTest
    @CsvSource({
            "2016, 366, 105, 8, 1.0",
            "2017, 365, 105, 9, 1.0",
            "2018, 365, 104, 9, 1.0",
            "2019, 365, 104, 10, 1.0",
            "2020, 366, 104, 9, 1.0"
    })
    public void testGetNbRtt(Integer annee, Integer nbJours, Integer nbJoursReposHebdo, Integer nbJoursFeriesOuvres, Double tempsPartiel){
        //Given
        LocalDate date = LocalDate.now().minusYears((long) 2019 - annee);
        Employe e = new Employe();
        e.setTempsPartiel(tempsPartiel);
        Integer result = (int) Math.ceil((nbJours - Entreprise.NB_JOURS_MAX_FORFAIT - nbJoursReposHebdo - Entreprise.NB_CONGES_BASE - nbJoursFeriesOuvres) * tempsPartiel);

        //When
        Integer nbRtt = e.getNbRtt(date);

        //Then
        Assertions.assertEquals(result, nbRtt);
    }

}