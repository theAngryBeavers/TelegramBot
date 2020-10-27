package com.github.theangrybeavers.ydbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;

@PropertySource("classpath:/ydBotUsersSqlQuery.properties")
@Service("YdBotUsersDao")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class YdBotUsersDao {
	private final static Object mutex = new Object();
	
	@Value("${users.dbUrl}")
	private String dbUrl;

	@Value("${users.getUserState}")
	private String getUserStateQuery;

	@Value("${users.updateUserState}")
	private String updateUserStateQuery;

	@Value("${users.insertUser}")
	private String insertUserQuery;

	public Integer getBotState(final long chatId) {
		synchronized (mutex) {
			try (var connection = DriverManager.getConnection(dbUrl);
			     var statement = connection.prepareStatement(getUserStateQuery)) {
				statement.setLong(1, chatId);
				var result = statement.executeQuery();
				return result.next() ? result.getInt(1) : null;
			} catch (SQLException ex) {
				return null;
			}
		}
	}

	public void putUser(final long chatId, final int botState) {
		synchronized (mutex) {
			int updatedQty;
			try (var connection = DriverManager.getConnection(dbUrl);
			     var updateStatement = connection.prepareStatement(updateUserStateQuery);
			     var insertStatement = connection.prepareStatement(insertUserQuery)) {
				connection.setAutoCommit(false);
				updateStatement.setInt(1, botState);
				updateStatement.setLong(2, chatId);
				updatedQty = updateStatement.executeUpdate();

				if (updatedQty == 0) {
					insertStatement.setInt(2, botState);
					insertStatement.setLong(1, chatId);
					insertStatement.executeUpdate();
				}

				connection.commit();
				connection.setAutoCommit(true);
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}
}
