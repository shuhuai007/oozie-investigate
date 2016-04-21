


CREATE TABLE IF NOT EXISTS sda.temp
ROW FORMAT DELIMITED
        FIELDS TERMINATED BY '1'
AS
   select * from sda.zhihu_user where user_agree_num='${USER_AGREE_NUM}';

