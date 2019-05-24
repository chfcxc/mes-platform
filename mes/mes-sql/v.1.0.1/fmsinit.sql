-- --------------------------------------------------------
-- 主机:                           100.100.10.91
-- 服务器版本:                        5.6.30 - Source distribution
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  9.5.0.5196
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 导出 fms 的数据库结构
DROP DATABASE IF EXISTS `fms`;
CREATE DATABASE IF NOT EXISTS `fms` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `fms`;

-- 导出  表 fms.fms_account 结构
DROP TABLE IF EXISTS `fms_account`;
CREATE TABLE IF NOT EXISTS `fms_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT '客户id',
  `service_code_id` bigint(20) DEFAULT NULL COMMENT '服务号id',
  `app_id` varchar(64) DEFAULT NULL,
  `balance` bigint(20) DEFAULT NULL COMMENT '余量条数',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='账务表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_account_details 结构
DROP TABLE IF EXISTS `fms_account_details`;
CREATE TABLE IF NOT EXISTS `fms_account_details` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT '	客户id',
  `service_code_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `change_number` bigint(20) NOT NULL,
  `remaining_number` bigint(20) NOT NULL COMMENT '剩余条数',
  `operation_type` int(11) DEFAULT NULL COMMENT '操作类型 0充值 1扣费 2补款',
  `remark` varchar(128) DEFAULT NULL COMMENT '备注',
  `user_id` bigint(20) NOT NULL,
  `operation_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='账务明细表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_batch 结构
DROP TABLE IF EXISTS `fms_batch`;
CREATE TABLE IF NOT EXISTS `fms_batch` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(32) DEFAULT NULL COMMENT '标题',
  `batch_number` varchar(64) DEFAULT NULL COMMENT '批次号',
  `template_id` varchar(64) NOT NULL COMMENT '模板id',
  `template_type` int(4) NOT NULL COMMENT '模板类型：1个性，0普通',
  `content_type_id` bigint(20) DEFAULT NULL COMMENT '内容类型id',
  `content` varchar(255) DEFAULT NULL COMMENT '模板内容',
  `service_code_id` bigint(20) DEFAULT NULL COMMENT '服务号id',
  `app_id` varchar(64) DEFAULT NULL COMMENT 'appid',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `state` int(4) DEFAULT NULL COMMENT '	状态 -1 标识编辑状态 0 待解析 1 解析中 3 发送完成 4 提交数据错误',
  `cmcc_number` int(11) DEFAULT NULL COMMENT '移动发送总数',
  `cucc_number` int(11) DEFAULT NULL COMMENT '联通发送总数',
  `ctcc_number` int(11) DEFAULT NULL COMMENT '电信发送总数',
  `send_total` int(11) DEFAULT NULL,
  `success_total` int(11) DEFAULT NULL COMMENT '成功总数',
  `error_text_number` int(11) DEFAULT NULL COMMENT '内容错误数量',
  `unknown_total` int(11) DEFAULT NULL COMMENT '未识别',
  `repeat_total` int(11) DEFAULT NULL COMMENT '重复总数',
  `user_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='批次表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_blacklist 结构
