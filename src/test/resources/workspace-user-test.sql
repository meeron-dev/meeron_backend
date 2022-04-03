insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name, deleted)
values(9, 'test9@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테스투우', false);
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position, deleted)
values(9, 9, 1, null, current_timestamp, current_timestamp, false, '테에스투', '', '퉤수튜', false);

insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name, deleted)
values(10, 'test10@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테스투웃', false);
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position, deleted)
values(10, 10, 1, null, current_timestamp, current_timestamp, false, '테에슽', '', '퉤웨수튜', false);

insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name, deleted)
values(11, 'test11@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테슈튜', false);
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position, deleted)
values(11, 11, 1, 3, current_timestamp, current_timestamp, false, '나가야함', '', '나가야함', false);
