package com.villcore.web.download.dao;

import com.villcore.web.download.DownTask;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * create by villcore on 2018/6/9
 */
@Mapper
public interface DownTaskDao {
    @Insert("insert into t_down_task (name, url, state, total_file_len, has_down_bytes, submit_timestamp) values(#{name},#{url},#{state},#{totalFileLen},#{hasDownBytes},#{submitTimestamp})")
    int saveDownTask(DownTask downTask);

    @Select("SELECT * FROM t_down_task WHERE id = #{id}")
    DownTask getDownTask(int id);

    @Select("SELECT * FROM t_down_task ORDER BY submit_timestamp DESC LIMIT #{start},#{size}")
    List<DownTask> listDownTask(@Param("start") int start,
                                @Param("size") int size);

    @Update("UPDATE t_down_task SET name = #{name}, url=#{url}, state = #{state}, total_file_len = #{totalFileLen}, has_down_bytes = #{hasDownBytes}, submit_timestamp = #{submitTimestamp}, finish_timestamp = #{finishTimestamp} WHERE id = #{id}")
    int updateDownTask(DownTask downTask);

    @Select("SELECT * FROM t_down_task WHERE state = 0 ORDER BY submit_timestamp ASC  LIMIT #{start},#{size}")
    List<DownTask> listUnfinishDownTask(@Param("start") int start,
                                @Param("size") int size);

    @Select("DELETE FROM t_down_task WHERE id = #{id}")
    void deleteDownTask(int id);
}
