SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;
-- ================================================================
-- 채용 시스템 데이터베이스 초기화 스크립트 (MariaDB)
-- ================================================================


-- ================================================================
-- 2. 데이터 삽입
-- ================================================================

-- UserRole 데이터
INSERT INTO user_role(created_at, updated_at, code, name)
VALUES (NOW(), NOW(), 'ROLE_ADMIN', '관리자'),
       (NOW(), NOW(), 'ROLE_APPLICANT', '지원자'),
       (NOW(), NOW(), 'ROLE_INTERVIEWER', '면접관'),
       (NOW(), NOW(), 'ROLE_RECRUITER', '채용 담당자');

-- Users 데이터 (채용담당자 및 지원자)
INSERT INTO `users` (`name`, `email`, `password`, `birth`, `gender`, `phone`, `user_role_id`)
VALUES ('김민준', 'minjun.kim@company.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1985-03-15', 'Male',
        '010-1234-5678', 2),
       ('이서연', 'seoyeon.lee@company.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1987-07-22', 'Female',
        '010-2345-6789', 2),
       ('박지훈', 'jihoon.park@company.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1983-11-08', 'Male',
        '010-3456-7890', 3),
       ('최유진', 'yujin.choi@company.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1990-05-30', 'Female',
        '010-4567-8901', 4),
       ('정민수', 'minsu.jung@company.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1982-09-12', 'Male',
        '010-5678-9012', 1),
       ('강수빈', 'subin.kang@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1995-02-14', 'Female',
        '010-6789-0123', 1),
       ('윤재현', 'jaehyun.yoon@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1993-08-25', 'Male',
        '010-7890-1234', 1),
       ('한예슬', 'yeseul.han@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1996-12-03', 'Female',
        '010-8901-2345', 1),
       ('오성민', 'seongmin.oh@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1994-04-18', 'Male',
        '010-9012-3456', 1),
       ('임지우', 'jiwoo.lim@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1997-06-27', 'Female',
        '010-0123-4567', 1),
       ('송태양', 'taeyang.song@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1992-10-09', 'Male',
        '010-1111-2222', 4),
       ('배소영', 'soyoung.bae@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1995-01-21', 'Female',
        '010-2222-3333', 4),
       ('서준호', 'junho.seo@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1991-03-17', 'Male', '010-3333-4444',
        2),
       ('나현아', 'hyuna.na@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1998-07-05', 'Female', '010-4444-5555',
        2),
       ('황동혁', 'donghyuk.hwang@email.com', '$2a$10$fkDbsetbtX6HF78CV3.boeVCf8QmECM6d1.3/Tm2AFDdXEcGxYq5a', '1993-11-29', 'Male',
        '010-5555-6666', 4);

-- JobGroup 데이터
INSERT INTO `job_group` (`name`, `location`)
VALUES ('개발', '서울 강남구'),
       ('디자인', '서울 강남구'),
       ('기획', '서울 서초구'),
       ('마케팅', '서울 강남구'),
       ('영업', '서울 영등포구');

-- Duty 데이터
INSERT INTO `duty` (`name`, `job_group_id`)
VALUES ('백엔드 개발', 1),
       ('프론트엔드 개발', 1),
       ('모바일 개발', 1),
       ('데이터 엔지니어링', 1),
       ('UI/UX 디자인', 2),
       ('그래픽 디자인', 2),
       ('서비스 기획', 3),
       ('전략 기획', 3),
       ('퍼포먼스 마케팅', 4),
       ('콘텐츠 마케팅', 4);

-- Department 데이터
INSERT INTO `department` (`name`, `duty_id`)
VALUES ('플랫폼개발팀', 1),
       ('웹서비스팀', 2),
       ('앱개발팀', 3),
       ('데이터플랫폼팀', 4),
       ('UX디자인팀', 5),
       ('브랜드디자인팀', 6),
       ('프로덕트기획팀', 7),
       ('전략기획실', 8),
       ('그로스마케팅팀', 9),
       ('콘텐츠마케팅팀', 10);

-- JobPosting 데이터
INSERT INTO `job_posting` (`title`, `employment_type`, `career_type`, `min_experience`, `max_experience`,
                           `position_level`, `location`, `apply_start_date`, `apply_end_date`, `hire_end_date`,
                           `headcount`, `summary`, `responsibilities`, `requirements`, `preferred`, `salary_type`,
                           `salary_min`, `salary_max`, `salary_negotiable`, `working_hours`, `benefits`, `contact_name`,
                           `contact_email`, `additional_info`, `department_id`, `created_user_id`)
