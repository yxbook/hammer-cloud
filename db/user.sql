--hc_userimage 认证图片表变更
ALTER TABLE `hammerchain`.`hc_userimage`
  CHANGE `name1` `full_photo` VARCHAR (255) CHARSET utf8 COLLATE utf8_general_ci NULL COMMENT '正面照名字',
  CHANGE `name2` `reverse_photo` VARCHAR (255) CHARSET utf8 COLLATE utf8_general_ci NULL COMMENT '反面照片名字',
  CHANGE `status` `status` INT (2) DEFAULT 0 NULL COMMENT '审核状态--0.未审核 1.通过 2.未通过3.实名认证未通过',
  ADD COLUMN `alipay_account` VARCHAR (255) NULL COMMENT '支付宝账号' AFTER `status`,
  ADD COLUMN `alipay_photo` VARCHAR (255) NULL COMMENT '支付宝的收款码照片' AFTER `alipay_account`,
  ADD COLUMN `wechat_account` VARCHAR (255) NULL COMMENT '微信账号' AFTER `alipay_photo`,
  ADD COLUMN `wechat_photo` VARCHAR (255) NULL COMMENT '微信的收款码照片' AFTER `wechat_account`;

--hc_account表变更
ALTER TABLE `hammerchain`.`hc_account`
ADD COLUMN `realnam_info` VARCHAR (500) NULL COMMENT '实名认证未通过的原因信息'

ALTER TABLE `hammerchain`.`hc_account`
ADD COLUMN `cnt` DOUBLE (18, 10) NULL COMMENT '用户拥有cnt'



ALTER TABLE `hammerchain`.`hc_userimage` CHANGE `time` `time` TIMESTAMP NULL COMMENT '身份认证时间',
CHANGE `status` `status` INT(2) DEFAULT 0 NULL COMMENT '0、未支付认证；1、已支付认证', ADD COLUMN `pay_cert_time`
TIMESTAMP NULL COMMENT '支付认证时间' AFTER `wechat_photo`;

/*created by huangs on 2018-09-21*/
INSERT INTO `hc_points_class` (`name`, `describe`) VALUES ('微信绑定', '微信绑定（15积分）');
INSERT INTO `hc_points_class` (`name`, `describe`) VALUES ('支付宝绑定', '支付宝绑定（15积分）');

