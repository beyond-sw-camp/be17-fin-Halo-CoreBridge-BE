-- UserRole Data
INSERT INTO user_role (id, code, name, created_at, updated_at) VALUES (1, 'ROLE_USER', '일반 사용자', NOW(), NOW());
INSERT INTO user_role (id, code, name, created_at, updated_at) VALUES (2, 'ROLE_ADMIN', '관리자', NOW(), NOW());

-- Users Data
INSERT INTO users (id, name, email, password, birth, gender, phone, user_role_id, created_at, updated_at) VALUES (1, '김철수', 'kim.chulsoo@example.com', 'password123', '1990-01-15', 'MALE', '010-1234-5678', 1, NOW(), NOW());
INSERT INTO users (id, name, email, password, birth, gender, phone, user_role_id, created_at, updated_at) VALUES (2, '이영희', 'lee.younghee@example.com', 'password123', '1992-05-20', 'FEMALE', '010-9876-5432', 1, NOW(), NOW());
INSERT INTO users (id, name, email, password, birth, gender, phone, user_role_id, created_at, updated_at) VALUES (3, '박관리', 'park.admin@example.com', 'adminpass', '1985-11-01', 'MALE', '010-1111-2222', 2, NOW(), NOW());
INSERT INTO users (id, name, email, password, birth, gender, phone, user_role_id, created_at, updated_at) VALUES (4, '최지혜', 'choi.jihye@example.com', 'password123', '1995-03-10', 'FEMALE', '010-3333-4444', 1, NOW(), NOW());
INSERT INTO users (id, name, email, password, birth, gender, phone, user_role_id, created_at, updated_at) VALUES (5, '정민준', 'jung.minjun@example.com', 'password123', '1988-07-22', 'MALE', '010-5555-6666', 1, NOW(), NOW());

-- JobGroup Data
INSERT INTO job_group (id, name, location, created_at, updated_at) VALUES (1, '개발', '서울', NOW(), NOW());
INSERT INTO job_group (id, name, location, created_at, updated_at) VALUES (2, '디자인', '경기', NOW(), NOW());
INSERT INTO job_group (id, name, location, created_at, updated_at) VALUES (3, '마케팅', '부산', NOW(), NOW());

-- Duty Data
INSERT INTO duty (id, name, job_group_id, created_at, updated_at) VALUES (1, '백엔드 개발', 1, NOW(), NOW());
INSERT INTO duty (id, name, job_group_id, created_at, updated_at) VALUES (2, '프론트엔드 개발', 1, NOW(), NOW());
INSERT INTO duty (id, name, job_group_id, created_at, updated_at) VALUES (3, 'UI/UX 디자인', 2, NOW(), NOW());
INSERT INTO duty (id, name, job_group_id, created_at, updated_at) VALUES (4, '디지털 마케팅', 3, NOW(), NOW());

-- Department Data
INSERT INTO department (id, name, duty_id, created_at, updated_at) VALUES (1, '백엔드 개발팀', 1, NOW(), NOW());
INSERT INTO department (id, name, duty_id, created_at, updated_at) VALUES (2, '프론트엔드 개발팀', 2, NOW(), NOW());
INSERT INTO department (id, name, duty_id, created_at, updated_at) VALUES (3, '디자인팀', 3, NOW(), NOW());
INSERT INTO department (id, name, duty_id, created_at, updated_at) VALUES (4, '마케팅팀', 4, NOW(), NOW());