VALUES ('백엔드 개발자 (Spring Boot)', 'FULL_TIME', 'EXPERIENCED', 3, 7, '시니어', '서울 강남구', '2025-10-01 09:00:00',
        '2025-11-30 18:00:00', '2025-12-31 18:00:00', 3,
        '글로벌 서비스를 지원하는 대규모 백엔드 시스템을 개발합니다.',
        'Spring Boot 기반 REST API 설계 및 개발, MSA 아키텍처 설계 및 구현, 데이터베이스 설계 및 최적화, 클라우드 인프라 관리',
        'Spring Boot/Java 3년 이상 경력, RDBMS(MySQL/PostgreSQL) 경험, RESTful API 설계 경험, Git 사용 경험',
        'MSA 아키텍처 구축 경험, Kubernetes/Docker 활용 경험, AWS/GCP 등 클라우드 경험, 대용량 트래픽 처리 경험',
        'YEARLY', 50000000, 80000000, true, '09:00-18:00 (유연근무제)',
        '4대보험, 연차/반차 자유사용, 맥북 프로 지급, 교육비 지원, 간식/음료 무제한, 명절 선물, 경조사 지원',
        '김민준', 'minjun.kim@company.com', '서류전형 > 1차 면접 > 2차 면접 > 최종합격', 1, 1),

       ('프론트엔드 개발자 (React)', 'FULL_TIME', 'EXPERIENCED', 2, 5, '주니어/시니어', '서울 강남구', '2025-10-15 09:00:00',
        '2025-11-15 18:00:00', '2025-12-15 18:00:00', 2,
        '사용자 경험을 최우선으로 하는 웹 서비스 프론트엔드를 개발합니다.',
        'React 기반 웹 애플리케이션 개발, 반응형 UI/UX 구현, API 연동 및 상태관리, 성능 최적화',
        'React 2년 이상 경력, JavaScript/TypeScript 능숙, HTML5/CSS3 이해, Git 사용 경험',
        'Next.js 사용 경험, Redux/MobX 등 상태관리 라이브러리 경험, 웹 성능 최적화 경험, UI/UX에 대한 이해',
        'YEARLY', 40000000, 65000000, true, '09:00-18:00 (유연근무제)',
        '4대보험, 재택근무 가능, 최신 장비 지원, 도서구입비 지원, 스낵바 운영, 워크샵 지원',
        '이서연', 'seoyeon.lee@company.com', '포트폴리오 필수 제출', 2, 1),

       ('iOS 앱 개발자', 'FULL_TIME', 'NEW', NULL, NULL, '신입/경력', '서울 강남구', '2025-10-20 09:00:00', '2025-12-20 18:00:00',
        '2026-01-20 18:00:00', 2,
        '수백만 사용자가 사용하는 모바일 앱을 개발합니다.',
        'iOS 네이티브 앱 개발, UI/UX 구현, RESTful API 연동, 앱 성능 모니터링 및 개선',
        'Swift/SwiftUI 사용 가능, iOS 앱 개발 경험(프로젝트 포함), Git 사용 가능, CS 기초 지식',
        '출시된 앱 개발 경험, RxSwift/Combine 사용 경험, 클린 아키텍처 이해, 앱 스토어 배포 경험',
        'YEARLY', 35000000, 50000000, false, '09:00-18:00',
        '4대보험, 점심/저녁 식사 제공, 최신 맥북/아이폰 지급, 컨퍼런스 참가 지원, 야근 택시비 지원',
        '박지훈', 'jihoon.park@company.com', '신입의 경우 개인 프로젝트 포트폴리오 필수', 3, 2),

       ('데이터 엔지니어', 'FULL_TIME', 'EXPERIENCED', 3, 10, '시니어', '서울 강남구', '2025-09-01 09:00:00', '2025-10-31 18:00:00',
        '2025-11-30 18:00:00', 1,
        '빅데이터 파이프라인을 구축하고 데이터 인프라를 관리합니다.',
        '데이터 파이프라인 설계 및 구축, ETL 프로세스 개발, 데이터 웨어하우스 구축, 데이터 품질 관리',
        'Python 또는 Scala 능숙, Spark/Hadoop 경험, SQL 능숙, 데이터베이스 설계 경험',
        'Airflow/Kafka 사용 경험, 클라우드 데이터 플랫폼 경험, 머신러닝 파이프라인 구축 경험, dbt 사용 경험',
        'YEARLY', 60000000, 90000000, true, '10:00-19:00 (유연근무제)',
        '스톡옵션, 4대보험, 원격근무 가능, 최고 사양 장비 지원, 컨퍼런스 참가비 전액 지원',
        '최유진', 'yujin.choi@company.com', '데이터 엔지니어링 포트폴리오 제출 필수', 4, 2),

       ('UI/UX 디자이너', 'FULL_TIME', 'EXPERIENCED', 2, 5, '중급', '서울 강남구', '2025-10-10 09:00:00', '2025-11-25 18:00:00',
        '2025-12-25 18:00:00', 2,
        '사용자 중심의 직관적인 인터페이스를 디자인합니다.',
        '웹/모바일 UI 디자인, 사용자 리서치 및 분석, 프로토타입 제작, 디자인 시스템 구축 및 관리',
        'Figma 능숙, 2년 이상 UI/UX 디자인 경력, 포트폴리오 보유, 웹/앱 디자인 경험',
        '디자인 시스템 구축 경험, 사용자 테스트 진행 경험, 프론트엔드 개발 지식, Principle/Framer 사용 경험',
        'YEARLY', 40000000, 60000000, true, '10:00-19:00',
        '4대보험, 재택근무 주 2회, 최신 맥북 프로 지급, 외부 강의 수강료 지원, 디자인 툴 구독료 지원',
        '이서연', 'seoyeon.lee@company.com', '포트폴리오 필수 제출 (PDF 또는 링크)', 5, 1),

       ('프로덕트 매니저 (PM)', 'FULL_TIME', 'EXPERIENCED', 4, 8, '시니어', '서울 서초구', '2025-10-05 09:00:00',
        '2025-11-30 18:00:00', '2025-12-31 18:00:00', 1,
        '데이터 기반으로 제품 전략을 수립하고 실행합니다.',
        '제품 로드맵 수립, 기능 우선순위 결정, 개발팀과 협업, 사용자 피드백 분석 및 개선안 도출',
        'PM 경력 4년 이상, 데이터 기반 의사결정 경험, SQL 사용 가능, 애자일 방법론 이해',
        'B2C 서비스 PM 경험, A/B 테스트 설계 및 분석 경험, 개발 지식 보유, 스타트업 경험',
        'YEARLY', 55000000, 85000000, true, '09:00-18:00 (유연근무제)',
        '스톡옵션, 4대보험, 자율 출퇴근, 교육비 무제한 지원, 업무용 장비 자유 선택',
        '박지훈', 'jihoon.park@company.com', '제품 기획서 또는 케이스 스터디 제출', 7, 2),

       ('백엔드 개발 인턴', 'INTERN', 'NEW', NULL, NULL, '인턴', '서울 강남구', '2025-11-01 09:00:00', '2025-12-15 18:00:00',
        '2026-01-15 18:00:00', 5,
        '실무 중심의 백엔드 개발 인턴십 프로그램입니다.',
        '백엔드 API 개발 보조, 데이터베이스 관리, 테스트 코드 작성, 코드 리뷰 참여',
        '컴퓨터공학 전공 또는 관련 분야 재학/졸업, Java 또는 Python 기초 지식, 데이터베이스 기초 지식, 학습 의지',
        '개인 프로젝트 경험, Spring Boot 학습 경험, Git 사용 경험, 개발 커뮤니티 활동',
        'MONTHLY', 2500000, 2500000, false, '09:00-18:00',
        '4대보험, 중식 제공, 맥북 대여, 우수 인턴 정규직 전환 기회, 멘토링 프로그램',
        '김민준', 'minjun.kim@company.com', '3개월 인턴십 후 정규직 전환 검토', 1, 1);

