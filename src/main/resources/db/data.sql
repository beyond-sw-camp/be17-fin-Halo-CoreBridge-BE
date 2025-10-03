INSERT INTO user_role(created_at, updated_at, code, name)
VALUES (NOW(), NOW(), 'ROLE_ADMIN', '관리자'),
       (NOW(), NOW(), 'ROLE_APPLICANT', '지원자'),
       (NOW(), NOW(), 'ROLE_INTERVIEWER', '면접관'),
       (NOW(), NOW(), 'ROLE_RECRUITER', '채용 담당자');
