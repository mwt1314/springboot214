CREATE TABLE `Student`(
	`s_id` VARCHAR(20),
	`s_name` VARCHAR(20) NOT NULL DEFAULT '',
	`s_birth` VARCHAR(20) NOT NULL DEFAULT '',
	`s_sex` VARCHAR(10) NOT NULL DEFAULT '',
	PRIMARY KEY(`s_id`)
);
-- 课程表
CREATE TABLE `Course`(
	`c_id`  VARCHAR(20),
	`c_name` VARCHAR(20) NOT NULL DEFAULT '',
	`t_id` VARCHAR(20) NOT NULL,
	PRIMARY KEY(`c_id`)
);
-- 教师表
CREATE TABLE `Teacher`(
	`t_id` VARCHAR(20),
	`t_name` VARCHAR(20) NOT NULL DEFAULT '',
	PRIMARY KEY(`t_id`)
);
-- 成绩表
CREATE TABLE `Score`(
	`s_id` VARCHAR(20),
	`c_id`  VARCHAR(20),
	`s_score` INT(3),
	PRIMARY KEY(`s_id`,`c_id`)
);
-- 插入学生表测试数据
INSERT INTO Student VALUES('01' , '赵雷' , '1990-01-01' , '男');
INSERT INTO Student VALUES('02' , '钱电' , '1990-12-21' , '男');
INSERT INTO Student VALUES('03' , '孙风' , '1990-05-20' , '男');
INSERT INTO Student VALUES('04' , '李云' , '1990-08-06' , '男');
INSERT INTO Student VALUES('05' , '周梅' , '1991-12-01' , '女');
INSERT INTO Student VALUES('06' , '吴兰' , '1992-03-01' , '女');
INSERT INTO Student VALUES('07' , '郑竹' , '1989-07-01' , '女');
INSERT INTO Student VALUES('08' , '王菊' , '1990-01-20' , '女');
-- 课程表测试数据
INSERT INTO Course VALUES('01' , '语文' , '02');
INSERT INTO Course VALUES('02' , '数学' , '01');
INSERT INTO Course VALUES('03' , '英语' , '03');

-- 教师表测试数据
INSERT INTO Teacher VALUES('01' , '张三');
INSERT INTO Teacher VALUES('02' , '李四');
INSERT INTO Teacher VALUES('03' , '王五');

-- 成绩表测试数据
INSERT INTO Score VALUES('01' , '01' , 80);
INSERT INTO Score VALUES('01' , '02' , 90);
INSERT INTO Score VALUES('01' , '03' , 99);
INSERT INTO Score VALUES('02' , '01' , 70);
INSERT INTO Score VALUES('02' , '02' , 60);
INSERT INTO Score VALUES('02' , '03' , 80);
INSERT INTO Score VALUES('03' , '01' , 80);
INSERT INTO Score VALUES('03' , '02' , 80);
INSERT INTO Score VALUES('03' , '03' , 80);
INSERT INTO Score VALUES('04' , '01' , 50);
INSERT INTO Score VALUES('04' , '02' , 30);
INSERT INTO Score VALUES('04' , '03' , 20);
INSERT INTO Score VALUES('05' , '01' , 76);
INSERT INTO Score VALUES('05' , '02' , 87);
INSERT INTO Score VALUES('06' , '01' , 31);
INSERT INTO Score VALUES('06' , '03' , 34);
INSERT INTO Score VALUES('07' , '02' , 89);
INSERT INTO Score VALUES('07' , '03' , 98);

-- 1、查询"01"课程比"02"课程成绩高的学生的信息及课程分数	
SELECT s.*,a.`s_score` AS '01',b.`s_score` AS '02' FROM score a INNER JOIN score b ON a.`c_id` = '01' AND b.`c_id` = '02' AND a.`s_score` > b.`s_score` AND a.`s_id` = b.`s_id` INNER JOIN student s ON s.s_id = a.`s_id`;

-- 2、查询"01"课程比"02"课程成绩低的学生的信息及课程分数
SELECT s.*,a.`s_score` AS '01',b.`s_score` AS '02' FROM score a INNER JOIN score b ON a.`s_id` = b.`s_id` AND a.`c_id` = '01' AND b.`c_id` = '02' AND a.`s_score` < b.`s_score` INNER JOIN student s ON s.`s_id` = a.`s_id`;

