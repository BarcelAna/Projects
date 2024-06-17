package hr.fer.oprpp1.hw05.shell.commands;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class MkdirCommand represents mkdir command of MyShell program.
 * @author anace
 *
 */
public class MkdirCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public MkdirCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Creates new directory with given path.");
		commandDescription.add("FORM: mkdir [dir-name]");
	}

	/**
	 * Creates new directory with given path.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String dirName;
		List<String> elements = env.split(arguments);
		if(elements.size() != 1) {
			env.writeln("Invalid mkdir command format: mkdir command takes only one argument");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			dirName = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			dirName = elements.get(0).trim();
		}
		File semip = new File(dirName.substring(0, dirName.lastIndexOf("\\")));
		if(!semip.exists()) {
			env.writeln("Given path does not exist");
			return ShellStatus.CONTINUE;
		}
		File p = new File(dirName);
		if(p.exists())
		p.mkdirs();
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "mkdir";
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
