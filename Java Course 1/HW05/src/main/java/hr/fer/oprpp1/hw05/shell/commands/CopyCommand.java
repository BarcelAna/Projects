package hr.fer.oprpp1.hw05.shell.commands;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellIOException;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class CopyCommand represents copy command of MyShell program.
 * @author anace
 *
 */
public class CopyCommand implements ShellCommand {

	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public CopyCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Copies first file content to second file. If the second file is directory, first file is copied to given directory.");
		commandDescription.add("FORM: copy [file-name] [file-name]");
		commandDescription.add("OPTIONAL: second file name can be directory");
	}
	
	/**
	 * Writes symbol representation of the given symbol name, or if the new symbol is given changes current symbol representation to given new value and writes message to user. 
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 * @throws ShellIOException 
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) throws ShellIOException {
		String firstFile;
		String secondFile ;
		List<String> elements = env.split(arguments);
		if(elements.size() != 2) {
			env.writeln("Invalid copy command format: copy command must take two arguments");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			firstFile = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			firstFile = elements.get(0).trim();
		}
		if(elements.get(1).startsWith("\"")) {
			secondFile = elements.get(1).substring(1, elements.get(1).length()-1).trim();
		} else {
			secondFile = elements.get(1).trim();
		}
		Path first = Paths.get(firstFile);
		Path second = Paths.get(secondFile);
		
		if(Files.isDirectory(first)) {
			env.writeln("Incorrect copy command format: first argument must be a file");
			return ShellStatus.CONTINUE;
		}
		
		if(Files.isDirectory(second)) {
			second = Paths.get(secondFile, first.getFileName().toString());
		}
		
		if(Files.exists(second)) {
			env.writeln(second + " file already exists. Can I overwrite it? Y/N?");
			String response = env.readLine();
			if(response.toLowerCase().equals("n")) {
				return ShellStatus.CONTINUE;
			}
		}
		
		BufferedReader br = null;
		try {
			 br = new BufferedReader(
					new InputStreamReader(
							new BufferedInputStream(
									Files.newInputStream(first))));
		} catch (IOException e) {
			env.writeln("File " + first.getFileName() + " can't be open");
			return ShellStatus.CONTINUE;
		}
			
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(
					new OutputStreamWriter(
							new BufferedOutputStream(
									Files.newOutputStream(second))));
		} catch (IOException e) {
			env.writeln("File " + second.getFileName() + " can't be open");
			return ShellStatus.CONTINUE;
		}
			
		String line = "";
		try {
			while((line=br.readLine()) != null) {
				try {
					bw.write(line + '\n');
				} catch (IOException e) {
					env.writeln("Can't write into file " + second.getFileName());
					return ShellStatus.CONTINUE;
				}	
			}
		} catch (ShellIOException | IOException e1) {
			e1.printStackTrace();
		}
		
		try {
			bw.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Return command name
	 * @return command name
	 */
	@Override
	public String getCommandName() {
		return "copy";
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
