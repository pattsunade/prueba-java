package com.nuvix.demo.movies_series_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuvix.demo.movies_series_api.dto.PeliculaRequestDTO;
import com.nuvix.demo.movies_series_api.model.Genero;
import com.nuvix.demo.movies_series_api.model.Peliculas;
import com.nuvix.demo.movies_series_api.repository.GeneroRepository;
import com.nuvix.demo.movies_series_api.repository.PeliculasRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PeliculasIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PeliculasRepository peliculaRepository;

    @Autowired
    private GeneroRepository generoRepository;

    private Genero generoAccion;

   @PersistenceContext
private EntityManager entityManager;

@BeforeEach
void setUp() {
    entityManager.createNativeQuery("DELETE FROM peliculas_generos").executeUpdate();
    entityManager.createNativeQuery("DELETE FROM series_generos").executeUpdate();
    // Luego borra las entidades principales
    peliculaRepository.deleteAll();
    generoRepository.deleteAll();
    generoAccion = generoRepository.save(new Genero(null, "Acción_")); 
}
    @Test
    void testCreatePelicula() throws Exception {
       PeliculaRequestDTO request = new PeliculaRequestDTO();
request.setTitulo("Matrix__");
request.setDirector("Wachowski_");
request.setAno_Lanzamiento(1999); 
request.setDuracionMinutos(120);  
request.setCalificacion(new BigDecimal("9.0"));
request.setGeneroIds(new HashSet<>(Collections.singletonList(generoAccion.getId())));

        mockMvc.perform(post("/api/peliculas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Matrix__"))
                .andExpect(jsonPath("$.generos[0].nombre").value("Acción_"));

        Optional<Peliculas> found = peliculaRepository.findByTitulo("Matrix__");
        assertThat(found).isPresent();
    }

    @Test
    void testGetAllPeliculas() throws Exception {
        Peliculas pelicula = new Peliculas();
        pelicula.setTitulo("Matrix");
        pelicula.setDirector("Wachowski");
        pelicula.setAno_lanzamiento(1999);
        pelicula.setCalificacion(new BigDecimal("9.0"));
        pelicula.setGeneros(new HashSet<>(Collections.singletonList(generoAccion)));
        peliculaRepository.save(pelicula);

        mockMvc.perform(get("/api/peliculas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.peliculas[0].titulo").value("Matrix"));
            }

    @Test
    void testGetPeliculaById_found() throws Exception {
        Peliculas pelicula = new Peliculas();
        pelicula.setTitulo("Matrix");
        pelicula.setDirector("Wachowski");
        pelicula.setAno_lanzamiento(1999);
        pelicula.setCalificacion(new BigDecimal("9.0"));
        pelicula.setGeneros(new HashSet<>(Collections.singletonList(generoAccion)));
        pelicula = peliculaRepository.save(pelicula);

        mockMvc.perform(get("/api/peliculas/{id}", pelicula.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Matrix"));
    }

    @Test
    void testGetPeliculaById_notFound() throws Exception {
        mockMvc.perform(get("/api/peliculas/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeletePelicula() throws Exception {
        Peliculas pelicula = new Peliculas();
        pelicula.setTitulo("Matrix");
        pelicula.setDirector("Wachowski");
        pelicula.setAno_lanzamiento(1999);
        pelicula.setCalificacion(new BigDecimal("9.0"));
        pelicula.setGeneros(new HashSet<>(Collections.singletonList(generoAccion)));
        pelicula = peliculaRepository.save(pelicula);

        mockMvc.perform(delete("/api/peliculas/{id}", pelicula.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(peliculaRepository.findById(pelicula.getId())).isNotPresent();
    }
}