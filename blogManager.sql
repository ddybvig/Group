drop database if exists blogManager;
Create database blogManager;
 
 use blogManager;
 
create table `user`(
`id` int primary key auto_increment,
`username` varchar(30) not null unique,
`password` varchar(100) not null,
`enabled` boolean not null);

create table `role`(
`id` int primary key auto_increment,
`role` varchar(30) not null
);

create table `user_role`(
`user_id` int not null,
`role_id` int not null,
primary key(`user_id`,`role_id`),
foreign key (`user_id`) references `user`(`id`),
foreign key (`role_id`) references `role`(`id`));

insert into `user`(`id`,`username`,`password`,`enabled`)
    values(1,"admin", "password", true),
        (2,"user","password",true);

insert into `role`(`id`,`role`)
    values(1,"ROLE_ADMIN"), (2,"ROLE_USER");
    
insert into `user_role`(`user_id`,`role_id`)
    values(1,1),(1,2),(2,2);

create table blogPost(
`id` int primary key auto_increment,
title varchar(60) not null, 
body text not null,
`date` dateTime not null,
expirationDate date,
approved boolean not null
);

create table tag(
`id` int primary key auto_increment,
name varchar(30) not null
);

create table blogPost_tag(
blogPost_id int not null,
tag_id int not null,
primary key (blogPost_id, tag_id),
foreign key (blogPost_id) references blogPost(id),
foreign key (tag_id) references tag(id)
);

create table staticPage (
`id` int primary key auto_increment,
title varchar(60) not null, 
body text not null
);