DROP TABLE IF EXISTS `fms_blacklist`;
CREATE TABLE IF NOT EXISTS `fms_blacklist` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) DEFAULT NULL,
  `remark` varchar(128) DEFAULT NULL,
  `is_delete` int(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `save_unique_index` (`mobile`,`is_delete`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='黑名单';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_black_dictionary 结构
DROP TABLE IF EXISTS `fms_black_dictionary`;
CREATE TABLE IF NOT EXISTS `fms_black_dictionary` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `content` varchar(190) DEFAULT NULL COMMENT '内容',
  `is_delete` tinyint(4) DEFAULT '0' COMMENT '是否已删除  0未删除(默认) 1已删除',
  `last_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(128) DEFAULT NULL COMMENT '说明',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `save_unique_index` (`content`,`is_delete`) USING BTREE
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='黑字典';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_business_type 结构
DROP TABLE IF EXISTS `fms_business_type`;
CREATE TABLE IF NOT EXISTS `fms_business_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父类id',
  `level` int(4) DEFAULT NULL COMMENT '层级',
  `full_path` varchar(32) DEFAULT NULL COMMENT '全路径',
  `save_type` int(2) DEFAULT NULL COMMENT '保存类型 1 保存 2 不保存',
  `user_id` bigint(20) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='业务类型';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_channel 结构
DROP TABLE IF EXISTS `fms_channel`;
CREATE TABLE IF NOT EXISTS `fms_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel_name` varchar(32) DEFAULT NULL COMMENT '通道名称',
  `channel_number` varchar(64) DEFAULT NULL COMMENT '通道号',
  `send_speed` int(10) DEFAULT NULL COMMENT '发送速度',
  `template_type` varchar(64) NOT NULL COMMENT '	类型：1个性，0普通，0,1',
  `business_type_id` bigint(20) DEFAULT NULL COMMENT '业务类型id',
  `providers` varchar(32) DEFAULT NULL COMMENT '允许发送的运营商 1.移动 2.电信 3.联通 例如1#2#3',
  `cmcc_limit` int(10) DEFAULT NULL COMMENT '移动通道字数限制',
  `cucc_limit` int(10) DEFAULT NULL COMMENT '联通通道字数限制',
  `ctcc_limit` int(10) DEFAULT NULL COMMENT '电信通道字数限制',
  `is_need_report` int(4) DEFAULT NULL COMMENT '是否需要报备 0 不需要1需要报备',
  `report_type` int(4) DEFAULT NULL COMMENT '报备类型 0线上报备线上回模板ID，1线上报备线下回模板ID，2线下报备线上回模板ID，3线下报备线下回模板ID',
  `state` int(4) DEFAULT NULL COMMENT '状态 0 停用1启用',
  `create_time` datetime DEFAULT NULL COMMENT '生成日期',
  `user_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='通道表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_channel_info 结构
DROP TABLE IF EXISTS `fms_channel_info`;
CREATE TABLE IF NOT EXISTS `fms_channel_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `channel_id` bigint(20) DEFAULT NULL COMMENT '通道id',
  `propertiey_name` varchar(64) DEFAULT NULL COMMENT '属性',
  `propertiey_value` varchar(64) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `property_key` varchar(256) DEFAULT NULL COMMENT '属性键值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='第三方通道属性表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_enterprise_content_type_day 结构
DROP TABLE IF EXISTS `fms_enterprise_content_type_day`;
CREATE TABLE IF NOT EXISTS `fms_enterprise_content_type_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_type_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业内容类型发送日统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_enterprise_content_type_month 结构
DROP TABLE IF EXISTS `fms_enterprise_content_type_month`;
CREATE TABLE IF NOT EXISTS `fms_enterprise_content_type_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_type_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业内容类型发送月统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_enterprise_content_type_year 结构
DROP TABLE IF EXISTS `fms_enterprise_content_type_year`;
CREATE TABLE IF NOT EXISTS `fms_enterprise_content_type_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `business_type_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业内容类型发送年统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_message 结构
DROP TABLE IF EXISTS `fms_message`;
CREATE TABLE IF NOT EXISTS `fms_message` (
  `id` varchar(64) NOT NULL,
  `custom_fms_id` varchar(128) DEFAULT NULL COMMENT '	客户自定义唯一标识',
  `template_id` varchar(64) NOT NULL COMMENT '模板id',
  `app_id` varchar(64) DEFAULT NULL COMMENT 'appid',
  `mobile` varchar(11) DEFAULT NULL COMMENT '手机号',
  `title` varchar(32) DEFAULT NULL COMMENT '标题',
  `content` varchar(255) DEFAULT NULL COMMENT '内容',
  `send_value` varchar(128) DEFAULT NULL COMMENT '客户提交变量值',
  `service_code_id` bigint(20) DEFAULT NULL COMMENT '服务号id',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT '企业id',
  `batch_number` varchar(64) DEFAULT NULL COMMENT '批次号',
  `state` int(4) DEFAULT NULL COMMENT '状态 1发送中 2发送到运营商 3发送成功 4发送失败 5发送超时',
  `send_type` int(4) DEFAULT NULL COMMENT '发送方式：1、接口；2、页面',
  `template_type` int(4) NOT NULL COMMENT '模板类型：1个性，0普通',
  `user_id` bigint(20) DEFAULT NULL,
  `send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `channel_id` bigint(20) DEFAULT NULL,
  `content_type_id` bigint(20) DEFAULT NULL COMMENT '内容类型id',
  `operator_code` varchar(4) DEFAULT NULL COMMENT '运营商编码',
  `channel_report_state` varchar(64) DEFAULT NULL COMMENT '状态报告状态',
  `channel_report_desc` varchar(64) DEFAULT NULL COMMENT '	状态报告描述',
  `channel_response_time` datetime DEFAULT NULL COMMENT '响应比对完成时间',
  `channel_report_time` datetime DEFAULT NULL COMMENT '网关异步状态报告返回时间',
  `operator_id` varchar(64) DEFAULT NULL COMMENT '运营商id',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='闪信表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_servicecode_consumption_day 结构
DROP TABLE IF EXISTS `fms_servicecode_consumption_day`;
CREATE TABLE IF NOT EXISTS `fms_servicecode_consumption_day` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `servicecode_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号日消费统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_servicecode_consumption_month 结构
DROP TABLE IF EXISTS `fms_servicecode_consumption_month`;
CREATE TABLE IF NOT EXISTS `fms_servicecode_consumption_month` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `servicecode_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号月消费统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_servicecode_consumption_year 结构
DROP TABLE IF EXISTS `fms_servicecode_consumption_year`;
CREATE TABLE IF NOT EXISTS `fms_servicecode_consumption_year` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `servicecode_id` bigint(20) DEFAULT NULL,
  `enterprise_id` bigint(20) DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `total_number` int(20) DEFAULT NULL COMMENT '提交总数',
  `cmcc_number` int(20) DEFAULT NULL,
  `cucc_number` int(20) DEFAULT NULL COMMENT '	联通总数',
  `ctcc_number` int(20) DEFAULT NULL COMMENT '电信总数',
  `unknow_number` int(20) DEFAULT NULL COMMENT '未知总数',
  `success_number` int(20) DEFAULT NULL COMMENT '成功总数',
  `cmcc_success_number` int(20) DEFAULT NULL COMMENT '移动成功总数',
  `cucc_success_number` int(20) DEFAULT NULL COMMENT '联通成功总数',
  `ctcc_success_number` int(20) DEFAULT NULL COMMENT '电信成功总数',
  `fail_number` int(20) DEFAULT NULL COMMENT '失败总数',
  `cmcc_fail_number` int(20) DEFAULT NULL COMMENT '移动失败总数',
  `cucc_fail_number` int(20) DEFAULT NULL COMMENT '联通失败总数',
  `ctcc_fail_number` int(20) DEFAULT NULL COMMENT '电信失败总数',
  `timeout_number` int(20) DEFAULT NULL COMMENT '超时总数',
  `cmcc_timeout_number` int(20) DEFAULT NULL,
  `cucc_timeout_number` int(20) DEFAULT NULL COMMENT '联通超时总数',
  `ctcc_timeout_number` int(20) DEFAULT NULL COMMENT '电信超时总数',
  `report_time` datetime DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='服务号年消费统计';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_service_code 结构
DROP TABLE IF EXISTS `fms_service_code`;
CREATE TABLE IF NOT EXISTS `fms_service_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_code` varchar(64) DEFAULT NULL,
  `app_id` varchar(64) DEFAULT NULL,
  `secret_key` varchar(64) DEFAULT NULL COMMENT '秘钥',
  `state` int(4) DEFAULT NULL COMMENT '状态 0启用1停用',
  `enterprise_id` bigint(20) DEFAULT NULL COMMENT '企业id',
  `business_type_id` bigint(20) DEFAULT NULL COMMENT '业务类型id',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='服务号表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_service_code_channel 结构
DROP TABLE IF EXISTS `fms_service_code_channel`;
CREATE TABLE IF NOT EXISTS `fms_service_code_channel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_code_id` bigint(20) DEFAULT NULL COMMENT '服务号id',
  `app_id` varchar(64) DEFAULT NULL,
  `channel_id` bigint(20) DEFAULT NULL COMMENT '通道id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `operator_code` varchar(32) DEFAULT NULL COMMENT '运营商',
  `template_type` int(4) NOT NULL COMMENT '类型：1个性，0普通',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='路由表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_service_code_param 结构
DROP TABLE IF EXISTS `fms_service_code_param`;
CREATE TABLE IF NOT EXISTS `fms_service_code_param` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` varchar(64) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `get_report_type` int(4) DEFAULT '2',
  `ip_configuration` varchar(64) DEFAULT NULL COMMENT 'ip配置',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='服务号配置参数表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_template 结构
DROP TABLE IF EXISTS `fms_template`;
CREATE TABLE IF NOT EXISTS `fms_template` (
  `id` varchar(64) NOT NULL COMMENT '模板id',
  `content` varchar(255) DEFAULT NULL COMMENT '报备模板内容',
  `variable` varchar(128) DEFAULT NULL COMMENT '报备模板变量',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `user_id` mediumtext,
  `template_type` int(4) DEFAULT NULL COMMENT '模板类型：1个性，0普通',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '生成时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='报备模板表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_template_channel_report 结构
DROP TABLE IF EXISTS `fms_template_channel_report`;
CREATE TABLE IF NOT EXISTS `fms_template_channel_report` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_id` varchar(64) NOT NULL COMMENT '模板id',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `channel_id` bigint(20) NOT NULL COMMENT '通道id',
  `operator_code` varchar(32) DEFAULT NULL COMMENT '运营商编码',
  `state` int(4) DEFAULT NULL COMMENT '移动报备状态：0 提交报备 1 报备中，2 通过 3 拒绝，4 不支持 5 超时',
  `channel_template_id` varchar(64) DEFAULT NULL COMMENT '通道报备id',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='报备模板报备结果表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_template_service_code_assign 结构
DROP TABLE IF EXISTS `fms_template_service_code_assign`;
CREATE TABLE IF NOT EXISTS `fms_template_service_code_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(128) DEFAULT NULL COMMENT '报备模板名称',
  `app_id` varchar(64) DEFAULT NULL,
  `template_id` varchar(128) DEFAULT NULL COMMENT '报备模板id',
  `send_type` int(4) DEFAULT NULL COMMENT '	提交类型：1接口，2页面',
  `audit_state` int(4) DEFAULT NULL COMMENT '审核状态：0 未完成 1 完成',
  `business_type_id` bigint(20) DEFAULT NULL COMMENT '业务类型id',
  `save_type` int(4) DEFAULT NULL COMMENT '保存类型',
  `content_type_id` bigint(20) DEFAULT NULL COMMENT '内容类型',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `user_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 COMMENT='模板与服务号关系表';

-- 数据导出被取消选择。
-- 导出  表 fms.fms_user_service_code_assign 结构
DROP TABLE IF EXISTS `fms_user_service_code_assign`;
CREATE TABLE IF NOT EXISTS `fms_user_service_code_assign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `service_code_id` bigint(20) DEFAULT NULL,
  `last_update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户分配服务号';

alter table fms_batch MODIFY state int(4) DEFAULT NULL COMMENT '	状态 -1 标识编辑状态 0 待解析 1 解析中  3 发送完成 4 提交数据错误';
