package com.beard.train.builder;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LombokBuilder {

    private String name;
    private Integer age;
}
