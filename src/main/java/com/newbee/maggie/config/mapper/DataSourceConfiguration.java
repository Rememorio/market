package com.newbee.maggie.config.mapper;

import java.beans.PropertyVetoException;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 配置datasource到ioc容器里面
 */
@Configuration
//标签表明告诉spring容器需要来到这个类下面检索相关的Bean,在spring初始化的时候就会检索这个类
// 配置mybatis mapper的扫描路径
@MapperScan("com.newbee.maggie.mapper")
public class DataSourceConfiguration {
    @Value("${jdbc.driver}")  // 读取application.properties里面的配置信息  使用下一行的jdbcDriver变量保存
    private String jdbcDriver;
    @Value("${jdbc.url}")
    private String jdbcUrl;
    @Value("${jdbc.username}")
    private String jdbcUsername;
    @Value("${jdbc.password}")
    private String jdbcPassword;

    /**
     * 生成与spring-dao.xml对应的bean dataSource
     */
    @Bean(name = "dataSource")  //用dataSource作为这个Bean的名字
    public ComboPooledDataSource createDataSource() throws PropertyVetoException {  //创建一个bean返回的是数据库的连接池即c3p0连接池
        // 生成datasource实例
        ComboPooledDataSource dataSource = new ComboPooledDataSource();

        // 跟配置文件一样设置以下信息
        // 驱动
        dataSource.setDriverClass(jdbcDriver);
        // 数据库连接URL
        dataSource.setJdbcUrl(jdbcUrl);
        // 设置用户名
        dataSource.setUser(jdbcUsername);
        // 设置用户密码
        dataSource.setPassword(jdbcPassword);

        // 配置c3p0连接池的私有属性
        // 连接池最大线程数
        dataSource.setMaxPoolSize(100);
        // 连接池最小线程数
        dataSource.setMinPoolSize(1);
        // 关闭连接后不自动commit
        dataSource.setAutoCommitOnClose(false);
        // 连接超时时间
        dataSource.setCheckoutTimeout(10000);
        // 连接失败重试次数
        dataSource.setAcquireRetryAttempts(2);
        // 数据库超时时间8小时（28800s），这里隔28780s检查一次连接
        dataSource.setIdleConnectionTestPeriod(28780);
        return dataSource;
    }
}