-- 3、查询平均成绩大于等于60分的同学的学生编号和学生姓名和平均成绩
SELECT a.`s_id`,s.`s_name`,ROUND(AVG(a.`s_score`), 2) AS avg_score FROM score a INNER JOIN student s ON a.`s_id` = s.`s_id` GROUP BY a.`s_id` HAVING avg_score >= 60;

-- 4、查询平均成绩小于60分的同学的学生编号和学生姓名和平均成绩
		-- (包括有成绩的和无成绩的)
SELECT s.s_id,st.s_name,AVG(s.s_score) FROM score s INNER JOIN student st ON s.`s_id` = st.`s_id` GROUP BY s.s_id HAVING AVG(s.s_score) < 60;
		
-- 5、查询所有同学的学生编号、学生姓名、选课总数、所有课程的总成绩
SELECT s.`s_id`,s.`s_name`,COUNT(1) AS num,SUM(a.`s_score`) AS total FROM student s LEFT JOIN score a ON s.`s_id` = a.`s_id` GROUP BY a.`s_id`;

-- 6、查询"李"姓老师的数量 
SELECT COUNT(1) FROM teacher t WHERE t.t_name LIKE '李%';

-- 7、查询学过"张三"老师授课的同学的信息 
SELECT st.* FROM score sc INNER JOIN student st ON sc.s_id = st.s_id WHERE sc.c_id IN (SELECT c.c_id FROM course c INNER JOIN teacher t ON c.t_id = t.t_id WHERE t.t_name = '张三');
SELECT s.* FROM student s WHERE s.`s_id` IN (SELECT DISTINCT k.s_id FROM teacher t INNER JOIN course c ON t.`t_id` = c.`t_id` INNER JOIN score k ON k.`c_id` = c.`c_id` WHERE t.`t_name` = '张三');

-- 8、查询没学过"张三"老师授课的同学的信息 	
SELECT * FROM student st WHERE st.s_id NOT IN (SELECT sc.s_id FROM score sc INNER JOIN course co ON sc.c_id = co.c_id INNER JOIN teacher te ON co.t_id = te.t_id WHERE te.t_name = '张三');

-- 9、查询学过编号为"01"并且也学过编号为"02"的课程的同学的信息
SELECT st.* FROM score a INNER JOIN score b ON a.s_id = b.s_id AND a.c_id = '01' AND b.c_id = '02' INNER JOIN student st ON st.s_id = a.s_id;

-- ***********10、查询学过编号为"01"但是没有学过编号为"02"的课程的同学的信息
SELECT st.* FROM student st WHERE st.s_id IN (SELECT sc.s_id FROM score sc WHERE sc.c_id = '01') AND st.s_id NOT IN (SELECT sc.s_id FROM score sc WHERE sc.c_id = '02');

-- 11、查询没有学全所有课程的同学的信息 支付牌照
SELECT st.* FROM student st INNER JOIN score sc ON st.s_id = sc.s_id GROUP BY sc.s_id HAVING COUNT(1) < (SELECT COUNT(1) FROM course);

-- 12、查询至少有一门课与学号为"01"的同学所学相同的同学的信息
SELECT st.* FROM student st WHERE st.s_id IN (SELECT DISTINCT(sc.s_id) FROM score sc WHERE sc.s_id <> '01' AND sc.c_id IN (SELECT sc.c_id FROM score sc WHERE sc.s_id = '01'));

-- ***13、查询和"01"号的同学学习的课程完全相同的其他同学的信息 
SELECT t1.s_id FROM (SELECT s_id,GROUP_CONCAT(c_id) AS group2 FROM score WHERE s_id = '01' GROUP BY s_id ORDER BY c_id) t2 
INNER JOIN 
(SELECT s_id,GROUP_CONCAT(c_id) AS group1 FROM score WHERE s_id <> '01' GROUP BY s_id ORDER BY c_id) t1
ON t2.group2 = t1.group1 INNER JOIN student t3 ON t3.s_id = t1.s_id;

-- 14、查询没学过"张三"老师讲授的任一门课程的学生姓名
SELECT st.* FROM student st WHERE st.s_id NOT IN (SELECT DISTINCT(sc.s_id) FROM score sc INNER JOIN (SELECT co.c_id FROM course co INNER JOIN teacher te ON co.t_id = te.t_id WHERE te.t_name = '张三') ct ON sc.c_id = ct.c_id );

-- 15、查询两门及其以上不及格课程的同学的学号，姓名及其平均成绩
SELECT sc.s_id,st.s_name,AVG(sc.s_score) FROM score sc INNER JOIN student st ON sc.s_id = st.s_id WHERE sc.s_score<60 GROUP BY sc.s_id HAVING COUNT(1) >= 2;