-- RecruitProcess 데이터 (각 채용공고별 프로세스)
INSERT INTO `recruit_process` (`name`, `order_idx`, `color_code`, `job_posting_id`)
VALUES
-- 백엔드 개발자 프로세스
('서류전형', 1, 'BLUE', 1),
('코딩테스트', 2, 'PURPLE', 1),
('1차 기술면접', 3, 'ORANGE', 1),
('2차 컬처핏면접', 4, 'PINK', 1),
('최종합격', 5, 'RED', 1),

-- 프론트엔드 개발자 프로세스
('서류전형', 1, 'BLUE', 2),
('과제전형', 2, 'PURPLE', 2),
('기술면접', 3, 'ORANGE', 2),
('최종면접', 4, 'PINK', 2),
('최종합격', 5, 'RED', 2),

-- iOS 개발자 프로세스
('서류전형', 1, 'BLUE', 3),
('1차 면접', 2, 'ORANGE', 3),
('2차 면접', 3, 'PINK', 3),
('최종합격', 4, 'RED', 3),

-- 데이터 엔지니어 프로세스
('서류전형', 1, 'BLUE', 4),
('기술과제', 2, 'PURPLE', 4),
('기술면접', 3, 'ORANGE', 4),
('임원면접', 4, 'PINK', 4),
('최종합격', 5, 'RED', 4),

