package org.example;


    public class Payment {

        private String studentId;
        private String paymentType;
        private double amount;
        private boolean paid = false;

        public void makePayment(String studentId,
                                String paymentType,
                                double amount) throws Exception {

            if(amount <= 0){
                throw new IllegalArgumentException("Invalid Amount!");
            }

            this.studentId = studentId;
            this.paymentType = paymentType;
            this.amount = amount;
            this.paid = true;
        }

        public String getStudentId() {
            return studentId;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public double getAmount() {
            return amount;
        }

        public boolean isPaid() {
            return paid;
        }
}