-- 16、检索"01"课程分数小于60，按分数降序排列的学生信息
SELECT st.* FROM score sc INNER JOIN student st ON sc.s_id = st.s_id  WHERE sc.c_id = '01' AND sc.s_score < 60 ORDER BY sc.s_score DESC;

-- ***17、按平均成绩从高到低显示所有学生的所有课程的成绩以及平均成绩
SELECT a.`s_id`,
MAX(CASE WHEN a.`c_id` = '01' THEN a.`s_score` END) AS 'chinese',
MAX(CASE WHEN a.`c_id` = '02' THEN a.`s_score` END) AS 'math',
MAX(CASE WHEN a.`c_id` = '03' THEN a.`s_score` END) AS 'english',
ROUND(AVG(a.`s_score`),2) AS vg 
FROM score a GROUP BY a.`s_id` ORDER BY vg DESC;

-- *****18.查询各科成绩最高分、最低分和平均分：以如下形式显示：课程ID，课程name，最高分，最低分，平均分，及格率，中等率，优良率，优秀率
-- 及格为>=60，中等为：70-80，优良为：80-90，优秀为：>=90
SELECT a.`c_id`,b.`c_name`,
MAX(a.`s_score`) AS 'max',
MIN(a.`s_score`) AS 'min',
ROUND(AVG(a.`s_score`),2) AS 'avg',
CONCAT(ROUND(SUM(CASE WHEN a.`s_score` >= 60 THEN 1 ELSE 0 END)/COUNT(1) * 100, 2), '%') AS '及格率',
ROUND(SUM(CASE WHEN a.`s_score` BETWEEN 70 AND 80 THEN 1 ELSE 0 END)/COUNT(1), 2) AS '中等率',
ROUND(SUM(CASE WHEN a.`s_score` BETWEEN 80 AND 90 THEN 1 ELSE 0 END)/COUNT(1), 2) AS '优良率',
ROUND(SUM(CASE WHEN a.`s_score` >= 90 THEN 1 ELSE 0 END)/COUNT(1), 2) AS '优秀率'
 FROM score a INNER JOIN course b ON a.`c_id` = b.`c_id` GROUP BY a.`c_id`;

-- ***19、按各科成绩进行排序，并显示排名
(SELECT a.`c_id`,a.`s_score`,(SELECT COUNT(DISTINCT(b.`s_score`)) FROM score b WHERE b.c_id = '01' AND b.s_score >= a.`s_score`) AS rank FROM score a WHERE a.`c_id` = '01' ORDER BY a.`c_id` ASC,a.`s_score` DESC)
 UNION 
(SELECT a.`c_id`,a.`s_score`,(SELECT COUNT(DISTINCT(b.`s_score`)) FROM score b WHERE b.c_id = '02' AND b.s_score >= a.`s_score`) AS rank FROM score a WHERE a.`c_id` = '02' ORDER BY a.`c_id` ASC,a.`s_score` DESC)
UNION
(SELECT a.`c_id`,a.`s_score`,(SELECT COUNT(DISTINCT(b.`s_score`)) FROM score b WHERE b.c_id = '03' AND b.s_score >= a.`s_score`) AS rank FROM score a WHERE a.`c_id` = '03' ORDER BY a.`c_id` ASC,a.`s_score` DESC);

SELECT a.s_score,@cr:=(CASE WHEN @cid != a.c_id THEN 1 ELSE @cr+1 END) AS 'rank',@cid:=a.c_id AS c_id FROM (SELECT s.c_id,s.s_score FROM score s GROUP BY s.c_id,s.s_score ORDER BY s.c_id,s.s_score DESC ) a,(SELECT @cid:=0,@cr:=0) AS xx;

-- 20、***查询学生的总成绩并进行排名
SELECT s_id,total AS '总分',(@i:=@i+1) AS '排名' FROM (SELECT s_id,SUM(s_score) AS total FROM score GROUP BY s_id ORDER BY total DESC) t,(SELECT @i:=0) AS j;

SELECT a.s_id,
	@i:=@i+1 AS i,
	@k:=(CASE WHEN @score=a.sum_score THEN @k ELSE @i END) AS rank,
	@score:=a.sum_score AS score
FROM (SELECT s_id,SUM(s_score) AS sum_score FROM score GROUP BY s_id ORDER BY sum_score DESC) as a,
	(SELECT @k:=0,@i:=0,@score:=0)s;

