INSERT INTO `system_business` VALUES ('7', '闪推服务', '闪推服务', 'fms', '7',NOW(), NOW());

INSERT INTO `system_resource` VALUES ('18', 'MOUDLE', 'FMS', '闪推', NULL, '1', 'nav-ico-mms0', '0', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('19', 'MOUDLE', 'FMS', '闪推', NULL, '1', 'nav-ico-mms0', '0', 'CLIENT', NOW(), '0', 'fms');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '18');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '19');
INSERT INTO `system_resource`  VALUES ('1801', 'NAV', 'FMS_REPORTED', '报备管理', NULL, '1', 'nav-ico-21', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1802', 'NAV', 'FMS_MESSAGE', '发送管理', NULL, '2', 'nav-ico-3', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1803', 'NAV', 'FMS_SERVICECODE', '服务号管理', NULL, '3', 'nav-ico-10', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1804', 'NAV', 'FMS_ACCOUNT', '账务管理', NULL, '4', 'nav-ico-22', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1805', 'NAV', 'FMS_CHANNEL', '通道管理', NULL, '5', 'nav-ico-5', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1806', 'NAV', 'FMS_BASE', '基础数据', NULL, '6', 'nav-ico-7', '18', 'MANAGE', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1807', 'NAV', 'FMS_REPORT', '报表管理', NULL, '7', 'nav-ico-9', '18', 'MANAGE', NOW(), '0', 'fms');

INSERT INTO `system_resource`  VALUES ('1901', 'NAV', 'FMS_CLINT_REPORTED', '报备管理', NULL, '1', 'nav-ico-21', '19', 'CLIENT', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1902', 'NAV', 'FMS_CLINT_MESSAGE', '发送管理', NULL, '2', 'nav-ico-3', '19', 'CLIENT', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1903', 'NAV', 'FMS_CLINT_SERVICECODE', '服务号管理', NULL, '3', 'nav-ico-10', '19', 'CLIENT', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1904', 'NAV', 'FMS_CLINT_ACCOUNT', '账务管理', NULL, '4', 'nav-ico-22', '19', 'CLIENT', NOW(), '0', 'fms');
INSERT INTO `system_resource`  VALUES ('1905', 'NAV', 'FMS_CLINT_REPORT', '报表管理', NULL, '5', 'nav-ico-9', '19', 'CLIENT', NOW(), '0', 'fms');

INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1801');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1802');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1803');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1804');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1805');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1806');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '1807');
INSERT INTO `system_resource` VALUES ('180101', 'PAGE', 'FMS_REPORTED_AUDIT', '信息审核管理', '/fms/reported/audit', '1', NULL, '1801', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180102', 'PAGE', 'FMS_REPORTED_QUERY', '报备结果查询', '/fms/reported/query', '2', NULL, '1801', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180201', 'PAGE', 'FMS_MESSAGE_BATCH', '批次查询', '/fms/message/batch', '1', NULL, '1802', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180202', 'PAGE', 'FMS_MESSAGE_DETAIL', '发送明细', '/fms/message/detail', '2', NULL, '1802', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180301', 'PAGE', 'FMS_SERVICECODE_MANAGE', '服务号', '/fms/servicecode/manage', '1', NULL, '1803', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180401', 'PAGE', 'FMS_ACCOUNT_BALANCE', '账务查询', '/fms/account/balance', '1', NULL, '1804', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180402', 'PAGE', 'FMS_ACCOUNT_DETAIL', '充值扣费明细', '/fms/account/detail', '2', NULL, '1804', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180501', 'PAGE', 'FMS_CHANNEL_MANAGE', '通道配置', '/fms/channel/manage', '1', NULL, '1805', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180601', 'PAGE', 'FMS_BASE_BUSINESS', '业务类型', '/fms/base/business', '1', NULL, '1806', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180602', 'PAGE', 'FMS_BASE_BLACKLISTGLOBAL', '黑名单', '/fms/base/blacklistglobal', '2', NULL, '1806', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180603', 'PAGE', 'FMS_BASE_BLACKDICTIONARY', '黑字典', '/fms/base/blackdictionary', '3', NULL, '1806', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180701', 'PAGE', 'FMS_REPORT_CONSUMPTION', '服务号消费统计', '/fms/report/consumption', '1', NULL, '1807', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('180702', 'PAGE', 'FMS_REPORT_CONTENTTYPE', '内容类别发送统计', '/fms/report/contenttype', '2', NULL, '1807', 'MANAGE',  NOW(), '0', 'fms');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180101');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180102');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180201');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180202');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180301');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180401');  
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180402');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180501');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180601');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180602');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180603');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180701');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('1', '180702');

