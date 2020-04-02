package com.raising.forward.mapper;

import com.raising.forward.entity.DesignLine;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ysy
 * @date 2018/3/26 19:05
 * @description
 */

@Component
public interface DesignLineMapper {
    public List<DesignLine> query(@Param("cookieId") Integer cookieId,@Param("tenant") String tenant);

    public Integer validate(@Param("cookieId") Integer cookieId,@Param("tenant") String tenant);

    public void insert(@Param("designLine") DesignLine designLine,@Param("tenant") String tenant);

    public void delete(@Param("cookieId") Integer cookieId,@Param("tenant") String tenant);
}
