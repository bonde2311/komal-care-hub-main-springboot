-- Fix: Allow reviews without a product (general site-level reviews)
ALTER TABLE reviews ALTER COLUMN product_id DROP NOT NULL;
