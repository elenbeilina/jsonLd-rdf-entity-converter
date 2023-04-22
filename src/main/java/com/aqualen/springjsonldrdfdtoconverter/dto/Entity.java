package com.aqualen.springjsonldrdfdtoconverter.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * Object that will be passed to Entity Api.
 */
@Data
@Builder
public class Entity implements Serializable {

    private String id;
    private String name;
    private String domain;
    private String type;
    private String status;
    private String resourceType;

}
