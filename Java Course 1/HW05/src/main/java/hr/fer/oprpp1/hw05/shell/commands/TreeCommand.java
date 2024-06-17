package hr.fer.oprpp1.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class TreeCommand represents tree command of MyShell program.
 * @author anace
 */
public class TreeCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public TreeCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Prints directory tree.");
		commandDescription.add("FORM: tree [dir-name]");
	}

	/**
	 * Prints directory tree.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String dirName;
		List<String> elements = env.split(arguments);
		if(elements.size() != 1) {
			env.writeln("Invalid tree command format: tree command takes only one argument");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			dirName = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			dirName = elements.get(0).trim();
		}
		Path p = Path.of(dirName);
		if(!Files.isDirectory(p)) {
			env.writeln("Invalid tree command format: tree command argument must be directory path");
			return ShellStatus.CONTINUE;	
		}
		printTree(p, 1, env);
		return ShellStatus.CONTINUE;
	}

	private void printTree(Path p, int level, Environment env) {
		env.writeln(p.getFileName().toString());
		try {
			for (Path p2 : Files.newDirectoryStream(p)) {
				for(int i = 0; i < level; ++i) {
		        	env.write(" ");
		        }
			    if (Files.isDirectory(p2)) {
			        printTree(p2, level + 1, env);
			    } else {
			        env.writeln(p2.getFileName().toString());
			    }
			}
		} catch (ShellIOException | IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "tree";
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