INSERT INTO `system_resource` VALUES ('190101', 'PAGE', 'FMS_CLINT_REPORTED_CONTENT', '内容报备', '/fms/client/reportedcontent', '1', NULL, '1901', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190102', 'PAGE', 'FMS_CLINT_REPORTED_QUERY', '报备结果查询', '/fms/client/reportedquery', '2', NULL, '1901', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190201', 'PAGE', 'FMS_CLINT_MESSAGE_SEND', '闪推发送', '/fms/client/messagesend', '1', NULL, '1902', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190202', 'PAGE', 'FMS_CLINT_MESSAGE_BATCH', '批次查询', '/fms/client/messagebatch', '2', NULL, '1902', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190203', 'PAGE', 'FMS_CLINT_MESSAGE_DETATIL', '发送记录', '/fms/client/messagedetail', '3', NULL, '1902', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190301', 'PAGE', 'FMS_CLINT_SERVICECODE_MANAGE', '服务号', '/fms/client/servicecode', '1', NULL, '1903', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190401', 'PAGE', 'FMS_CLINT_ACCOUNT_QUERY', '账务查询', '/fms/client/account', '1', NULL, '1904', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190501', 'PAGE', 'FMS_CLINT_REPORT_CONSUMPTION', '服务号消费统计', '/fms/client/reportconsumption', '1', NULL, '1905', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_resource` VALUES ('190502', 'PAGE', 'FMS_CLINT_REPORT_CONTENTTYPE', '内容类别发送统计', '/fms/client/reportcontenttype', '2', NULL, '1905', 'CLIENT',  NOW(), '0', 'fms');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '1901');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '1902');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '1903');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '1904');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '1905');

INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190101');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190102');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190201');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190202');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190203');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190301');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190401');  
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190501');
INSERT INTO `system_role_resource_assign` (`system_role_id`, `system_resource_id`) VALUES ('2', '190502');

update system_resource set resource_code='FMS_SERVICECODE' where id=1803;
update system_resource set resource_code='FMS_CLINT_SERVICECODE' where id=1903;
update system_resource set resource_code='FMS_SERVICECODE_MANAGE',resource_path='/fms/servicecode/manage' where id=180301;
update system_resource set resource_code='FMS_CLINT_SERVICECODE_MANAGE',resource_path='/fms/client/servicecode'  where id=190301;


update system_resource set resource_name='账务管理' where id=1804;
update system_resource set resource_name='服务号' where id=180301;
update system_resource set resource_name='账务查询' where id=180401;
update system_resource set resource_name='通道配置' where id=180501;

update system_resource set resource_name='批次查询' where id=190202;
update system_resource set resource_name='发送记录' where id=190203;
update system_resource set resource_name='服务号' where id=190301;

update system_resource set resource_name='账务管理' where id=1904;
update system_resource set resource_name='账务查询' where id=190401;

update  system_resource set resource_icon ='nav-ico-21' where id =1801;
update  system_resource set resource_icon ='nav-ico-3' where id =1802;
update  system_resource set resource_icon ='nav-ico-10' where id =1803;
update  system_resource set resource_icon ='nav-ico-22' where id =1804;
update  system_resource set resource_icon ='nav-ico-5' where id =1805;
update  system_resource set resource_icon ='nav-ico-7' where id =1806;
update  system_resource set resource_icon ='nav-ico-9' where id =1807;
update  system_resource set resource_icon ='nav-ico-21' where id =1901;
update  system_resource set resource_icon ='nav-ico-3' where id =1902;
update  system_resource set resource_icon ='nav-ico-10' where id =1903;
update  system_resource set resource_icon ='nav-ico-22' where id =1904;
update  system_resource set resource_icon ='nav-ico-9' where id =1905;

update system_resource set resource_icon='nav-ico-30' where id=18;  
update system_resource set resource_icon='nav-ico-30' where id=19;  
