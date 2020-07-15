create database ibike;

use ibike;

--status     ->    0未启用  1启用但未解锁  2. 开锁使用中   3.报修
--bid         ->  编号    auto_increment     ->   系统集群唯一
create table bike(
   bid bigint primary key auto_increment,
   status int default 0,
   qrcode varchar(100) default '',
   latitude double ,
   longitude double
);