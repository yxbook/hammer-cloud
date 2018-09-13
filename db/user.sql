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
