package com.leemccormick.lombokloggingdebugging.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

// @AllArgsConstructor + @Data --> This is how the Model java class with Lombok, much shorter....
@AllArgsConstructor
@Data
public class NumbersLombok {
    private int a;
    private int b;
}
