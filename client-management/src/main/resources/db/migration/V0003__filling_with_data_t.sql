set search_path = salon;

insert into service_to_consumables (service_id, consumable_id)
select s.id, c.id
from services s,
     consumables c
where s.title = 'Простое окрашивание'
  and c.title in ('Краска',
                  'Полотенце'
    );

insert into visits (date, start_time, service_id, client_id, master_id)
values ('2006-12-11', '12:00:01', 1, 2, 3),
       ('2006-12-12', '12:00:02', 1, 1, 1),
       ('2006-12-13', '12:00:03', 1, 1, 2);

insert into clients (first_name, last_name, contact, dob)
values ('Иван', 'Иванов', '+79998887766', '2001-01-01'),
       ('Пётр', 'Петров', '+79995554433', '2002-02-02');

insert into services (title, price, duration, description)
values ('Мужская стрижка', 299, '20', 'любая стрижка'),
       ('Женская стрижка', 299, '60', 'любая стрижка, кроме "каре"'),
       ('Простое окрашивание', 1000, '120', 'короткие волосы');

insert into employees (first_name, last_name, contact, dob, function)
values ('Светлана', 'Кукушкина', '+75598887766', '2001-08-07', 'HAIRDRESSER'),
       ('Евгения', 'Матрёшкина', '+79445554411', '2002-05-19', 'NAILMASTER'),
       ('Инна', 'Поварёнкина', '+79995554433', '2003-07-13', 'ADMIN'),
       ('Екатерина', 'Щеглова', '+79545554412', '2002-05-19', 'CLEANING');

insert into consumables (title, unit, price)
values ('Краска', 'Штука', 255.7),
       ('Полотенце', 'Штука', 10),
       ('Простыня', 'Штука', 20),
       ('Крем для солярия', 'Штука', 120),
       ('Оксидант1', 'Бутылка', 179.9),
       ('Оксидант2', 'Бутылка', 189.9),
       ('Оксидант3', 'Бутылка', 199.9);