-- JobPosting Data
INSERT INTO job_posting (id, title, employment_type, career_type, min_experience, max_experience, position_level, location, apply_start_date, apply_end_date, hire_end_date, headcount, summary, responsibilities, requirements, preferred, salary_type, salary_min, salary_max, salary_negotiable, working_hours, benefits, contact_name, contact_email, additional_info, department_id, created_user_id, created_at, updated_at) VALUES (1, '백엔드 개발자 채용', 'FULL_TIME', 'EXPERIENCED', 3, 7, '대리-과장', '서울 강남구', '2025-10-01 09:00:00', '2025-10-31 18:00:00', '2025-11-15 18:00:00', 5, '백엔드 시스템 개발 및 운영', '서비스 API 개발, DB 설계 및 최적화', 'Java, Spring Boot 경험 3년 이상', 'MSA 경험 우대', 'YEARLY', 4000, 6000, FALSE, '주 5일, 10시-19시', '식대 제공, 유연 근무', '김인사', 'hr@example.com', '많은 지원 바랍니다.', 1, 3, NOW(), NOW());
INSERT INTO job_posting (id, title, employment_type, career_type, min_experience, max_experience, position_level, location, apply_start_date, apply_end_date, hire_end_date, headcount, summary, responsibilities, requirements, preferred, salary_type, salary_min, salary_max, salary_negotiable, working_hours, benefits, contact_name, contact_email, additional_info, department_id, created_user_id, created_at, updated_at) VALUES (2, '프론트엔드 개발자 채용', 'FULL_TIME', 'NEW', 0, 0, '사원', '서울 서초구', '2025-10-05 09:00:00', '2025-11-05 18:00:00', '2025-11-20 18:00:00', 3, '프론트엔드 웹 서비스 개발', 'React 기반 UI 개발, 웹 성능 최적화', 'HTML, CSS, JavaScript, React 경험', 'TypeScript 경험 우대', 'YEARLY', 3000, 4500, FALSE, '주 5일, 9시-18시', '커피 무제한, 사내 동호회 지원', '박인사', 'hr2@example.com', '신입 환영합니다.', 2, 3, NOW(), NOW());
INSERT INTO job_posting (id, title, employment_type, career_type, min_experience, max_experience, position_level, location, apply_start_date, apply_end_date, hire_end_date, headcount, summary, responsibilities, requirements, preferred, salary_type, salary_min, salary_max, salary_negotiable, working_hours, benefits, contact_name, contact_email, additional_info, department_id, created_user_id, created_at, updated_at) VALUES (3, 'UI/UX 디자이너 채용', 'FULL_TIME', 'EXPERIENCED', 2, 5, '주니어-시니어', '경기 성남시', '2025-10-10 09:00:00', '2025-11-10 18:00:00', '2025-11-25 18:00:00', 2, '사용자 경험 개선 및 인터페이스 디자인', '와이어프레임, 프로토타입 제작, 디자인 시스템 구축', 'Figma, Sketch, Adobe XD 사용 경험 2년 이상', '애니메이션 디자인 경험 우대', 'YEARLY', 3500, 5500, FALSE, '주 5일, 9시-18시', '최신 장비 지원, 디자인 교육 지원', '최인사', 'hr3@example.com', '포트폴리오 제출 필수', 3, 3, NOW(), NOW());
INSERT INTO job_posting (id, title, employment_type, career_type, min_experience, max_experience, position_level, location, apply_start_date, apply_end_date, hire_end_date, headcount, summary, responsibilities, requirements, preferred, salary_type, salary_min, salary_max, salary_negotiable, working_hours, benefits, contact_name, contact_email, additional_info, department_id, created_user_id, created_at, updated_at) VALUES (4, '디지털 마케터 (경력무관)', 'FULL_TIME', 'ANY', 0, 0, '사원-대리', '부산 해운대구', '2025-10-15 09:00:00', '2025-11-15 18:00:00', '2025-11-30 18:00:00', 4, '온라인 마케팅 전략 수립 및 실행', 'SNS 채널 운영, 광고 캠페인 기획 및 분석', '디지털 마케팅에 대한 이해와 열정', 'GA, SEO 경험 우대', 'MONTHLY', 250, 350, FALSE, '주 5일, 9시-18시', '성과급 지급, 마케팅 교육 지원', '정인사', 'hr4@example.com', '적극적이고 창의적인 인재를 찾습니다.', 4, 3, NOW(), NOW());

-- Room Data
INSERT INTO room (id, name, location, room_type, description) VALUES (1, '면접실 A', '본사 5층', 'OFFLINE', '조용하고 쾌적한 면접실');
INSERT INTO room (id, name, location, room_type, description) VALUES (2, '온라인 면접방 1', '온라인', 'ONLINE', '화상 면접용 가상 공간');
INSERT INTO room (id, name, location, room_type, description) VALUES (3, '면접실 B', '본사 6층', 'OFFLINE', '넓고 편안한 면접실');
INSERT INTO room (id, name, location, room_type, description) VALUES (4, '온라인 면접방 2', '온라인', 'ONLINE', '그룹 화상 면접용 가상 공간');

-- Image Data
INSERT INTO image (id, original_filename, saved_path, content_type, file_size, is_deleted, user_id, created_at, updated_at) VALUES (1, 'profile1.jpg', '/images/profile/1_profile1.jpg', 'image/jpeg', 102400, FALSE, 1, NOW(), NOW());
INSERT INTO image (id, original_filename, saved_path, content_type, file_size, is_deleted, user_id, created_at, updated_at) VALUES (2, 'profile2.png', '/images/profile/2_profile2.png', 'image/png', 204800, FALSE, 2, NOW(), NOW());
INSERT INTO image (id, original_filename, saved_path, content_type, file_size, is_deleted, user_id, created_at, updated_at) VALUES (3, 'profile3.jpg', '/images/profile/3_profile3.jpg', 'image/jpeg', 150000, FALSE, 4, NOW(), NOW());
INSERT INTO image (id, original_filename, saved_path, content_type, file_size, is_deleted, user_id, created_at, updated_at) VALUES (4, 'profile4.png', '/images/profile/4_profile4.png', 'image/png', 250000, FALSE, 5, NOW(), NOW());