-- UI/UX 디자이너 프로세스
('서류전형', 1, 'BLUE', 5),
('포트폴리오리뷰', 2, 'PURPLE', 5),
('실무면접', 3, 'ORANGE', 5),
('최종합격', 4, 'RED', 5),

-- PM 프로세스
('서류전형', 1, 'BLUE', 6),
('케이스스터디', 2, 'PURPLE', 6),
('팀면접', 3, 'ORANGE', 6),
('임원면접', 4, 'PINK', 6),
('최종합격', 5, 'RED', 6),

-- 인턴 프로세스
('서류전형', 1, 'BLUE', 7),
('면접', 2, 'ORANGE', 7),
('최종합격', 3, 'RED', 7);

-- JobPostingSkill 데이터
INSERT INTO `job_posting_skill` (`name`, `job_posting_id`)
VALUES
-- 백엔드 개발자 스킬
('Java', 1),
('Spring Boot', 1),
('MySQL', 1),
('Redis', 1),
('Kubernetes', 1),
('AWS', 1),
-- 프론트엔드 개발자 스킬
('React', 2),
('TypeScript', 2),
('Next.js', 2),
('Redux', 2),
('Webpack', 2),
-- iOS 개발자 스킬
('Swift', 3),
('SwiftUI', 3),
('RxSwift', 3),
('CoreData', 3),
-- 데이터 엔지니어 스킬
('Python', 4),
('Spark', 4),
('Airflow', 4),
('Kafka', 4),
('Snowflake', 4),
-- UI/UX 디자이너 스킬
('Figma', 5),
('Sketch', 5),
('Prototyping', 5),
('User Research', 5),
-- PM 스킬
('SQL', 6),
('Jira', 6),
('A/B Testing', 6),
('Google Analytics', 6),
-- 인턴 스킬
('Java', 7),
('Python', 7),
('Git', 7);

-- Resume 데이터 (지원서)
INSERT INTO `resume` (`applied_at`, `description`, `job_posting_id`, `user_id`, `process_id`)
VALUES ('2025-10-05 14:30:00', '5년차 백엔드 개발자로 MSA 전환 프로젝트를 리드한 경험이 있습니다.', 1, 6, 3),
       ('2025-10-07 10:15:00', '스타트업에서 3년간 Spring Boot로 다양한 서비스를 개발했습니다.', 1, 7, 2),
       ('2025-10-18 16:20:00', 'React와 TypeScript를 활용한 대규모 프로젝트 경험이 있습니다.', 2, 8, 4),
       ('2025-10-22 11:45:00', '개인 프로젝트로 출시한 iOS 앱이 10만 다운로드를 달성했습니다.', 3, 9, 2),
       ('2025-10-25 09:30:00', '데이터 분석부터 파이프라인 구축까지 전 과정을 경험했습니다.', 4, 10, 1),
       ('2025-10-15 13:50:00', '사용자 중심 디자인으로 전환율 30% 향상을 이끌었습니다.', 5, 11, 3),
       ('2025-10-12 15:10:00', 'B2C 앱 서비스에서 MAU 200만을 달성한 경험이 있습니다.', 6, 12, 2),
       ('2025-11-03 10:00:00', '컴퓨터공학 전공 4학년으로 백엔드 개발에 열정이 있습니다.', 7, 13, 1),
       ('2025-11-05 14:20:00', '개인 프로젝트로 Spring Boot 학습을 완료했습니다.', 7, 14, 1),
       ('2025-10-08 16:40:00', '7년차 백엔드 개발자로 대용량 트래픽 처리 경험이 풍부합니다.', 1, 15, 4);

