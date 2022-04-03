insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name)
values(12, 'test12@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테스투우');
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(12, 12, 1, null, current_timestamp, current_timestamp, false, '테에스투', '', '퉤수튜');

insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name)
values(13, 'test130@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테스투웃');
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(13, 13, 1, null, current_timestamp, current_timestamp, false, '테에슽', '', '퉤웨수튜');

insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name)
values(14, 'test14@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '테슈튜');
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(14, 14, 1, 3, current_timestamp, current_timestamp, false, '나가야함', '', '나가야함');
