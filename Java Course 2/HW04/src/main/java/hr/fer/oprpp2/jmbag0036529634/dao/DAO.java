package hr.fer.oprpp2.jmbag0036529634.dao;

import hr.fer.oprpp2.jmbag0036529634.model.Poll;
import hr.fer.oprpp2.jmbag0036529634.model.PollOption;

import java.util.List;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	List<Poll> getPolls();

	Poll getPoll(long pollID);

	List<PollOption> getPollOptions(long pollID);

	void upadateVotes(long id);

	Long getPollID(long id);
}