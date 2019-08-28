create table roles(
    id int primary key auto_increment,
    role enum('GUEST', 'USER', 'ADMIN') unique not null
) ENGINE=InnoDB CHARSET=utf8;

create table users(
    id int primary key auto_increment,
    `name` varchar(255) not null,
    id_role int not null,
    foreign key(id_role) references roles(id)
) ENGINE=InnoDB CHARSET=utf8;