package com.rschir.prac.DTO;

import io.hypersistence.utils.hibernate.type.array.IntArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
public class GameSessionDTO {
    private Long x_id;
    private Long o_id;
}