-- Career 데이터 (경력사항)
INSERT INTO `career` (`company_name`, `position`, `start_date`, `end_date`, `resume_id`)
VALUES ('네이버', '백엔드 개발자', '2020-01-01 00:00:00', '2025-09-30 00:00:00', 1),
       ('카카오', '백엔드 개발자', '2018-03-01 00:00:00', '2019-12-31 00:00:00', 1),
       ('토스', 'Spring 개발자', '2022-06-01 00:00:00', '2025-10-01 00:00:00', 2),
       ('쿠팡', '프론트엔드 개발자', '2021-07-01 00:00:00', '2024-05-31 00:00:00', 3),
       ('라인', '데이터 엔지니어', '2019-03-01 00:00:00', '2023-12-31 00:00:00', 5),
       ('배달의민족', 'UI/UX 디자이너', '2021-01-01 00:00:00', '2025-09-30 00:00:00', 6),
       ('당근마켓', '프로덕트 매니저', '2020-02-01 00:00:00', '2025-08-31 00:00:00', 7),
       ('삼성전자', '백엔드 개발자', '2018-01-01 00:00:00', '2025-09-30 00:00:00', 10);

-- Education 데이터 (학력사항)
INSERT INTO `education` (`school_name`, `major`, `degree`, `resume_id`)
VALUES ('서울대학교', '컴퓨터공학과', '학사', 1),
       ('연세대학교', '소프트웨어학과', '학사', 2),
       ('고려대학교', '컴퓨터학과', '학사', 3),
       ('KAIST', '전산학부', '석사', 4),
       ('서울대학교', '산업공학과', '학사', 5),
       ('홍익대학교', '디자인학부', '학사', 6),
       ('성균관대학교', '경영학과', '학사', 7),
       ('포항공과대학교', '컴퓨터공학과', '학사', 8),
       ('한양대학교', '컴퓨터소프트웨어학부', '재학', 9),
       ('서강대학교', '컴퓨터공학과', '학사', 10);

-- Certificate 데이터 (자격증)
INSERT INTO `certificate` (`name`, `acquired_date`, `resume_id`)
VALUES ('정보처리기사', '2019-11', 1),
       ('AWS Certified Solutions Architect', '2022-03', 1),
       ('SQLD', '2021-06', 2),
       ('정보처리기사', '2020-08', 3),
       ('Google Cloud Professional Data Engineer', '2021-12', 5),
       ('컴퓨터그래픽스운용기능사', '2020-05', 6),
       ('OPIC IH', '2019-09', 7),
       ('정보처리기사', '2024-05', 8),
       ('SQLD', '2023-11', 10);

-- Language 데이터 (어학능력)
INSERT INTO `language` (`name`, `test_name`, `language_name`, `grade`, `speaking_level`, `test_date`, `resume_id`)
VALUES ('영어', 'TOEIC', 'English', '950', 'Advanced', '2024-08-15', 1),
       ('영어', 'TOEIC Speaking', 'English', 'Level 7', 'Advanced', '2024-03-20', 3),
       ('영어', 'OPIC', 'English', 'IH', 'Intermediate High', '2023-11-10', 5),
       ('일본어', 'JLPT', 'Japanese', 'N2', 'Intermediate', '2023-07-15', 6),
       ('영어', 'TOEIC', 'English', '920', 'Advanced', '2024-09-05', 7),
       ('중국어', 'HSK', 'Chinese', '5급', 'Intermediate', '2024-01-20', 10);

-- ResumeSkill 데이터 (지원자 기술스택)
INSERT INTO `resume_skill` (`name`, `resume_id`)
VALUES ('Java', 1),
       ('Spring Boot', 1),
       ('Kubernetes', 1),
       ('MySQL', 1),
       ('Redis', 1),
       ('AWS', 1),
       ('Java', 2),
       ('Spring Boot', 2),
       ('PostgreSQL', 2),
       ('Docker', 2),
       ('Jenkins', 2),
       ('React', 3),
       ('TypeScript', 3),
       ('Next.js', 3),
       ('Redux', 3),
       ('Tailwind CSS', 3),
       ('Swift', 4),
       ('SwiftUI', 4),
       ('RxSwift', 4),
       ('Firebase', 4),
       ('Realm', 4),
       ('Python', 5),
       ('Spark', 5),
       ('Airflow', 5),
       ('dbt', 5),
       ('Snowflake', 5),
       ('AWS', 5),
       ('Figma', 6),
       ('Sketch', 6),
       ('Adobe XD', 6),
       ('Protopie', 6),
       ('Illustrator', 6),
       ('SQL', 7),
       ('Python', 7),
       ('Tableau', 7),
       ('Mixpanel', 7),
       ('Amplitude', 7),
       ('Java', 8),
       ('Python', 8),
       ('MySQL', 8),
       ('Git', 8),
       ('Java', 9),
       ('Spring', 9),
       ('MySQL', 9),
       ('Git', 9),
       ('Linux', 9),
       ('Java', 10),
       ('Spring Boot', 10),
       ('Redis', 10),
       ('Kafka', 10),
       ('MySQL', 10),
       ('AWS', 10);