-- ***21、查询不同老师所教不同课程平均分从高到低显示 
SELECT sc.c_id,co.t_id,co.c_name,AVG(sc.s_score) avg_score FROM score sc INNER JOIN course co ON sc.c_id = co.c_id GROUP BY co.t_id,sc.c_id ORDER BY avg_score DESC;

-- **22、查询所有课程的成绩第2名到第3名的学生信息及该课程成绩
SELECT d.*,c.排名,c.s_score,c.c_id FROM (SELECT a.s_id,a.s_score,a.c_id,@i:=@i+1 AS 排名 FROM score a,(SELECT @i:=0) AS s WHERE a.c_id = '01' ORDER BY a.s_score DESC) c LEFT JOIN student d ON c.s_id=d.s_id WHERE 排名 IN (2,3)
UNION
SELECT d.*,c.排名,c.s_score,c.c_id FROM (SELECT a.s_id,a.s_score,a.c_id,@j:=@j+1 AS 排名 FROM score a,(SELECT @j:=0) AS s WHERE a.c_id = '02' ORDER BY a.s_score DESC) c LEFT JOIN student d ON c.s_id=d.s_id WHERE 排名 IN (2,3)
UNION
SELECT d.*,c.排名,c.s_score,c.c_id FROM (SELECT a.s_id,a.s_score,a.c_id,@k:=@k+1 AS 排名 FROM score a,(SELECT @k:=0) AS s WHERE a.c_id = '03' ORDER BY a.s_score DESC) c LEFT JOIN student d ON c.s_id=d.s_id WHERE 排名 IN (2,3)

-- ******23、统计各科成绩各分数段人数：课程编号,课程名称,[100-85],[85-70],[70-60],[0-60]及所占百分比
SELECT sc.c_id,co.c_name,SUM(CASE WHEN s_score BETWEEN 85 AND 100  THEN 1 ELSE 0 END) AS '[100-85]',ROUND(100*(SUM(CASE WHEN s_score BETWEEN 85 AND 100 THEN 1 ELSE 0 END)/COUNT(1)),2) AS '[100-85]百分比' FROM score sc INNER JOIN course co ON sc.c_id = co.c_id GROUP BY co.c_id HAVING co.c_id  = '01'

SELECT DISTINCT f.c_name,a.c_id,b.`85-100`,b.百分比,c.`70-85`,c.百分比,d.`60-70`,d.百分比,e.`0-60`,e.百分比 FROM score a
				LEFT JOIN (SELECT c_id,SUM(CASE WHEN s_score >85 AND s_score <=100 THEN 1 ELSE 0 END) AS `85-100`,
											ROUND(100*(SUM(CASE WHEN s_score >85 AND s_score <=100 THEN 1 ELSE 0 END)/COUNT(*)),2) AS 百分比
								FROM score GROUP BY c_id)b ON a.c_id=b.c_id
				LEFT JOIN (SELECT c_id,SUM(CASE WHEN s_score >70 AND s_score <=85 THEN 1 ELSE 0 END) AS `70-85`,
											ROUND(100*(SUM(CASE WHEN s_score >70 AND s_score <=85 THEN 1 ELSE 0 END)/COUNT(*)),2) AS 百分比
								FROM score GROUP BY c_id)c ON a.c_id=c.c_id
				LEFT JOIN (SELECT c_id,SUM(CASE WHEN s_score >60 AND s_score <=70 THEN 1 ELSE 0 END) AS `60-70`,
											ROUND(100*(SUM(CASE WHEN s_score >60 AND s_score <=70 THEN 1 ELSE 0 END)/COUNT(*)),2) AS 百分比
								FROM score GROUP BY c_id)d ON a.c_id=d.c_id
				LEFT JOIN (SELECT c_id,SUM(CASE WHEN s_score >=0 AND s_score <=60 THEN 1 ELSE 0 END) AS `0-60`,
											ROUND(100*(SUM(CASE WHEN s_score >=0 AND s_score <=60 THEN 1 ELSE 0 END)/COUNT(*)),2) AS 百分比
								FROM score GROUP BY c_id)e ON a.c_id=e.c_id
				LEFT JOIN course f ON a.c_id = f.c_id;


-- 24、查询学生平均成绩及其名次
SELECT k.*,(@r:=@r+1) FROM (SELECT sc.s_id,ROUND(AVG(sc.s_score),2) AS avg_score FROM score sc GROUP BY sc.s_id ORDER BY avg_score DESC) k,(SELECT @r:=0) r;
 
