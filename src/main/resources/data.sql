insert into workspace(workspace_id, created_date, last_modified_date, name, workspace_logo_url)
values(1, current_timestamp, current_timestamp, '4tune', null);

insert into user(user_id, email, role, user_provider, created_date, last_modified_date)
values(1, 'test1@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp);
insert into user(user_id, email, role, user_provider, created_date, last_modified_date)
values(2, 'test2@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp);
insert into user(user_id, email, role, user_provider, created_date, last_modified_date)
values(3, 'test3@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp);
insert into user(user_id, email, role, user_provider, created_date, last_modified_date)
values(4, 'test4@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp);
insert into user(user_id, email, role, user_provider, created_date, last_modified_date)
values(5, 'test5@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp);

insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(1, 1, 1, current_timestamp, current_timestamp, false, '무무', null, 'Server / PM');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(2, 2, 1, current_timestamp, current_timestamp, true, '네코', null, 'Planner');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(3, 3, 1, current_timestamp, current_timestamp, false, '제로', null, 'Android');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(4, 4, 1, current_timestamp, current_timestamp, false, '주미', null, 'iOS');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(5, 5, 1, current_timestamp, current_timestamp, false, '미소', null, 'Designer');
