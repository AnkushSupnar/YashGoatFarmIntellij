-- IGST Migration Script
-- Run this on your MySQL database before launching the updated application

ALTER TABLE item ADD COLUMN igst DECIMAL(5,2) NOT NULL DEFAULT 0.00;

ALTER TABLE transaction
    ADD COLUMN igst_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN igst_amount  DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE bill ADD COLUMN igst_total DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE quotation ADD COLUMN igst_total DECIMAL(10,2) NOT NULL DEFAULT 0.00;

ALTER TABLE quotation_transaction
    ADD COLUMN igst_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    ADD COLUMN igst_amount  DECIMAL(10,2) NOT NULL DEFAULT 0.00;
