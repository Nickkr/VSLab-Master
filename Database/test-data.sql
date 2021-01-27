-- Users
insert into user.user (`name`, `lastname`, `password`, `username`, `role`) values ('admin', 'admin', 'admin', 'admin', 1);
insert into user.user (`name`, `lastname`, `password`, `username`, `role`) values ('user', 'user', 'user', 'user', 0);
insert into user.user (`name`, `lastname`, `password`, `username`, `role`) values ('foo', 'bar', 'admin', 'foo-admin', 1);
insert into user.user (`name`, `lastname`, `password`, `username`, `role`) values ('foo', 'bar', 'user', 'foo-user', 0);

-- Categories
insert into category.category (`id`, `name`) values (1, 'Obst');
insert into category.category (`id`, `name`) values (2, 'Gemüse');

-- Products
insert into product.product (`details`, `name`, `price`, `category_id`) values ('This is a test detail.', 'Apfel', 2.0, 1);
insert into product.product (`details`, `name`, `price`, `category_id`) values ('This is a test detail.', 'Kiwi', 3.0, 1);
insert into product.product (`details`, `name`, `price`, `category_id`) values ('This is a test detail.', 'Banane', 1.0, 1);
insert into product.product (`details`, `name`, `price`, `category_id`) values ('This is a test detail.', 'Karotte', 1.0, 2);
