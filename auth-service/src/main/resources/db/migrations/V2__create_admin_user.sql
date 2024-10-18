-- Проверка, существует ли пользователь "admin"
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM salon.users WHERE user_name = 'admin') THEN
            -- Вставляем пользователя
            INSERT INTO salon.users (user_name, password, enabled)
            VALUES ('admin', '{bcrypt}$2y$10$Ua2ZXcmfY/eMsq5YgZo1UupMVXoAZkuZ6Q9yXbpB8Wd/nGSr2B1hu', 'true');

            -- Вставляем роль для пользователя
            INSERT INTO salon.authorities (name)
            VALUES ('ROLE_ADMIN');
        END IF;
    END $$;
