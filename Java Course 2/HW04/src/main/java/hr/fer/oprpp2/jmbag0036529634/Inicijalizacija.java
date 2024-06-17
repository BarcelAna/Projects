package hr.fer.oprpp2.jmbag0036529634;

import java.beans.PropertyVetoException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Properties p = new Properties();
		try (FileReader fr = new FileReader(
				sce.getServletContext().getRealPath("WEB-INF/dbsettings.properties"))) {
			p.load(fr);
		} catch (IOException e) {
			throw new RuntimeException("Error while trying to read config file!");
		}

		String host = p.getProperty("host");
		String port = p.getProperty("port");
		String dbName = p.getProperty("name");
		String user = p.getProperty("user");
		String password = p.getProperty("password");

		String connectionURL = "jdbc:derby://"+host+":"+port+"/"+dbName+";user="+user+";password="+password;

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.client.ClientAutoloadedDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);
		cpds.setInitialPoolSize(5);
		cpds.setMaxPoolSize(20);
		cpds.setMinPoolSize(5);
		cpds.setAcquireIncrement(5);

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);

		Connection connection;
		try {
			connection = cpds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			if(!dbExists(connection, "Polls")) {
				createTable(connection, "CREATE TABLE Polls(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, title VARCHAR(150) NOT NULL,  message CLOB(2048) NOT NULL)");
			}
			if(!dbExists(connection, "PollOptions")) {
				createTable(connection, "CREATE TABLE PollOptions(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID) REFERENCES Polls(id))");
			}
			if(PollsEmpty(connection)) {
				populatePolls(connection, sce.getServletContext());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void populatePolls(Connection connection, ServletContext sc)  {
		String polls_file = sc.getRealPath("/WEB-INF/Polls.txt");
		try {
			List<String> polls_lines = Files.readAllLines(Paths.get(polls_file));
			for(String line : polls_lines) {
				String[] elems = line.split("#");

				String reference = elems[2];
				String title = elems[0];
				String message = elems[1];

				try(PreparedStatement pst = connection.prepareStatement("INSERT INTO Polls (title, message) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {
					pst.setString(1, title);
					pst.setString(2, message);
					pst.executeUpdate();
					try(ResultSet generatedKey = pst.getGeneratedKeys()) {
						if(generatedKey.next()) {
							long id = generatedKey.getLong(1);
							insertPollOptions(reference, id, sc, connection);
						}
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void insertPollOptions(String reference, long pollID, ServletContext sc, Connection connection) throws IOException {
		String fileName = sc.getRealPath("/WEB-INF/PollOptions.txt");
		List<String> lines = Files.readAllLines(Path.of(fileName));
		for(String line:lines) {
			String[] elems = line.split("#");
			if(!elems[3].equals(reference)) {
				continue;
			}
			String optionTitle = elems[1];
			String optionLink = elems[2];
			try(PreparedStatement pst = connection.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) VALUES (?, ?, ?, ?)")) {
				pst.setString(1, optionTitle);
				pst.setString(2, optionLink);
				pst.setLong(3, pollID);
				pst.setLong(4, 0);

				pst.executeUpdate();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private boolean PollsEmpty(Connection connection)  {
		boolean empty = true;
		try (PreparedStatement pst =  connection.prepareStatement("SELECT * FROM Polls")) {
			try(ResultSet rs = pst.executeQuery()) {
				if(rs.next()) {
					empty = false;
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return empty;
	}

	private void createTable(Connection connection, String sql) {
		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean dbExists(Connection connection, String tableName) throws SQLException {
		boolean exists = false;

		DatabaseMetaData metaData = connection.getMetaData();

		try(ResultSet rs = metaData.getTables(null, null, tableName.toUpperCase(), null)){
			if(rs.next()) {
				exists = true;
			}
		}

		return exists;
	}

}
