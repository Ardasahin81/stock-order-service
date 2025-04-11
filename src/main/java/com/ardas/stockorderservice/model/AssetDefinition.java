package com.ardas.stockorderservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "s_asset_definition", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class AssetDefinition extends Base {

    @Column
    private String name;

}
