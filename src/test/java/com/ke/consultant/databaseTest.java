package com.ke.consultant;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class databaseTest {
    @Test
    public void testDB() throws Exception {
        JDBC jdbc = new JDBC();
        StudentBean[] students = jdbc.queryDB("student");
        StudentBean[] replicate = jdbc.queryDB("student_replicate");
        for (Integer n = 0; n < students.length; n++) {
            assertEquals(students[n], replicate[n]);
        }
    }
}