insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, created_date, last_modified_date, deleted)
values(6, 1, 1, '테스트미팅', '공지사항', current_date, '14:00', '16:00', '구글 밋', current_timestamp, current_timestamp, false);
insert into agenda(agenda_id, meeting_id, created_date, last_modified_date, name, agenda_order)
values(2, 6, current_timestamp, current_timestamp, '테스트아젠다2', 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(18, 6, 1, 'UNKNOWN', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(19, 6, 2, 'UNKNOWN', current_timestamp, current_timestamp, 1);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(20, 6, 3, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(21, 6, 4, 'ABSENT', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(22, 6, 5, 'ATTEND', current_timestamp, current_timestamp, 1);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, created_date, last_modified_date, deleted)
values(7, 1, 1, '테스트미팅2', '공지사항', current_date, '17:00', '18:00', '구글 밋', current_timestamp, current_timestamp, false);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(23, 7, 1, 'ATTEND', current_timestamp, current_timestamp, 0);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(24, 7, 2, 'ATTEND', current_timestamp, current_timestamp, 1);

insert into meeting(meeting_id, workspace_id, team_id, name, purpose, start_date, start_time, end_time, place, created_date, last_modified_date, deleted)
values(8, 1, 1, '지우기전용 데이터', '지우기전용 데이터', current_date, '14:00', '16:00', '구글 밋', current_timestamp, current_timestamp, false);
insert into attendee(attendee_id, meeting_id, workspace_user_id, attend_status, created_date, last_modified_date, is_meeting_admin)
values(25, 8, 2, 'ATTEND', current_timestamp, current_timestamp, 1);
