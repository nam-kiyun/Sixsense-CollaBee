drop table if exists emp;
drop table if exists dept;
drop table if exists emp_file;
drop table if exists emp2;

create table emp
(
  empno     int,
  ename   varchar(10),
  job      varchar(9),
  mgr      int,
  hiredate  DATE,
  sal       DECIMAL(7,2),
  comm      DECIMAL(7,2),
  deptno    int,
  account   Byte
)
;

create unique index idx_emp_pk on emp
(empno)
;
alter table emp add constraint ( primary key (empno) );

create table dept
(
  deptno  int,
  dname   varchar(14),
  loc     varchar(13)
)
;
create unique index idx_dept_pk on dept
(deptno)
;
alter table dept add constraint ( primary key (deptno) );


create table emp_file
(
  empno      varchar(4),
  file_name  varchar(50),
  file_data  byte
)
;
create unique index idx_emp_file_pk on emp_file
(empno)
;
alter table emp_file add constraint ( primary key (empno) );


CREATE TABLE emp2
(
  empno     int,
  ename   varchar(10),
  job      varchar(9),
  mgr      int,
  hiredate  DATE,
  sal       DECIMAL(7,2),
  comm      DECIMAL(7,2),
  deptno    int,
  account   Byte
);


Insert into DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (10, 'ACCOUNTING', 'NEW YORK');
Insert into DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (20, 'RESEARCH', 'DALLAS');
Insert into DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (30, 'SALES', 'CHICAGO');
Insert into DEPT
   (DEPTNO, DNAME, LOC)
 Values
   (40, 'OPERATIONS', 'BOSTON');



Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
(1111, 'Honggildong', '総務', 22, to_date('2012/01/12', '%Y/%m/%d'), 5555, 10);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7369, 'SMITH', 'CLERK', 7902, to_date('1980/12/17', '%Y/%m/%d'), 4157.28, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7499, 'ALLEN', 'SALESMAN', 7698, to_date('1981/02/20', '%Y/%m/%d'), 1607, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7521, 'WARD', 'SALESMAN', 7698, to_date('1981/02/22', '%Y/%m/%d'), 1250, 500, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, DEPTNO)
 Values
   (7566, 'JONES', 'MANAGER', 7839, to_date('1981/04/02', '%Y/%m/%d'), 20);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7654, 'MARTIN', 'SALESMAN', 7698, to_date('1981/09/28', '%Y/%m/%d'), 1250, 1400, 10);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7698, 'BLAKE', 'MANAGER', 7839, to_date('1981/05/01', '%Y/%m/%d'), 2850, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7782, 'CLARK', 'MANAGER', 7839, to_date('1981/06/09', '%Y/%m/%d'), 2450, 10);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7788, 'SCOTT', 'ANALYST', 7566, to_date('1987/04/19', '%Y/%m/%d'), 3000, 20);
Insert into EMP
   (EMPNO, ENAME, JOB, HIREDATE, SAL, DEPTNO)
 Values
   (7839, 'KING', 'PRESIDENT', to_date('1981/11/17', '%Y/%m/%d'), 5000, 10);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, COMM, DEPTNO)
 Values
   (7844, 'TURNER', 'SALESMAN', 7698, to_date('1981/09/08', '%Y/%m/%d'), 1500, 0, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7876, 'ADAMS', 'CLERK', 7788, to_date('1987/05/23', '%Y/%m/%d'), 1100, 20);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7900, 'JAMES', 'CLERK', 7698, to_date('1981/12/03', '%Y/%m/%d'), 950, 30);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7902, 'FORD', 'ANALYST', 7566, to_date('1981/12/03', '%Y/%m/%d'), 3000, 20);
Insert into EMP
   (EMPNO, ENAME, JOB, MGR, HIREDATE, SAL, DEPTNO)
 Values
   (7934, 'MILLER', 'CLERK', 7782, to_date('1982/01/23', '%Y/%m/%d'), 1300, 10);