-- Board Data
INSERT INTO board (id, title, contents, like_count, view_count, user_id, created_at, updated_at) VALUES (1, '첫 번째 게시글', '안녕하세요, 첫 게시글입니다.', 10, 100, 1, NOW(), NOW());
INSERT INTO board (id, title, contents, like_count, view_count, user_id, created_at, updated_at) VALUES (2, '두 번째 게시글', '두 번째 게시글 내용입니다.', 5, 50, 2, NOW(), NOW());
INSERT INTO board (id, title, contents, like_count, view_count, user_id, created_at, updated_at) VALUES (3, '새로운 소식 공유', '회사 소식을 공유합니다. 많은 관심 부탁드립니다.', 20, 200, 3, NOW(), NOW());
INSERT INTO board (id, title, contents, like_count, view_count, user_id, created_at, updated_at) VALUES (4, '개발 팁 공유', 'Spring Boot 개발 시 유용한 팁입니다.', 15, 150, 1, NOW(), NOW());
INSERT INTO board (id, title, contents, like_count, view_count, user_id, created_at, updated_at) VALUES (5, '디자인 트렌드', '최신 UI/UX 디자인 트렌드에 대해 알아봅시다.', 8, 80, 4, NOW(), NOW());

-- Likes Data
INSERT INTO likes (id, board_id, user_id, created_at, updated_at) VALUES (1, 1, 2, NOW(), NOW());
INSERT INTO likes (id, board_id, user_id, created_at, updated_at) VALUES (2, 2, 1, NOW(), NOW());
INSERT INTO likes (id, board_id, user_id, created_at, updated_at) VALUES (3, 3, 1, NOW(), NOW());
INSERT INTO likes (id, board_id, user_id, created_at, updated_at) VALUES (4, 3, 2, NOW(), NOW());
INSERT INTO likes (id, board_id, user_id, created_at, updated_at) VALUES (5, 4, 5, NOW(), NOW());

-- RecruitProcess Data
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (1, '서류 전형', 1, 'BLUE', 1);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (2, '1차 면접', 2, 'RED', 1);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (3, '2차 면접', 3, 'ORANGE', 1);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (4, '최종 합격', 4, 'PURPLE', 1);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (5, '서류 심사', 1, 'BLUE', 2);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (6, '코딩 테스트', 2, 'PINK', 2);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (7, '기술 면접', 3, 'RED', 2);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (8, '임원 면접', 4, 'ORANGE', 2);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (9, '최종 합격', 5, 'PURPLE', 2);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (10, '포트폴리오 심사', 1, 'BLUE', 3);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (11, '실무 면접', 2, 'RED', 3);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (12, '최종 합격', 3, 'PURPLE', 3);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (13, '서류 전형', 1, 'BLUE', 4);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (14, '실무 면접', 2, 'RED', 4);
INSERT INTO recruit_process (id, name, order_idx, color_code, job_posting_id) VALUES (15, '최종 합격', 3, 'PURPLE', 4);

-- CoverLetterTitle Data
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (1, '지원 동기', '회사에 지원하게 된 동기를 작성해주세요.', 1);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (2, '성장 과정', '자신의 성장 과정을 구체적으로 작성해주세요.', 1);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (3, '직무 역량', '지원 직무와 관련된 자신의 강점을 작성해주세요.', 1);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (4, '입사 후 포부', '입사 후 어떤 기여를 하고 싶은지 작성해주세요.', 1);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (5, '지원 동기 및 포부', '프론트엔드 개발에 대한 열정과 비전을 보여주세요.', 2);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (6, '협업 경험', '팀 프로젝트 경험을 중심으로 작성해주세요.', 2);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (7, '디자인 철학', '자신만의 디자인 철학을 설명해주세요.', 3);
INSERT INTO cover_letter_title (id, title, sub_title, job_posting_id) VALUES (8, '마케팅 경험', '성공적인 마케팅 캠페인 경험을 작성해주세요.', 4);

