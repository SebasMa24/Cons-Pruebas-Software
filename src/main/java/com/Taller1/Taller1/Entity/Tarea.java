package com.Taller1.Taller1.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column
    private LocalDateTime recordatorio;

    // Constructor sin fecha de finalizacion
    public Tarea(Long id, String titulo, String descripcion, LocalDate fechaVencimiento, String estado) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                ", estado=" + estado +
                ", fechaFinalizacion=" + fechaFinalizacion +
                ", recordatorio=" + recordatorio +
                '}';
    }
}
