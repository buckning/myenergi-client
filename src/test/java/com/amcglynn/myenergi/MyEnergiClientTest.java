package com.amcglynn.myenergi;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MyEnergiClientTest {

    @Test
    void name() {
        assertThat(new MyEnergiClient()).isNotNull();
    }
}
