package com.ipiecoles.java.java350.model;

//import org.junit.Assert;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class EmployeTest {

    @Test
    public void testGetNbAnneeAncienneteNow(){
        //Given
        Employe employe = new Employe();
        LocalDate dateEmbauche = LocalDate.now();
        employe.setDateEmbauche(dateEmbauche);

        //when
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    @Test
    public void testGetNbAnneeAncienneteAfter(){
    //Given
    Employe employe = new Employe();
    LocalDate dateEmbauche = LocalDate.now().plusYears(1);
    employe.setDateEmbauche(dateEmbauche);

    //when
    Integer nbAnnee = employe.getNombreAnneeAnciennete();

    //then
    Assertions.assertThat(nbAnnee).isEqualTo(0);
    }

    @Test
    public void testGetNbAnneeAncienneteBefore(){
        //Given
        Employe employe = new Employe();
        LocalDate dateEmbauche = LocalDate.now().minusYears(1);
        employe.setDateEmbauche(dateEmbauche);

        //when
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(nbAnnee).isEqualTo(1);
    }

    @Test
    public void testGetNbAnneeAncienneteNull(){
        //Given
        Employe employe = new Employe();
        LocalDate dateEmbauche = LocalDate.now().minusYears(1);
        employe.setDateEmbauche(null);

        //when
        Integer nbAnnee = employe.getNombreAnneeAnciennete();

        //then
        Assertions.assertThat(nbAnnee).isEqualTo(0);
    }
}
