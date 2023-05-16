package br.com.acoes.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import br.com.acoes.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    @Column(name = "dh_created_at")
    private LocalDateTime dataCreated;

    @Column(name = "dh_updated_at")
    private LocalDateTime dataUpdated;

    @PrePersist
    public void prePersist() {
        this.dataCreated = DateUtil.getDataAtual();
    }

	@PreUpdate
	public void preUpdate() {
		this.dataUpdated = DateUtil.getDataAtual();
	}
}
