-- Проверка, существует ли пользователь "admin"
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin') THEN
            -- Вставляем пользователя
            INSERT INTO users (username, password, enabled)
            VALUES ('admin', '{bcrypt}$2a$10$XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX', true);

            -- Вставляем роль для пользователя
            INSERT INTO authorities (user_id, role)
            VALUES ((SELECT id FROM users WHERE username = 'admin'), 'ROLE_ADMIN');
        END IF;
    END $$;
