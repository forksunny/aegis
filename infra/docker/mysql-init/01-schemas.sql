CREATE DATABASE IF NOT EXISTS aegis_quote CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'aegis_quote'@'%' IDENTIFIED BY 'aegis_quote_pw';

GRANT ALL PRIVILEGES ON aegis_quote.* TO 'aegis_quote'@'%';

FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS aegis_policy CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE USER IF NOT EXISTS 'aegis_policy'@'%' IDENTIFIED BY 'aegis_policy_pw';

GRANT ALL PRIVILEGES ON aegis_policy.* TO 'aegis_policy'@'%';

FLUSH PRIVILEGES;