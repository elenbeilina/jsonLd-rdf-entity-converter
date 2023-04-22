package com.aqualen.springjsonldrdfdtoconverter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class EntityConfig {
    String domain;
    String status;
}
