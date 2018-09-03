drop table if exists fm_product_info;CREATE TABLE `fm_product_info` (  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',  `product_no` varchar(20) NOT NULL COMMENT '商品编号',  `product_name` varchar(64) NOT NULL COMMENT '商品名称',  `prodcut_price` decimal(8,2) NOT NULL COMMENT '商品单价',  `prodcut_stock` decimal(18,10) NOT NULL COMMENT '库存',  `prodcut_icon` varchar(100) DEFAULT NULL COMMENT '商品图标',  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '商品状态0、新建状态; 1、正常; 2、下架; -1、删除',  `category_type` tinyint(4) NOT NULL COMMENT '类目类型',  `user_id` int(20) NOT NULL COMMENT '商品创建人',  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',  `product_detail` varchar(500) DEFAULT NULL COMMENT '商品描述',  `product_type` tinyint(4) DEFAULT NULL COMMENT '商品类型1、卖出;2、买入',  `currency` tinyint(4) DEFAULT NULL COMMENT '币种',  `max_price` decimal(8,2) DEFAULT NULL COMMENT '最高可成交的交个',  `min_limit` decimal(18,2) DEFAULT NULL COMMENT '每一笔交易额的最小限额',  `max_limit` decimal(18,2) DEFAULT NULL COMMENT '每一笔交易额的最大限额',  `payment_term` tinyint(10) DEFAULT NULL COMMENT '付款期限',  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;drop table if exists fm_order_info;CREATE TABLE `fm_order_info` (  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',  `order_no` varchar(20) NOT NULL COMMENT '订单编号',  `buyer_id` int(20) NOT NULL COMMENT '买家ID',  `payment` decimal(18,2) DEFAULT NULL COMMENT '买家支付总金额',  `payment_type` tinyint(4) DEFAULT NULL COMMENT '买家支付方式1、微信; 2、支付宝;',  `order_status` tinyint(4) DEFAULT '0' COMMENT '订单状态0、未付款; 1、已付款(付款确认); 2、订单取消; 3、交易成功',  `payment_time` timestamp NULL DEFAULT NULL COMMENT '支付时间',  `end_time` timestamp NULL DEFAULT NULL COMMENT '交易完成时间',  `create_time` timestamp NULL DEFAULT NULL COMMENT '下单时间',  `update_time` timestamp NULL DEFAULT NULL COMMENT '订单修改时间',  `leave_msg` varchar(500) DEFAULT NULL COMMENT '下单留言',  `is_pay` tinyint(4) DEFAULT '0' COMMENT '付款确认0、未付款确认;1、确认已付款',  PRIMARY KEY (`id`)) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;