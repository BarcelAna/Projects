package hr.fer.oprpp2.jmbag0036529634.dao.sql;

import hr.fer.oprpp2.jmbag0036529634.dao.DAO;
import hr.fer.oprpp2.jmbag0036529634.model.Poll;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {
	@Override
	public List<Poll> getPolls() {
		List<Poll> polls = new ArrayList<>();
		Connection connection = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = connection.prepareStatement("SELECT id, title FROM Polls")) {
			try(ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {
					long id = rs.getLong(1);
					String title = rs.getString(2);

					Poll p = new Poll();
					p.setId(id);
					p.setTitle(title);

					polls.add(p);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return polls;
	}

	@Override
	public Poll getPoll(long pollID) {
		Poll poll = new Poll();
		Connection connection = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = connection.prepareStatement("SELECT * FROM Polls WHERE id=?")) {
			pst.setLong(1, pollID);
			try(ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {
					long id = rs.getLong(1);
					String title = rs.getString(2);
					String message = rs.getString(3);

					poll.setId(id);
					poll.setTitle(title);
					poll.setMessage(message);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return poll;
	}

	@Override
	public List<PollOption> getPollOptions(long pollID) {
		List<PollOption> pollOptions = new ArrayList<>();
		Connection connection = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = connection.prepareStatement("SELECT * FROM PollOptions WHERE pollID=? order by votesCount desc")) {
			pst.setLong(1, pollID);
			try(ResultSet rs = pst.executeQuery()) {
				while(rs.next()) {
					long id = rs.getLong(1);
					String title = rs.getString(2);
					String link = rs.getString(3);
					long votesCnt = rs.getLong(5);

					PollOption pOpt = new PollOption();
					pOpt.setId(id);
					pOpt.setOptionTitle(title);
					pOpt.setOptionLink(link);
					pOpt.setPollID(pollID);
					pOpt.setId(id);
					pOpt.setVotesCount(votesCnt);

					pollOptions.add(pOpt);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return pollOptions;
	}

	@Override
	public void upadateVotes(long id) {
		Connection connection = SQLConnectionProvider.getConnection();

		try(PreparedStatement pst = connection.prepareStatement("UPDATE PollOptions SET votesCount=votesCount+1 WHERE id=?")) {
			pst.setLong(1, id);
			pst.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Long getPollID(long pollOptId) {
		Long pollID = null;
		Connection connection = SQLConnectionProvider.getConnection();
		try(PreparedStatement pst = connection.prepareStatement("SELECT pollID FROM PollOptions WHERE id=?")) {
			pst.setLong(1, pollOptId);
			try(ResultSet rs = pst.executeQuery()) {
				if(rs.next()) {
					pollID = rs.getLong(1);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return pollID;
	}
}