-- REPO_BRANCHES 테이블에 유니크 제약 조건 추가
-- 동일한 프로젝트 저장소에서 같은 이름의 브랜치가 중복 생성되는 것을 방지

-- 1. 기존 중복 데이터 확인
SELECT project_repo_id, branch_name, COUNT(*) as duplicate_count
FROM REPO_BRANCHES 
GROUP BY project_repo_id, branch_name 
HAVING COUNT(*) > 1;

-- 2. 중복 데이터가 있다면 삭제 (가장 최근 것만 남김)
DELETE rb1 FROM REPO_BRANCHES rb1
INNER JOIN REPO_BRANCHES rb2 
WHERE rb1.project_repo_id = rb2.project_repo_id 
AND rb1.branch_name = rb2.branch_name 
AND rb1.repo_branch_id < rb2.repo_branch_id;

-- 3. 유니크 제약 조건 추가
ALTER TABLE REPO_BRANCHES 
ADD CONSTRAINT uk_repo_branches_project_branch 
UNIQUE (project_repo_id, branch_name);

-- 4. 제약 조건 확인
SHOW INDEX FROM REPO_BRANCHES WHERE Key_name = 'uk_repo_branches_project_branch';