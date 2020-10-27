package com.github.theangrybeavers.ydbot.dao;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.DriverManager;
import java.sql.SQLException;

@PropertySource("classpath:/ydBotUploadedFilesSqlQuery.properties")
@Service("YdBotUploadedFilesDao")
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class YdBotUploadedFilesDao {
	private final static Object mutex = new Object();

	@Value("${uploadedfiles.dbUrl}")
	private String dbUrl;

	@Value("${uploadedfiles.getTelegramFileId}")
	private String getTelegramFileIdQuery;

	@Value("${uploadedfiles.insertFile}")
	private String insertFileQuery;

	public void putFile(final String videoId,
	                    final String telegramFileId,
	                    final int mediaType) {
		synchronized (mutex) {
			if (getUploadedFile(videoId, mediaType) == null) {
				try (var connection = DriverManager.getConnection(dbUrl);
				     var statement = connection.prepareStatement(insertFileQuery)) {
					statement.setString(1, videoId);
					statement.setString(2, telegramFileId);
					statement.setInt(3, mediaType);
					statement.executeUpdate();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public String getUploadedFile(final String videoId, final int mediaType) {
		synchronized (mutex) {
			try (var connection = DriverManager.getConnection(dbUrl);
			     var statement = connection.prepareStatement(getTelegramFileIdQuery)) {
				statement.setString(1, videoId);
				statement.setInt(2, mediaType);
				var result = statement.executeQuery();
				return result.next()
						? result.getString(1)
						: null;
			} catch (SQLException ex) {
				return null;
			}
		}
	}
}