SELECT a.s_id,
				@i:=@i+1 AS '不保留空缺排名',
				@k:=(CASE WHEN @avg_score=a.avg_s THEN @k ELSE @i END) AS '保留空缺排名',
				@avg_score:=avg_s AS '平均分'
		FROM (SELECT s_id,ROUND(AVG(s_score),2) AS avg_s FROM score GROUP BY s_id ORDER BY avg_s DESC)a,(SELECT @avg_score:=0,@i:=0,@k:=0)b;

-- 25、****查询各科成绩前三名的记录
SELECT * FROM score sc GROUP BY sc.c_id;

SELECT a.s_id,a.c_id,a.s_score FROM score a 
			LEFT JOIN score b ON a.c_id = b.c_id AND a.s_score<b.s_score
			GROUP BY a.s_id,a.c_id,a.s_score HAVING COUNT(b.s_id)<3
			ORDER BY a.c_id,a.s_score DESC;
			
-- 26、查询每门课程被选修的学生数 
SELECT sc.c_id,COUNT(1) FROM score sc GROUP BY sc.c_id;

-- 27、查询出只有两门课程的全部学生的学号和姓名
SELECT s_id,s_name FROM student WHERE s_id IN(SELECT s_id FROM score GROUP BY s_id HAVING COUNT(c_id)=2);

-- 28、查询男生、女生人数
SELECT st.s_sex,COUNT(1) FROM student st GROUP BY st.s_sex;

-- 29、查询名字中含有"风"字的学生信息
SELECT * FROM student WHERE s_name LIKE '%风%';

-- 30、查询同名同性学生名单，并统计同名人数
SELECT st.s_name,st.s_sex, COUNT(1) FROM student st GROUP BY st.s_name,st.s_sex HAVING COUNT(1) > 1;

SELECT a.s_name,a.s_sex,COUNT(*) FROM student a  JOIN student b ON a.s_id !=b.s_id AND a.s_name = b.s_name AND a.s_sex = b.s_sex GROUP BY a.s_name,a.s_sex;
		
-- 31、查询1990年出生的学生名单
SELECT s_name FROM student WHERE s_birth LIKE '1990%';

-- 32、查询每门课程的平均成绩，结果按平均成绩降序排列，平均成绩相同时，按课程编号升序排列 
SELECT sc.c_id,AVG(sc.s_score) aa FROM score sc GROUP BY sc.c_id ORDER BY aa DESC,sc.c_id ASC;

-- 33、查询平均成绩大于等于85的所有学生的学号、姓名和平均成绩 
SELECT sc.s_id,st.s_name,ROUND(AVG(sc.s_score),2) aa FROM score sc LEFT JOIN student st ON sc.s_id = st.s_id GROUP BY sc.s_id HAVING aa >= 85;

-- 34、查询课程名称为"数学"，且分数低于60的学生姓名和分数 
SELECT st.s_name,sc.s_score FROM course co INNER JOIN score sc ON co.c_id = sc.c_id INNER JOIN student st ON st.s_id = sc.s_id WHERE co.c_name = '数学' AND sc.s_score < 60;

-- ***35、查询所有学生的课程及分数情况
SELECT st.s_id,st.s_name,
SUM(CASE co.c_name WHEN '语文' THEN sc.s_score ELSE 0 END) AS '语文',
SUM(CASE co.c_name WHEN '数学' THEN sc.s_score ELSE 0 END) AS '数学',
SUM(CASE co.c_name WHEN '英语' THEN sc.s_score ELSE 0 END) AS '英语' 
FROM student st LEFT JOIN score sc ON st.s_id = sc.s_id LEFT JOIN course co ON co.c_id = sc.c_id GROUP BY st.s_id;

-- 36、查询任何一门课程成绩在70分以上的学生姓名、课程名称和分数； 
SELECT a.s_name,b.c_name,c.s_score FROM course b LEFT JOIN score c ON b.c_id = c.c_id LEFT JOIN student a ON a.s_id=c.s_id WHERE c.s_score>70;

-- 37、查询不及格的课程
SELECT a.s_id,a.c_id,b.c_name,a.s_score FROM score a LEFT JOIN course b ON a.c_id = b.c_id WHERE a.s_score<60;

-- 38、查询课程编号为01且课程成绩在80分以上的学生的学号和姓名
SELECT st.s_id,st.s_name FROM score sc INNER JOIN student st ON sc.s_id = st.s_id WHERE sc.c_id = '01' AND sc.s_score > 80;

-- 39、求每门课程的学生人数 
SELECT COUNT(1) FROM score GROUP BY c_id;