-- OverseasExperience 데이터 (해외경험)
INSERT INTO `overseas_experience` (`type`, `country`, `start_date`, `end_date`, `note`, `resume_id`)
VALUES ('어학연수', '미국', '2017-01-01', '2017-12-31', '캘리포니아 어학연수', 1),
       ('교환학생', '일본', '2019-03-01', '2019-08-31', '도쿄대학 교환학생', 6),
       ('워킹홀리데이', '호주', '2018-01-01', '2018-12-31', '시드니 워킹홀리데이', 7),
       ('해외인턴', '싱가포르', '2020-07-01', '2020-12-31', 'IT 스타트업 인턴', 10);

-- Room 데이터 (면접실)
INSERT INTO `room` (`name`, `location`, `room_type`, `description`)
VALUES ('회의실 A', '본사 5층', 'OFFLINE', '대형 회의실 (10인실)'),
       ('회의실 B', '본사 5층', 'OFFLINE', '중형 회의실 (6인실)'),
       ('회의실 C', '본사 6층', 'OFFLINE', '소형 회의실 (4인실)'),
       ('임원 회의실', '본사 10층', 'OFFLINE', '임원 전용 회의실'),
       ('Zoom 1번방', '온라인', 'ONLINE', 'Zoom 화상 회의'),
       ('Google Meet 1번방', '온라인', 'ONLINE', 'Google Meet 화상 회의'),
       ('MS Teams 1번방', '온라인', 'ONLINE', 'MS Teams 화상 회의');

-- Interview 데이터 (면접일정)
INSERT INTO `interview` (`start_date_time`, `duration`, `status`, `description`, `resume_id`, `room_id`,
                         `recruit_process_id`)
VALUES ('2025-10-20 14:00:00', 60, 'COMPLETED', '백엔드 개발 1차 기술면접 - 시스템 설계 및 코딩', 1, 1, 3),
       ('2025-10-28 15:00:00', 90, 'SCHEDULED', '백엔드 개발 2차 컬처핏 면접', 1, 4, 4),
       ('2025-10-15 10:00:00', 45, 'COMPLETED', '코딩테스트 진행', 2, 5, 2),
       ('2025-10-25 16:00:00', 60, 'COMPLETED', '프론트엔드 기술면접 - React 실무 경험', 3, 2, 8),
       ('2025-10-30 11:00:00', 60, 'SCHEDULED', 'iOS 개발 1차 면접', 4, 3, 12),
       ('2025-11-01 14:00:00', 90, 'SCHEDULED', '데이터 엔지니어 기술면접', 5, 1, 18),
       ('2025-10-22 13:00:00', 60, 'COMPLETED', 'UI/UX 포트폴리오 리뷰', 6, 2, 22),
       ('2025-10-18 15:30:00', 60, 'COMPLETED', 'PM 케이스 스터디 발표', 7, 1, 27),
       ('2025-11-08 10:00:00', 30, 'SCHEDULED', '인턴 면접', 8, 6, 30),
       ('2025-10-25 16:00:00', 90, 'COMPLETED', '백엔드 시니어 기술면접', 10, 1, 3);

