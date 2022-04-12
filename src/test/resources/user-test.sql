insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name, deleted)
values(15, 'test15@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '고두팔', false);

insert into user(user_id, email, role, user_provider, created_date, last_modified_date, name, deleted)
values(16, 'test16@test.com', 'USER', 'KAKAO', current_timestamp, current_timestamp, '김삭제', false);
insert into workspace_user(workspace_user_id, user_id, workspace_id, team_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position, deleted)
values(16, 16, 1, 3, current_timestamp, current_timestamp, false, '삭제하기', '', '삭제하기', false);
