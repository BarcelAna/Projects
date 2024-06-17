package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class ExitCommand represents exit command of MyShell program.
 * @author anace
 *
 */
public class ExitCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public ExitCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Terminates shell.");
	}
	
	/**
	 * Terminates shell.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status TERMINATE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(!arguments.equals("")) {
			env.writeln("Invalid exit command format: exit command takes no argument");
		}
		return ShellStatus.TERMINATE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "exit";
	}

	/**
	 * Returns unmodifiable command description list
	 * @return unmodifiable list
	 */
	@Override
	public List<String> getCommandDescription() {
		return Collections.unmodifiableList(commandDescription);
	}

}