-- Board 데이터 (게시판)
INSERT INTO `board` (`title`, `contents`, `like_count`, `view_count`, `user_id`)
VALUES ('2025년 하반기 신입 공채 안내',
        '안녕하세요. 인사팀입니다.\n2025년 하반기 신입 공채를 다음과 같이 진행합니다.\n\n- 접수기간: 2025.11.01 ~ 11.30\n- 전형절차: 서류 > 코딩테스트 > 면접 > 최종합격\n- 채용분야: 개발, 디자인, 기획\n\n많은 관심 부탁드립니다.',
        45, 1230, 1),
       ('면접 준비 팁 공유합니다',
        '최근 면접을 진행하면서 느낀 점들을 공유합니다.\n\n1. 기술 스택은 깊이있게 준비하세요\n2. 프로젝트 경험을 구체적으로 설명할 수 있어야 합니다\n3. 회사와 직무에 대한 이해도가 중요합니다\n\n도움이 되셨으면 좋겠습니다!',
        89, 2456, 3),
       ('개발자 채용 FAQ',
        '개발자 채용과 관련하여 자주 묻는 질문들을 정리했습니다.\n\nQ. 신입도 지원 가능한가요?\nA. 네, 신입 채용 공고를 별도로 운영하고 있습니다.\n\nQ. 코딩테스트는 어떤 수준인가요?\nA. 알고리즘 중급 수준입니다.',
        67, 3201, 2),
       ('채용 프로세스 개선 안내',
        '더 나은 채용 경험을 위해 프로세스를 개선했습니다.\n\n- 서류 결과 안내 기간 단축 (7일 → 3일)\n- 면접 일정 조율 시스템 도입\n- 면접 피드백 제공 시작\n\n많은 관심 부탁드립니다.',
        34, 987, 1),
       ('디자이너 포트폴리오 가이드',
        '디자이너 지원 시 포트폴리오 작성 가이드입니다.\n\n1. 프로젝트 배경과 목표를 명확히\n2. 디자인 프로세스를 단계별로\n3. 결과와 성과를 수치로\n4. PDF 형식 권장 (최대 20MB)',
        102, 4523, 2);

-- Likes 데이터
INSERT INTO `likes` (`board_id`, `user_id`)
VALUES (1, 6),
       (1, 7),
       (1, 8),
       (1, 9),
       (2, 6),
       (2, 7),
       (2, 10),
       (2, 11),
       (2, 12),
       (3, 8),
       (3, 9),
       (3, 13),
       (3, 14),
       (4, 10),
       (4, 11),
       (4, 15),
       (5, 6),
       (5, 7),
       (5, 8),
       (5, 11),
       (5, 12);

-- JobPostingSchedule 데이터
INSERT INTO `job_posting_schedules` (`title`, `position`, `department`, `experience`, `type`, `assigned_to_id`,
                                     `posted_date`, `deadline`, `start_time`, `end_time`, `status`, `description`,
                                     `responsibilities`, `requirements`, `preferences`, `benefits`, `urgent`,
                                     `applicants`, `progress`, `screening`, `interview1`, `interview2`, `final_stage`)
VALUES ('백엔드 개발자 시니어급 채용', '백엔드 개발자', '플랫폼개발팀', '3-7년', '정규직', 1, '2025-10-01', '2025-11-30', '09:00:00', '18:00:00',
        'recruiting',
        '대규모 트래픽을 처리하는 백엔드 시스템 개발', 'API 설계 및 개발, MSA 아키텍처 설계, 데이터베이스 최적화',
        'Spring Boot 3년 이상, RDBMS 경험, RESTful API 설계 경험', 'MSA 경험, Kubernetes 경험, 클라우드 경험',
        '4대보험, 유연근무제, 최신 장비 지원, 교육비 지원', true, 23, 10, 8, 3, 2, 0),

       ('프론트엔드 개발자 (React)', '프론트엔드 개발자', '웹서비스팀', '2-5년', '정규직', 2, '2025-10-15', '2025-11-15', '09:00:00', '18:00:00',
        'screening',
        '사용자 경험 최우선 웹 서비스 개발', 'React 기반 웹 앱 개발, 반응형 UI 구현',
        'React 2년 이상, TypeScript 능숙', 'Next.js 경험, 성능 최적화 경험',
        '재택근무 가능, 최신 장비 지원', false, 18, 15, 12, 0, 0, 0),

       ('데이터 엔지니어', '데이터 엔지니어', '데이터플랫폼팀', '3-10년', '정규직', 4, '2025-09-01', '2025-10-31', '10:00:00', '19:00:00',
        'interviewing',
        '빅데이터 파이프라인 구축', '데이터 파이프라인 설계, ETL 개발',
        'Python/Scala 능숙, Spark 경험', 'Airflow 경험, 클라우드 경험',
        '스톡옵션, 원격근무 가능', true, 12, 8, 5, 3, 2, 0);