-- Resume Data
INSERT INTO resume (id, applied_at, description, job_posting_id, user_id, process_id, created_at, updated_at) VALUES (1, '2025-10-20 10:00:00', '백엔드 개발자 지원합니다.', 1, 1, 1, NOW(), NOW());
INSERT INTO resume (id, applied_at, description, job_posting_id, user_id, process_id, created_at, updated_at) VALUES (2, '2025-10-25 11:00:00', '프론트엔드 개발자 지원합니다.', 2, 2, 5, NOW(), NOW());
INSERT INTO resume (id, applied_at, description, job_posting_id, user_id, process_id, created_at, updated_at) VALUES (3, '2025-10-28 14:00:00', 'UI/UX 디자이너 지원합니다.', 3, 4, 10, NOW(), NOW());
INSERT INTO resume (id, applied_at, description, job_posting_id, user_id, process_id, created_at, updated_at) VALUES (4, '2025-10-29 16:00:00', '디지털 마케터 지원합니다.', 4, 5, 13, NOW(), NOW());
INSERT INTO resume (id, applied_at, description, job_posting_id, user_id, process_id, created_at, updated_at) VALUES (5, '2025-10-21 10:30:00', '백엔드 개발자 (경력) 지원합니다.', 1, 5, 1, NOW(), NOW());

-- CoverLetterDescription Data
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (1, '어릴 적부터 개발에 대한 열정이 많았습니다.', 1, 1);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (2, '꾸준한 학습으로 개발 역량을 키웠습니다.', 2, 1);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (3, 'Spring Boot를 활용한 프로젝트 경험이 풍부합니다.', 3, 1);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (4, '귀사에 기여하여 함께 성장하고 싶습니다.', 4, 1);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (5, 'React 개발에 대한 깊은 이���와 경험을 가지고 있습니다.', 5, 2);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (6, '다양한 협업 툴을 사용하여 팀 프로젝트를 성공적으로 이끌었습니다.', 6, 2);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (7, '사용자 중심의 디자인을 추구하며, 미적 감각과 실용성을 겸비했습니다.', 7, 3);
INSERT INTO cover_letter_description (id, description, cover_letter_title_id, resume_id) VALUES (8, '데이터 기반의 마케팅 전략으로 높은 ROI를 달성했습니다.', 8, 4);

