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
values(1, 1, 1, current_timestamp, current_timestamp, false, '무무', '', 'Server / PM');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(2, 2, 1, current_timestamp, current_timestamp, true, '네코', '', 'Planner');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(3, 3, 1, current_timestamp, current_timestamp, false, '제로', '', 'Android');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(4, 4, 1, current_timestamp, current_timestamp, false, '주미', '', 'iOS');
insert into workspace_user(workspace_user_id, user_id, workspace_id, created_date, last_modified_date, is_workspace_admin, nickname, profile_image_url, position)
values(5, 5, 1, current_timestamp, current_timestamp, false, '미소', '', 'Designer');

insert into team(team_id, workspace_id, created_date, last_modified_date, name)
values(1, 1, current_timestamp, current_timestamp, '디자인팀');
insert into team_user(team_user_id, workspace_user_id, team_id, created_date, last_modified_date)
values(1, 5, 1, current_timestamp, current_timestamp);

insert into team(team_id, workspace_id, created_date, last_modified_date, name)
values(2, 1, current_timestamp, current_timestamp, '기획팀');
insert into team_user(team_user_id, workspace_user_id, team_id, created_date, last_modified_date)
values(2, 2, 2, current_timestamp, current_timestamp);

insert into team(team_id, workspace_id, created_date, last_modified_date, name)
values(3, 1, current_timestamp, current_timestamp, '개발팀');
insert into team_user(team_user_id, workspace_user_id, team_id, created_date, last_modified_date)
values(3, 1, 3, current_timestamp, current_timestamp);
insert into team_user(team_user_id, workspace_user_id, team_id, created_date, last_modified_date)
values(4, 3, 3, current_timestamp, current_timestamp);
insert into team_user(team_user_id, workspace_user_id, team_id, created_date, last_modified_date)
values(5, 4, 3, current_timestamp, current_timestamp);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, meeting_status, created_date, last_modified_date)
values(1, 1, 1, '1주차 디자인 공지', '공지사항', '2022-02-18', '14:00', '16:00', '구글 밋', 'END', current_timestamp, current_timestamp);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(1, 1, 1, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(2, 1, 2, 'ATTEND', current_timestamp, current_timestamp, 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(3, 1, 3, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(4, 1, 4, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(5, 1, 5, 'ATTEND', current_timestamp, current_timestamp, 1);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, meeting_status, created_date, last_modified_date)
values(2, 1, 2, '1주차 기획 청사진 공지', '공지사항', '2022-02-20', '21:00', '23:00', '구글 밋', 'END', current_timestamp, current_timestamp);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(6, 2, 1, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(7, 2, 2, 'ATTEND', current_timestamp, current_timestamp, 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(8, 2, 3, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(9, 2, 4, 'ATTEND', current_timestamp, current_timestamp, 0);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, meeting_status, created_date, last_modified_date)
values(3, 1, 2, '2주차 기획 청사진 공지', '공지사항', '2022-02-28', '22:00', '23:00', '구글 밋', 'END', current_timestamp, current_timestamp);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(10, 3, 1, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(11, 3, 2, 'ATTEND', current_timestamp, current_timestamp, 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(12, 3, 3, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(13, 3, 4, 'ATTEND', current_timestamp, current_timestamp, 0);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, meeting_status, created_date, last_modified_date)
values(4, 1, 3, '앱 데이터베이스 설계', '논의', '2022-03-01', '22:00', '00:00', '구글 밋', 'END', current_timestamp, current_timestamp);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(14, 4, 1, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(15, 4, 3, 'ATTEND', current_timestamp, current_timestamp, 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(16, 4, 4, 'ATTEND', current_timestamp, current_timestamp, 0);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, meeting_status, created_date, last_modified_date)
values(5, 1, 3, '테스트 회의', '테스트', '2022-03-05', '22:00', '23:00', null, 'END', current_timestamp, current_timestamp);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(17, 5, 1, 'ATTEND', current_timestamp, current_timestamp, 1);

-- AGENDA, ISSUE, AGENDA_FILE
insert into agenda(agenda_id, meeting_id, created_date, last_modified_date, name, agenda_order)
values(1, 5, current_timestamp, current_timestamp, '테스트아젠다1', 1);
insert into issue(issue_id, agenda_id, created_date, last_modified_date, contents)
values(1, 1, current_timestamp, current_timestamp, '테스트이슈1');
insert into agenda_file(agenda_file_id, agenda_id, created_date, last_modified_date, url, origin_file_name, rename_file_name)
values(1, 1, current_timestamp, current_timestamp, 'test-url.com', '테스트사진.jpg', '1234.jpg');
