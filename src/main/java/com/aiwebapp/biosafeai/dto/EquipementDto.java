package com.aiwebapp.biosafeai.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EquipementDto {
    private Long id;
    private String name;
    private String type;
    private List<String> mediaUrls;
    private Long proprietaireId;
    private BigDecimal rentPrice;
    private BigDecimal sellingPrice;
    private boolean available;
} 