-- 40、查询选修"张三"老师所授课程的学生中，成绩最高的学生信息及其成绩
SELECT sc.s_id,st.s_name,sc.s_score FROM teacher te INNER JOIN course co ON te.t_id = co.t_id INNER JOIN score sc ON sc.c_id = co.c_Id INNER JOIN student st ON st.s_id = sc.s_id WHERE te.t_name = '张三' ORDER BY sc.s_score DESC LIMIT 1;

-- ***41、查询不同课程成绩相同的学生的学生编号、课程编号、学生成绩
SELECT DISTINCT b.s_id,b.c_id,b.s_score FROM score a,score b WHERE a.c_id != b.c_id AND a.s_score = b.s_score;

-- *****42、查询每门功成绩最好的前两名
SELECT a.s_id,a.c_id,a.s_score FROM score a WHERE (SELECT COUNT(1) FROM score b WHERE b.c_id=a.c_id AND b.s_score>=a.s_score)<=2 ORDER BY a.c_id;

-- 43、统计每门课程的学生选修人数（超过5人的课程才统计）。要求输出课程号和选修人数，查询结果按人数降序排列，若人数相同，按课程号升序排列
SELECT sc.c_id,COUNT(1) AS num FROM score sc GROUP BY sc.c_id HAVING num > 5 ORDER BY num DESC,sc.c_id ASC;

-- 44、检索至少选修两门课程的学生学号 
SELECT s_id,COUNT(1) AS sel FROM score GROUP BY s_id HAVING sel>=2;

-- 45、查询选修了全部课程的学生信息
SELECT * FROM student st WHERE st.s_id IN (SELECT sc.s_id FROM score sc GROUP BY sc.s_id HAVING COUNT(1) >= (SELECT COUNT(1) FROM course));

-- ***46、查询各学生的年龄，按照出生日期来算，当前月日 < 出生年月的月日则，年龄减一
SELECT s_birth,(DATE_FORMAT(NOW(),'%Y')-DATE_FORMAT(s_birth,'%Y') - 
				(CASE WHEN DATE_FORMAT(NOW(),'%m%d')>DATE_FORMAT(s_birth,'%m%d') THEN 0 ELSE 1 END)) AS age
		FROM student;

-- 47、查询本周过生日的学生
	SELECT * FROM student WHERE WEEK(DATE_FORMAT(NOW(),'%Y%m%d'))=WEEK(s_birth);
	SELECT * FROM student WHERE YEARWEEK(s_birth)=YEARWEEK(DATE_FORMAT(NOW(),'%Y%m%d'));
	SELECT WEEK(DATE_FORMAT(NOW(),'%Y%m%d'));

-- 48、查询下周过生日的学生
SELECT * FROM student WHERE WEEK(DATE_FORMAT(NOW(),'%Y%m%d'))+1 =WEEK(s_birth);

-- 49、查询本月过生日的学生
SELECT * FROM student WHERE MONTH(DATE_FORMAT(NOW(),'%Y%m%d')) =MONTH(s_birth);
	
-- 50、查询下月过生日的学生
SELECT * FROM student WHERE MONTH(DATE_FORMAT(NOW(),'%Y%m%d'))+1 =MONTH(s_birth);


CREATE TABLE `new_table` (
`id` INT(11) NOT NULL AUTO_INCREMENT,
`stat` INT(11) DEFAULT '0',
`_time` DATE DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

INSERT INTO new_table (stat, _time) VALUES (1, NOW());
INSERT INTO new_table (_time) VALUES ('2019-01-01');
INSERT INTO new_table (stat, _time) VALUE (2, '2019-01-03');
SELECT COUNT(stat=1 OR NULL) FROM new_table;    -- count不会对null进行统计
SELECT COUNT(stat=0 AND _time='2019-01-01' OR NULL) AS allCount FROM new_table ; #统计stat=1并且时间是2019-04-20的记录
SELECT COUNT(1) FROM new_table WHERE stat = 0 AND _time = '2019-01-01';
SELECT _time,COUNT(stat>=0 OR NULL) AS allCount,COUNT(stat=1 OR NULL) AS okCount FROM new_table GROUP BY _time; #按日期统计总数和合格数
SELECT _time,COUNT(stat>=0 OR NULL) AS allCount,COUNT(stat=1 OR NULL) AS okCount, (COUNT(stat=1 OR NULL)/COUNT(stat>=0 OR NULL) *100) AS yeild FROM new_table GROUP BY _time; #按日期统计总数和合格数，并计算合格率



