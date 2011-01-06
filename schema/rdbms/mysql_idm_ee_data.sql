USE openiam;

insert into USERS (user_id,first_name, last_name, STATUS, COMPANY_ID  ) values('3010','Help','Desk','ACTIVE','100');
insert into LOGIN(SERVICE_ID, LOGIN, MANAGED_SYS_ID, USER_ID, PASSWORD,RESET_PWD, IS_LOCKED, AUTH_FAIL_COUNT) VALUES('USR_SEC_DOMAIN','helpdesk','0','3010','b83a81d1b3f5f209a70ec02c3d7f7fc5',0,0,0);

INSERT INTO ROLE (ROLE_ID,SERVICE_ID,ROLE_NAME) VALUES ('HELPDESK','USR_SEC_DOMAIN','Help Desk');
INSERT INTO USER_ROLE (USER_ROLE_ID, SERVICE_ID, ROLE_ID, USER_ID, STATUS) VALUES('101','USR_SEC_DOMAIN','HELPDESK','3010','ACTIVE');



insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order) values('ENT_APPS', 'ROOT' ,'Enterprise Applications','Enterprise Applications','', 'en',0);

/* Self Service MENU options */
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order) values('SELFSERVICE', 'ROOT' ,'SELF SERVICE','SELF SERVICE','', 'en',0);

insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('ACCESSCENTER','SELFSERVICE', 'Access Management Center', 'Access Management Center', null, 'en', '1',0);

insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('MANAGEREQ', 'ACCESSCENTER' , 'Manage Requests','Manage Requests','requestList.selfserve', 'en','3',0);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('CREATEREQ','ACCESSCENTER','Create Request', 'Create Request', 'createRequest.selfserve', 'en', '4',0);

insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('SELFCENTER','SELFSERVICE','Self Service Center', 'Self Service Center', null, 'en', '2',0);

insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('DIRECTORY','SELFCENTER','Directory Lookup', 'Directory Lookup', 'pub/directory.do?method=view', 'en', '1',1);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('SELF_REGISTER','SELFCENTER','Self Registration', 'Self Registration', 'pub/selfRegister.selfserve', 'en', '2',1);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('CHNGPSWD','SELFCENTER', 'Change Password', 'Change Password', 'passwordChange.selfserve', 'en', '3',0);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('IDQUEST','SELFCENTER', 'Challenge Response', 'Challenge Response', 'identityQuestion.selfserve', 'en', '4',0);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('FORGOTPSWD','SELFCENTER', 'Forgot Password', 'Forgot Password', 'pub/unlockUser.selfserve', 'en', '5',1);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('PROFILE','SELFCENTER', 'Edit Your Profile', 'Edit Your Profile', 'profile.selfserve', 'en', '6',0);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, display_order, PUBLIC_URL) values('OUTOFOFFICE','SELFCENTER', 'Out of Office', 'Out of Office', 'outofoffice.selfserve', 'en', '7',0);


insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('BATCH_PROC','ADMIN','Batch Processes','Batch Processes','batchList.cnt', 'en',6);
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('BATCH_PROC','SUPER_SEC_ADMIN','IDM');


/* Delegated Admin - User manager and Help desk */
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER, PUBLIC_URL) values('SELF_QUERYUSER','ACCESSCENTER','Manage User','Manage User','idman/userSearch.do?action=view', 'en',1,0);

insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('SELF_USERSUMMARY','SELF_QUERYUSER','User Details','User Details','editUser.selfserve', 'en',1);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('SELF_USERIDENTITY','SELF_QUERYUSER','Identities','User Identities','userIdentity.selfserve', 'en',2);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('SELF_USERGROUP','SELF_QUERYUSER','Group','User Groups','userGroup.selfserve', 'en',3);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('SELF_USERROLE','SELF_QUERYUSER','Role','User Role','userRole.selfserve', 'en',4);
insert into MENU (menu_id, menu_group, menu_name,menu_desc,url,LANGUAGE_CD, DISPLAY_ORDER) values('SELF_USERPSWDRESET','SELF_QUERYUSER','Password Reset','Password Reset','resetPassword.selfserve', 'en',7);



INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFSERVICE','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('ACCESSCENTER','END_USER','USR_SEC_DOMAIN');


INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_REGISTER','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('MANAGEREQ','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFCENTER','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('DIRECTORY','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CHNGPSWD','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('IDQUEST','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('FORGOTPSWD','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('PROFILE','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('REPORTINC','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CONTADMIN','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CREATEREQ','END_USER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('OUTOFOFFICE','END_USER','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFSERVICE','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('ACCESSCENTER','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('NEWHIRE','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('MANAGEREQ','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFCENTER','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('DIRECTORY','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CHNGPSWD','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('IDQUEST','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('FORGOTPSWD','HR','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('PROFILE','HR','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CREATEREQ','HR','USR_SEC_DOMAIN');


INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFSERVICE','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('ACCESSCENTER','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('MANAGEREQ','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFCENTER','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('DIRECTORY','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CHNGPSWD','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('IDQUEST','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('FORGOTPSWD','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('PROFILE','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('REPORTINC','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CONTADMIN','MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CREATEREQ','MANAGER','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERSUMMARY','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERIDENTITY','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERGROUP','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERROLE','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERPSWDRESET','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_QUERYUSER','SECURITY_MANAGER','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFSERVICE','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('ACCESSCENTER','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('MANAGEREQ','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFCENTER','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('DIRECTORY','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CHNGPSWD','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('IDQUEST','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('FORGOTPSWD','SECURITY_MANAGER','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('PROFILE','SECURITY_MANAGER','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CREATEREQ','SECURITY_MANAGER','USR_SEC_DOMAIN');



INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERSUMMARY','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERIDENTITY','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERGROUP','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERROLE','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_USERPSWDRESET','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELF_QUERYUSER','HELPDESK','USR_SEC_DOMAIN');

INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFSERVICE','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('ACCESSCENTER','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('MANAGEREQ','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('SELFCENTER','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('DIRECTORY','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CHNGPSWD','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('IDQUEST','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('FORGOTPSWD','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('PROFILE','HELPDESK','USR_SEC_DOMAIN');
INSERT INTO PERMISSIONS(MENU_ID,ROLE_ID, SERVICE_ID) VALUES('CREATEREQ','HELPDESK','USR_SEC_DOMAIN');

/* Batch Tasks */
insert into BATCH_CONFIG(TASK_ID, TASK_NAME, FREQUENCY_UNIT_OF_MEASURE, ENABLED, TASK_URL, EXECUTION_ORDER) VALUES('100','ACCOUNT_LOCKED_NOTIFICATION', 'MINUTE','0','batch/accountLockedNotify.groovy','2');
insert into BATCH_CONFIG(TASK_ID, TASK_NAME, FREQUENCY_UNIT_OF_MEASURE, ENABLED, TASK_URL, EXECUTION_ORDER) VALUES('101','INACTIVE_USER', 'NIGHTLY','0','batch/inactiveUser.groovy','1');
insert into BATCH_CONFIG(TASK_ID, TASK_NAME, FREQUENCY_UNIT_OF_MEASURE, ENABLED, TASK_URL, EXECUTION_ORDER) VALUES('102','PASSWORD_NEAR_EXP', 'NIGHTLY','0','batch/passwordExpirationNotification.groovy','2');
insert into BATCH_CONFIG(TASK_ID, TASK_NAME, FREQUENCY_UNIT_OF_MEASURE, ENABLED, TASK_URL, EXECUTION_ORDER) VALUES('103','ACCOUNT_TERMINATED', 'NIGHTLY','0','batch/terminateOnExpiration.groovy','3');
insert into BATCH_CONFIG(TASK_ID, TASK_NAME, FREQUENCY_UNIT_OF_MEASURE, ENABLED, TASK_URL, EXECUTION_ORDER) VALUES('104','AUTO_UNLOCK', 'MINUTE','0','batch/autoUnlock.groovy','1');

