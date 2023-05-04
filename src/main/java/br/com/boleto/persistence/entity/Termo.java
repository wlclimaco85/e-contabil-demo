package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "termos", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Termo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer versao;
	private String htmlTermo;
	@Column(name = "dh_created_at")
    private LocalDateTime dataCreated;
}
