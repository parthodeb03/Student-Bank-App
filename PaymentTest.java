package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class paymentTest {
    @Test
    void testSuccessfulPayment() throws Exception {

        Payment payment = new Payment();

        payment.makePayment(
                "241-134-021",
                "Semester Fee",
                5000
        );

        assertEquals("241-134-021", payment.getStudentId());
        assertEquals("Semester Fee", payment.getPaymentType());
        assertEquals(5000, payment.getAmount());
        assertTrue(payment.isPaid());
    }

    @Test
    void testInvalidAmount() {

        Payment payment = new Payment();

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> payment.makePayment(
                        "241-134-021",
                        "Semester Fee",
                        -1000
                )
        );

        assertEquals("Invalid Amount!", exception.getMessage());
    }

    @Test
    void testPaymentStatusBeforePayment() {

        Payment payment = new Payment();

        assertFalse(payment.isPaid());
    }

}