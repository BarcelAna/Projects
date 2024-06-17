package hr.fer.oprpp1.hw05.shell.commands;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class CharsetsCommand represents charsets command of MyShell program.
 * @author anace
 *
 */
public class CharsetsCommand implements ShellCommand{

	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public CharsetsCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Writes all available charsets' names");
	}

	/**
	 * Writes all available charsets' names.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		if(!arguments.equals("")) {
			env.writeln("Invalid charsets command format: charsets command takes no arguments");
			return ShellStatus.CONTINUE;
		}
		Map<String, Charset> map = Charset.availableCharsets();
		env.writeln("Available charsets: ");
		for(String charsetName : map.keySet()) {
			env.writeln(charsetName);
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "charsets";
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
