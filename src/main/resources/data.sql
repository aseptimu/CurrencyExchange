INSERT INTO currencies (code, full_name, sign)
VALUES ('AUD', 'Australian dollar', 'A$'),
       ('USD', 'United States Dollar', '$'),
       ('EUR', 'Euro', '€'),
       ('GBP', 'Pound Sterling', '£');

INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (2, 3, 0.9),
       (2, 4, 0.8);