-- JobPostingSkill Data
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (1, 'Java', 1, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (2, 'Spring Boot', 1, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (3, 'MSA', 1, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (4, 'React', 2, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (5, 'TypeScript', 2, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (6, 'Figma', 3, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (7, 'Adobe XD', 3, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (8, 'Google Analytics', 4, NOW(), NOW());
INSERT INTO job_posting_skill (id, name, job_posting_id, created_at, updated_at) VALUES (9, 'SEO', 4, NOW(), NOW());

-- Pdf Data
INSERT INTO pdf (id, original_filename, saved_path, content_type, file_size, is_deleted, resume_id, created_at, updated_at) VALUES (1, 'resume_kim.pdf', '/pdfs/resume/1_resume_kim.pdf', 'application/pdf', 512000, FALSE, 1, NOW(), NOW());
INSERT INTO pdf (id, original_filename, saved_path, content_type, file_size, is_deleted, resume_id, created_at, updated_at) VALUES (2, 'resume_lee.pdf', '/pdfs/resume/2_resume_lee.pdf', 'application/pdf', 600000, FALSE, 2, NOW(), NOW());
INSERT INTO pdf (id, original_filename, saved_path, content_type, file_size, is_deleted, resume_id, created_at, updated_at) VALUES (3, 'portfolio_choi.pdf', '/pdfs/portfolio/3_portfolio_choi.pdf', 'application/pdf', 1200000, FALSE, 3, NOW(), NOW());

-- Career Data
INSERT INTO career (id, company_name, position, start_date, end_date, resume_id, created_at, updated_at) VALUES (1, 'ABC 주식회사', '백엔드 개발자', '2022-01-01 09:00:00', '2024-12-31 18:00:00', 1, NOW(), NOW());
INSERT INTO career (id, company_name, position, start_date, end_date, resume_id, created_at, updated_at) VALUES (2, 'XYZ 테크', '주니어 프론트엔드 개발자', '2023-03-01 09:00:00', '2025-09-30 18:00:00', 2, NOW(), NOW());
INSERT INTO career (id, company_name, position, start_date, end_date, resume_id, created_at, updated_at) VALUES (3, '디자인랩', 'UI/UX 디자이너', '2023-01-01 09:00:00', '2025-10-31 18:00:00', 3, NOW(), NOW());
INSERT INTO career (id, company_name, position, start_date, end_date, resume_id, created_at, updated_at) VALUES (4, '마케팅허브', '디지털 마케터', '2024-01-01 09:00:00', '2025-10-31 18:00:00', 4, NOW(), NOW());

-- Certificate Data
INSERT INTO certificate (id, name, acquired_date, resume_id, created_at, updated_at) VALUES (1, '정보처리기사', '2021-03-01', 1, NOW(), NOW());
INSERT INTO certificate (id, name, acquired_date, resume_id, created_at, updated_at) VALUES (2, 'SQLD', '2022-06-15', 1, NOW(), NOW());
INSERT INTO certificate (id, name, acquired_date, resume_id, created_at, updated_at) VALUES (3, '웹디자인기능사', '2023-04-20', 2, NOW(), NOW());
INSERT INTO certificate (id, name, acquired_date, resume_id, created_at, updated_at) VALUES (4, 'GTQ 1급', '2022-11-01', 3, NOW(), NOW());

-- Education Data
INSERT INTO education (id, school_name, major, degree, resume_id, created_at, updated_at) VALUES (1, '한국대학교', '컴퓨터공학과', '학사', 1, NOW(), NOW());
INSERT INTO education (id, school_name, major, degree, resume_id, created_at, updated_at) VALUES (2, '서울대학교', '소프트웨어학과', '학사', 2, NOW(), NOW());
INSERT INTO education (id, school_name, major, degree, resume_id, created_at, updated_at) VALUES (3, '이화여자대학교', '디자인학과', '학사', 3, NOW(), NOW());
INSERT INTO education (id, school_name, major, degree, resume_id, created_at, updated_at) VALUES (4, '고려대학교', '경영학과', '학사', 4, NOW(), NOW());

-- Language Data
INSERT INTO language (id, name, test_name, language_name, grade, speaking_level, test_date, resume_id, created_at, updated_at) VALUES (1, 'TOEIC', 'TOEIC', '영어', '900점', '상', '2023-05-10', 1, NOW(), NOW());
INSERT INTO language (id, name, test_name, language_name, grade, speaking_level, test_date, resume_id, created_at, updated_at) VALUES (2, 'OPIC', 'OPIC', '영어', 'AL', '최상', '2024-01-20', 1, NOW(), NOW());
INSERT INTO language (id, name, test_name, language_name, grade, speaking_level, test_date, resume_id, created_at, updated_at) VALUES (3, 'HSK', 'HSK', '중국��', '5급', '중', '2023-09-01', 2, NOW(), NOW());

-- OverseasExperience Data
INSERT INTO overseas_experience (id, type, country, start_date, end_date, note, resume_id, created_at, updated_at) VALUES (1, '어학연수', '미국', '2019-07-01', '2019-12-31', '영어 실력 향상', 1, NOW(), NOW());
INSERT INTO overseas_experience (id, type, country, start_date, end_date, note, resume_id, created_at, updated_at) VALUES (2, '워킹홀리데이', '호주', '2020-01-01', '2020-12-31', '다양한 문화 경험', 2, NOW(), NOW());

-- ResumeSkill Data
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (1, 'Java', 1, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (2, 'Spring Boot', 1, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (3, 'MySQL', 1, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (4, 'React', 2, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (5, 'JavaScript', 2, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (6, 'Figma', 3, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (7, 'Photoshop', 3, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (8, 'Google Analytics', 4, NOW(), NOW());
INSERT INTO resume_skill (id, name, resume_id, created_at, updated_at) VALUES (9, 'SNS 마케팅', 4, NOW(), NOW());

-- Interview Data
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (1, '2025-11-01 14:00:00', 60, 'SCHEDULED', '백엔드 개발자 1차 면접', 1, 1, 2, NOW(), NOW());
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (2, '2025-11-05 10:00:00', 45, 'SCHEDULED', '프론트엔드 개발자 1차 면접', 2, 2, 7, NOW(), NOW());
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (3, '2025-11-08 11:00:00', 50, 'SCHEDULED', 'UI/UX 디자이너 실무 면접', 3, 3, 11, NOW(), NOW());
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (4, '2025-11-12 15:00:00', 40, 'SCHEDULED', '디지털 마케터 실무 면접', 4, 4, 14, NOW(), NOW());
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (5, '2025-11-02 16:00:00', 60, 'COMPLETED', '백엔드 개발자 2차 면접 (완료)', 1, 1, 3, NOW(), NOW());
INSERT INTO interview (id, start_date_time, duration, status, description, resume_id, room_id, recruit_process_id, created_at, updated_at) VALUES (6, '2025-11-06 13:00:00', 45, 'ONGOING', '프론트엔드 개발자 코딩 테스트 (진행 중)', 2, 2, 6, NOW(), NOW());