-- JobPostingScheduleShare 데이터
INSERT INTO `job_posting_schedule_share` (`schedule_id`, `user_id`)
VALUES (1, 3),
       (1, 4),
       (1, 5),
       (2, 3),
       (2, 5),
       (3, 1),
       (3, 3);

-- Pdf 데이터 (이력서 PDF)
INSERT INTO `pdf` (`original_filename`, `saved_path`, `content_type`, `file_size`, `is_deleted`, `resume_id`)
VALUES ('강수빈_이력서.pdf', '/uploads/resumes/2025/10/resume_6_20251005.pdf', 'application/pdf', 524288, false, 1),
       ('윤재현_이력서.pdf', '/uploads/resumes/2025/10/resume_7_20251007.pdf', 'application/pdf', 612352, false, 2),
       ('한예슬_이력서.pdf', '/uploads/resumes/2025/10/resume_8_20251018.pdf', 'application/pdf', 487424, false, 3),
       ('오성민_이력서.pdf', '/uploads/resumes/2025/10/resume_9_20251022.pdf', 'application/pdf', 536870, false, 4),
       ('임지우_이력서.pdf', '/uploads/resumes/2025/10/resume_10_20251025.pdf', 'application/pdf', 698745, false, 5),
       ('송태양_이력서.pdf', '/uploads/resumes/2025/10/resume_11_20251015.pdf', 'application/pdf', 445896, false, 6),
       ('배소영_이력서.pdf', '/uploads/resumes/2025/10/resume_12_20251012.pdf', 'application/pdf', 578963, false, 7),
       ('서준호_이력서.pdf', '/uploads/resumes/2025/11/resume_13_20251103.pdf', 'application/pdf', 389456, false, 8),
       ('나현아_이력서.pdf', '/uploads/resumes/2025/11/resume_14_20251105.pdf', 'application/pdf', 412589, false, 9),
       ('황동혁_이력서.pdf', '/uploads/resumes/2025/10/resume_15_20251008.pdf', 'application/pdf', 654321, false, 10);

-- Image 데이터 (프로필 이미지)
INSERT INTO `image` (`original_filename`, `saved_path`, `content_type`, `file_size`, `is_deleted`, `user_id`)
VALUES ('profile_1.jpg', '/uploads/profiles/user_1_profile.jpg', 'image/jpeg', 204800, false, 1),
       ('profile_2.jpg', '/uploads/profiles/user_2_profile.jpg', 'image/jpeg', 215040, false, 2),
       ('profile_3.jpg', '/uploads/profiles/user_3_profile.jpg', 'image/jpeg', 198000, false, 3),
       ('profile_6.jpg', '/uploads/profiles/user_6_profile.jpg', 'image/jpeg', 187392, false, 6),
       ('profile_8.jpg', '/uploads/profiles/user_8_profile.jpg', 'image/jpeg', 223456, false, 8),
       ('profile_10.jpg', '/uploads/profiles/user_10_profile.jpg', 'image/jpeg', 195840, false, 10);

SET FOREIGN_KEY_CHECKS = 1;

-- ================================================================
-- 데이터 확인 쿼리
-- ================================================================

-- 전체 채용공고 수
SELECT COUNT(*) as total_job_postings
FROM job_posting;

-- 채용공고별 지원자 수
SELECT jp.title,
       COUNT(r.id) as applicant_count
FROM job_posting jp
         LEFT JOIN resume r ON jp.id = r.job_posting_id
GROUP BY jp.id, jp.title
ORDER BY applicant_count DESC;

-- 채용 프로세스별 지원자 현황
SELECT jp.title    as job_title,
       rp.name     as process_name,
       COUNT(r.id) as candidate_count
FROM job_posting jp
         JOIN recruit_process rp ON jp.id = rp.job_posting_id
         LEFT JOIN resume r ON rp.id = r.process_id
GROUP BY jp.id, jp.title, rp.id, rp.name
ORDER BY jp.id, rp.order_idx;

-- 면접 일정 현황
SELECT i.start_date_time,
       i.status,
       u.name   as candidate_name,
       jp.title as job_title,
       r.name   as room_name
FROM interview i
         JOIN resume res ON i.resume_id = res.id
         JOIN users u ON res.user_id = u.id
         JOIN job_posting jp ON res.job_posting_id = jp.id
         JOIN room r ON i.room_id = r.id
ORDER BY i.start_date_time;

-- ================================================================
-- 완료
-- ================================================================