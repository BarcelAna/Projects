package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class HelpCommand represents help command of MyShell program.
 * @author anace
 *
 */
public class HelpCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public HelpCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("If command name is given, this method writes command name and description, otherwise lists names of all supported commands.");
		commandDescription.add("FORM: help OPTIONAL[command-name]");
	}

	/**
	 * If command name is given, method writes command name and description, otherwise lists names of all supported commands.");
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(arguments.isBlank()) {
			env.writeln("Supported commands:");
			for(String commandName : env.commands().keySet()) {
				env.writeln(commandName);
			}
		} else {
			if(env.split(arguments).size() != 1) {
				env.writeln("Invalid help command format: help command takes zero or one argument");
				return ShellStatus.CONTINUE;
			}
			ShellCommand command = env.commands().get(arguments.trim());
			if(command == null) {
				env.writeln("Command name does not exist");
			}else {
				env.writeln(command.getCommandName());
				for(String d : command.getCommandDescription()) {
					env.writeln(d);
				}
			}
			
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "help";
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
