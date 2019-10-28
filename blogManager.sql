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
`userid` int not null,
`roleid` int not null,
primary key(`userid`,`roleid`),
foreign key (`userid`) references `user`(`id`),
foreign key (`roleid`) references `role`(`id`));

insert into `user`(`id`,`username`,`password`,`enabled`)
    values(1,"admin", "password", true),
        (2,"user","password",true);

insert into `role`(`id`,`role`)
    values(1,"ROLE_ADMIN"), (2,"ROLE_USER");
    
insert into `user_role`(`userid`,`roleid`)
    values(1,1),(1,2),(2,2);

create table blogpost(
`id` int primary key auto_increment,
title varchar(60) not null, 
body text not null,
`date` dateTime not null,
expirationdate date,
approved boolean not null,
`userid` int not null,
foreign key (`userid`) references `user`(`id`)
);

create table tag(
`id` int primary key auto_increment,
name varchar(30) not null
);

create table blogpost_tag(
blogpostid int not null,
tagid int not null,
primary key (blogpostid, tagid),
foreign key (blogpostid) references blogpost(id),
foreign key (tagid) references tag(id)
);

create table staticpage (
`id` int primary key auto_increment,
title varchar(60) not null, 
body text not null
);

update user set password = '$2a$10$PPpQx1AfFn2Rmp4bGxv5u.AgUPnpUCq6iGJ4.eCboXek0z/Mk3iYW' where id = 1;
update user set password = '$2a$10$PPpQx1AfFn2Rmp4bGxv5u.AgUPnpUCq6iGJ4.eCboXek0z/Mk3iYW' where id = 2;
