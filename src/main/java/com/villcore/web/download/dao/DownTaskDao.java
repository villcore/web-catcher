package com.villcore.web.download.dao;

import com.villcore.web.download.DownTask;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * create by villcore on 2018/6/9
 */
@Mapper
public interface DownTaskDao {
    @Insert("insert into t_down_task (name, url, state, total_file_len, has_down_bytes) values(#{name},#{url},#{state},#{totalFileLen},#{hasDownBytes})\n")
    int saveDownTask(DownTask downTask);
}
