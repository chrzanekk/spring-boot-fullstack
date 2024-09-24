package com.kchrzanowski.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getInt("age")).thenReturn(2);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("gender")).thenReturn(Gender.MALE.name());

        Customer actual = customerRowMapper.mapRow(resultSet,1);

        Customer expected = new Customer(
                1L,
                "John",
                "john@example.com"
                ,2, Gender.MALE);
        assertThat(expected).isEqualTo(actual);
    }
}