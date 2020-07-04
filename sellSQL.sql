`open_user`/*
SQLyog Ultimate v12.08 (64 bit)
MySQL - 5.7.25-0ubuntu0.18.04.2 : Database - sell
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`sell` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `sell`;

/*Table structure for table `open_user` */

DROP TABLE IF EXISTS `open_user`;

CREATE TABLE `open_user` (
  `openid` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `address` varchar(128) NOT NULL,
  `phone` varchar(32) NOT NULL,
  `money` double DEFAULT NULL,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `open_user` */

insert  into `open_user`(`openid`,`username`,`address`,`phone`,`money`,`create_time`) values ('1','杨优秀','安阳','18888888',999999,'2020-03-27 13:28:23'),('2','杨不优秀','北京','12134214214',999999.1,'2020-03-27 14:36:57'),('3','杨狐狸','西安','18868822111',NULL,'2020-03-27 15:10:41');

/*Table structure for table `order_detail` */

DROP TABLE IF EXISTS `order_detail`;

CREATE TABLE `order_detail` (
  `detail_id` varchar(32) NOT NULL,
  `order_id` varchar(32) NOT NULL,
  `product_id` varchar(32) NOT NULL,
  `product_name` varchar(64) NOT NULL,
  `product_price` decimal(8,2) NOT NULL,
  `product_quantity` int(11) NOT NULL COMMENT '商品数量',
  `product_icon` varchar(512) DEFAULT NULL COMMENT '商品图片',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`detail_id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单详情表';

/*Data for the table `order_detail` */

insert  into `order_detail`(`detail_id`,`order_id`,`product_id`,`product_name`,`product_price`,`product_quantity`,`product_icon`,`create_time`,`update_time`) values ('1585122597722103077','158512259761739509','1','cookie','12.00',2,NULL,'2020-03-25 15:49:57','2020-03-25 15:49:57'),('158512259776041492','158512259761739509','2','苹果','9.90',4,NULL,'2020-03-25 15:49:57','2020-03-25 15:49:57'),('158520261899090679','158520261867863993','1','cookie','12.00',2,NULL,'2020-03-26 14:03:39','2020-03-26 14:03:39'),('158520266213763365','1585202662071100237','1','cookie','12.00',2,NULL,'2020-03-26 14:04:22','2020-03-26 14:04:22'),('158520271769774446','158520271758529325','1','cookie','12.00',2,NULL,'2020-03-26 14:05:17','2020-03-26 14:05:17'),('158520279038878920','158520279038443414','2','苹果','9.90',2,NULL,'2020-03-26 14:06:30','2020-03-26 14:06:30');

/*Table structure for table `order_master` */

DROP TABLE IF EXISTS `order_master`;

CREATE TABLE `order_master` (
  `order_id` varchar(32) NOT NULL,
  `buyer_name` varchar(32) NOT NULL,
  `buyer_phone` varchar(32) NOT NULL,
  `buyer_address` varchar(128) NOT NULL,
  `buyer_openid` varchar(64) NOT NULL COMMENT '买家微信id',
  `order_amount` decimal(8,2) NOT NULL COMMENT '订单总金额',
  `order_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '订单状态，默认0：新下单',
  `pay_status` tinyint(3) NOT NULL DEFAULT '0' COMMENT '支付状态,默认0：未支付',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`order_id`),
  KEY `idx_buyer_openid` (`buyer_openid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

/*Data for the table `order_master` */

insert  into `order_master`(`order_id`,`buyer_name`,`buyer_phone`,`buyer_address`,`buyer_openid`,`order_amount`,`order_status`,`pay_status`,`create_time`,`update_time`) values ('158512259761739509','杨优秀','18888888','安阳','1','63.60',1,1,'2020-03-25 15:49:57','2020-03-28 10:37:18'),('158520261867863993','杨优秀','18868822111','安阳','1','24.00',1,0,'2020-03-26 14:03:39','2020-03-28 10:30:42'),('1585202662071100237','杨优秀','18868822111','安阳','1','24.00',1,1,'2020-03-26 14:04:22','2020-03-28 10:30:30'),('158520271758529325','杨优秀','18868822111','安阳','1','24.00',1,0,'2020-03-26 14:05:17','2020-03-28 10:28:05');

/*Table structure for table `product_category` */

DROP TABLE IF EXISTS `product_category`;

CREATE TABLE `product_category` (
  `category_id` int(11) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(64) NOT NULL,
  `category_type` int(11) NOT NULL COMMENT '类目编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`category_id`),
  UNIQUE KEY `uqe_category_type` (`category_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='类目表';

/*Data for the table `product_category` */

insert  into `product_category`(`category_id`,`category_name`,`category_type`,`create_time`,`update_time`) values (1,'aaa',3,'2020-03-24 14:49:49','2020-03-24 14:49:49'),(2,'水果',6,'2020-03-24 14:54:53','2020-03-24 14:54:53');

/*Table structure for table `product_info` */

DROP TABLE IF EXISTS `product_info`;

CREATE TABLE `product_info` (
  `product_id` varchar(32) NOT NULL,
  `product_name` varchar(64) NOT NULL,
  `product_price` decimal(8,2) NOT NULL,
  `product_stock` int(11) NOT NULL,
  `product_description` varchar(64) DEFAULT NULL,
  `product_icon` varchar(512) DEFAULT NULL,
  `category_type` int(11) NOT NULL COMMENT '类目编号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `product_status` tinyint(3) DEFAULT '0',
  PRIMARY KEY (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

/*Data for the table `product_info` */

insert  into `product_info`(`product_id`,`product_name`,`product_price`,`product_stock`,`product_description`,`product_icon`,`category_type`,`create_time`,`update_time`,`product_status`) values ('1','cookie','12.00',62,'deliciece',NULL,6,'2020-03-24 16:29:34','2020-03-28 09:40:15',0),('2','苹果','9.90',1009,'这是一个大大大大大大',NULL,6,'2020-03-24 19:04:53','2020-03-26 21:32:51',0);

/*Table structure for table `seller_info` */

DROP TABLE IF EXISTS `seller_info`;

CREATE TABLE `seller_info` (
  `id` varchar(32) NOT NULL,
  `username` varchar(32) NOT NULL,
  `password` varchar(32) NOT NULL,
  `openid` varchar(64) NOT NULL COMMENT '微信openid',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `seller_info` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
