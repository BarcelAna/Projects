package hr.fer.oprpp1.hw05.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class CatCommand represents cat command of MyShell program.
 * @author anace
 *
 */

public class CatCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public CatCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Opens given file and writes it content to console");
		commandDescription.add("FORM: cat [file-path] OPTIONAL[charset-name]");
	}

	/**
	 * Opens given file and writes it content to console.
	 * If some charset is given as an argument it is used for interpreting file content, otherwise UTF-8 is used.
	 * If cat command is not written with right arguments or if the file can't be opened, appropriate message is written to user.
	 * @param env - shell environmet
	 * @param arguments
	 * @return ShellStatus - CONTINUE if everything is ok, TERMINATE if error occurs.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String path = "";
		String charset = "UTF-8";
		List<String> elements = env.split(arguments); 
		if(elements.size() < 1 || elements.size() > 2) {
			env.writeln("Invalid cat command format: commant takes one or two arguments.");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			path = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			path = elements.get(0).trim();
		}
		if(elements.size() == 2) {
			charset = elements.get(1);
		}
		Path p = Path.of(path);
		if(Files.isDirectory(p)) {
			env.writeln("Invalid cat command format: path should not be directory.");
			return ShellStatus.CONTINUE;
		}
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(
						new BufferedInputStream(
							Files.newInputStream(p)), charset));
			String line;
			env.writeln("Sadrzaj datoteke " + p.getFileName() + " : ");
			while((line = br.readLine()) != null) {
				env.writeln(line);
			}
		} catch (IOException e) {
			env.writeln("File can't be open");
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Return command name
	 * @return command name
	 */
	@Override
	public String getCommandName() {
		return "cat";
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
