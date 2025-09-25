package com.Taller1.Taller1.Entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tarea{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false) 
    private String titulo;

    @Column
    private String descripcion;

    @Column
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    private String estado;

    @Column
    private LocalDate fechaFinalizacion;

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                ", estado=" + estado +
                ", fechaFinalizacion=" + fechaFinalizacion +
                '}';
    }
}
