package com.ipiecoles.java.java350.service;

import com.ipiecoles.java.java350.exception.EmployeException;
import com.ipiecoles.java.java350.model.Employe;
import com.ipiecoles.java.java350.model.NiveauEtude;
import com.ipiecoles.java.java350.model.Poste;
import com.ipiecoles.java.java350.repository.EmployeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeServiceTest {

    @InjectMocks
    EmployeService employeService;

    @Mock
    EmployeRepository employeRepository;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.initMocks(this.getClass());
    }

    @Test
    public void testEmbaucheEmployeTechnicienPleinTempsBts() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.TECHNICIEN;
        NiveauEtude niveauEtude = NiveauEtude.BTS_IUT;
        Double tempsPartiel = 1.0;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("T00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("T00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.2 * 1.0
        Assertions.assertEquals(1825.46, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("00345");
        when(employeRepository.findByMatricule("M00346")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals(nom, employeArgumentCaptor.getValue().getNom());
        Assertions.assertEquals(prenom, employeArgumentCaptor.getValue().getPrenom());
        Assertions.assertEquals(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")), employeArgumentCaptor.getValue().getDateEmbauche().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
        Assertions.assertEquals("M00346", employeArgumentCaptor.getValue().getMatricule());
        Assertions.assertEquals(tempsPartiel, employeArgumentCaptor.getValue().getTempsPartiel());

        //1521.22 * 1.4 * 0.5
        Assertions.assertEquals(1064.85, employeArgumentCaptor.getValue().getSalaire().doubleValue());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterNoLastMatricule() throws EmployeException {
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(null);

        //When
        employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel);

        //Then
        ArgumentCaptor<Employe> employeArgumentCaptor = ArgumentCaptor.forClass(Employe.class);
        verify(employeRepository, times(1)).save(employeArgumentCaptor.capture());
        Assertions.assertEquals("M00001", employeArgumentCaptor.getValue().getMatricule());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMasterExistingEmploye(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn(null);
        when(employeRepository.findByMatricule("M00001")).thenReturn(new Employe());

        //When/Then
        EntityExistsException e = Assertions.assertThrows(EntityExistsException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("L'employé de matricule M00001 existe déjà en BDD", e.getMessage());
    }

    @Test
    public void testEmbaucheEmployeManagerMiTempsMaster99999(){
        //Given
        String nom = "Doe";
        String prenom = "John";
        Poste poste = Poste.MANAGER;
        NiveauEtude niveauEtude = NiveauEtude.MASTER;
        Double tempsPartiel = 0.5;
        when(employeRepository.findLastMatricule()).thenReturn("99999");

        //When/Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () -> employeService.embaucheEmploye(nom, prenom, poste, niveauEtude, tempsPartiel));
        Assertions.assertEquals("Limite des 100000 matricules atteinte !", e.getMessage());
    }

    @ParameterizedTest
    @CsvSource({
            "'C001', 79999, 4, 1",
            "'C001', 79999, 0, 1",
            "'C001', 80000, 4, 2",
            "'C001', 90000, 2, 1",
            "'C001', 94999, 4, 2 ",
            "'C001', 95000, 2, 2",
            "'C001', 100000, 0, 1",
            "'C001', 104999, 2, 2",
            "'C001', 105000, 1, 2",
            "'C001', 105000, -1, 1",
            "'C001', 119999, 3, 4",
            "'C001', 120000, 3, 7",
            "'C001', 120000, -4, 1",
    })
    public void testCalculPerformanceCommercial(String matricule, Long caTraite, Integer performance, Integer expectedPerformance) throws EmployeException{
        //Given
        Employe employe = new Employe();
        employe.setPerformance(performance);
        when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn(null);
        Long objectifCa = 100000l;

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        performance = employe.getPerformance();

        //Then
        Assertions.assertEquals(expectedPerformance, performance);
    }

    @ParameterizedTest
    @CsvSource({
            "'C001', 79999, 4, 1",
            "'C001', 79999, 0, 1",
            "'C001', 80000, 4, 2",
            "'C001', 90000, 2, 1",
            "'C001', 94999, 4, 2 ",
            "'C001', 95000, 2, 2",
            "'C001', 100000, 0, 1",
            "'C001', 104999, 2, 2",
            "'C001', 105000, 1, 2",
            "'C001', 105000, -1, 1",
            "'C001', 119999, 3, 4",
            "'C001', 120000, 3, 7",
            "'C001', 120000, -4, 1",
    })
    public void testCalculPerformanceCommercialAboveAverage(String matricule, Long caTraite, Integer performance, Integer expectedPerformance) throws EmployeException{
        //Given
        Employe employe = new Employe();
        employe.setPerformance(performance);
        when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double) expectedPerformance - 1);
        Long objectifCa = 100000l;
        expectedPerformance = expectedPerformance + 1;

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        performance = employe.getPerformance();

        //Then
        Assertions.assertEquals(expectedPerformance, performance);
    }

    @ParameterizedTest
    @CsvSource({
            "'C001', 79999, 4, 1",
            "'C001', 79999, 0, 1",
            "'C001', 80000, 4, 2",
            "'C001', 90000, 2, 1",
            "'C001', 94999, 4, 2 ",
            "'C001', 95000, 2, 2",
            "'C001', 100000, 0, 1",
            "'C001', 104999, 2, 2",
            "'C001', 105000, 1, 2",
            "'C001', 105000, -1, 1",
            "'C001', 119999, 3, 4",
            "'C001', 120000, 3, 7",
            "'C001', 120000, -4, 1",
    })
    public void testCalculPerformanceCommercialBelowAverage(String matricule, Long caTraite, Integer performance, Integer expectedPerformance) throws EmployeException{
        //Given
        Employe employe = new Employe();
        employe.setPerformance(performance);
        when(employeRepository.findByMatricule(matricule)).thenReturn(employe);
        when(employeRepository.avgPerformanceWhereMatriculeStartsWith("C")).thenReturn((double) expectedPerformance + 1);
        Long objectifCa = 100000l;

        //When
        employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa);
        performance = employe.getPerformance();

        //Then
        Assertions.assertEquals(expectedPerformance, performance);
    }

    @Test
    public void testCalculPerformanceCommercialMatriculeNull() throws EmployeException{
        //Given
        String matricule = null;
        Long caTraite = 100000l;
        Long objectifCa = 100000l;

        //When //Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () ->  employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        Assertions.assertEquals("Le matricule ne peut être null et doit commencer par un C !", e.getMessage());
    }

    @Test
    public void testCalculPerformanceCommercialCaTraiteNull() throws EmployeException{
        //Given
        String matricule = "C001";
        Long caTraite = null;
        Long objectifCa = 100000l;

        //When //Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () ->  employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        Assertions.assertEquals("Le chiffre d'affaire traité ne peut être négatif ou null !", e.getMessage());
    }

    @Test
    public void testCalculPerformanceCommercialObjectifCaNull() throws EmployeException{
        //Given
        String matricule = "C001";
        Long caTraite = 100000l;
        Long objectifCa = null;

        //When // Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () ->  employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        Assertions.assertEquals("L'objectif de chiffre d'affaire ne peut être négatif ou null !", e.getMessage());
    }

    @Test
    public void testCalculPerformanceCommercialMatriculeInexistant() throws EmployeException{
        //Given
        String matricule = "C001";
        Long caTraite = 100000l;
        Long objectifCa = 100000l;
        when(employeRepository.findByMatricule(matricule)).thenReturn(null);

        //When //Then
        EmployeException e = Assertions.assertThrows(EmployeException.class, () ->  employeService.calculPerformanceCommercial(matricule, caTraite, objectifCa));
        Assertions.assertEquals("Le matricule " + matricule + " n'existe pas !", e.getMessage());
    }

}