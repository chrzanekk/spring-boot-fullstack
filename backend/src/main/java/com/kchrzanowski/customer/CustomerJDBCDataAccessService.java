package com.kchrzanowski.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//for test and learn only
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = """
                SELECT id,name,email,password,age,gender
                FROM customer
                """;
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Long id) {
        var sql = """
                SELECT id,name,email,password,age,gender
                FROM customer
                WHERE id = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, id).stream().findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email,password, age, gender)
                VALUES (?, ?, ?, ?,?);
                """;

        int update = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender().name());

        System.out.println("jdbcTemplate.update: " + update);

    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE email = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Long id) {
        var sql = """
                DELETE FROM customer
                WHERE id = ?
                """;
        jdbcTemplate.update(sql, id);
        System.out.println("jdbcTemplate delete: " + sql);
    }

    @Override
    public boolean existsCustomerById(Long id) {
        var sql = """
                SELECT count(id)
                FROM customer
                WHERE id = ?
                """;
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    //todo this is not effective...i will not use JDBC template in future
    @Override
    public void updateCustomer(Customer customer) {
        if (customer.getName() != null) {
            String sql = "UPDATE customer SET name = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, customer.getName(), customer.getId());
            System.out.println("jdbcTemplate update name: " + result);
        }

        if (customer.getAge() != 0) {
            String sql = "UPDATE customer SET age = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, customer.getAge(), customer.getId());
            System.out.println("jdbcTemplate update age: " + result);
        }

        if (customer.getEmail() != null) {
            String sql = "UPDATE customer SET email = ? WHERE id =?";
            int result = jdbcTemplate.update(sql, customer.getEmail(), customer.getId());
            System.out.println("jdbcTemplate update email: " + result);
        }

        if (customer.getGender() != null) {
            String sql = "UPDATE customer SET gender =? WHERE id =?";
            int result = jdbcTemplate.update(sql, customer.getGender().name(), customer.getId());
            System.out.println("jdbcTemplate update gender: " + result);
        }
    }

    @Override
    public Optional<Customer> selectCustomerByEmail(String email) {
        var sql = """
                SELECT id,name,email,password,age,gender
                FROM customer
                WHERE email = ?
                """;
        return jdbcTemplate.query(sql, customerRowMapper, email).stream().findFirst();
    }
}
