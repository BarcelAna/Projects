package hr.fer.oprpp1.hw05.shell;

import java.util.List;
/**
 * Shell Command interface lists all methods that allows shell to work and execute commands.
 * @author anace
 *
 */
public interface ShellCommand {
	/**
	 * Executes command
	 * @param env - shell environment
	 * @param arguments - everything user entered after the command name
	 * @return shell status
	 */
	ShellStatus executeCommand(Environment env, String arguments);
	
	/**
	 * Returns command name.
	 * @return command name
	 */
	String getCommandName();
	
	/**
	 * Returns command description ad read only list.
	 * @return command description
	 */
	List<String> getCommandDescription();
}
