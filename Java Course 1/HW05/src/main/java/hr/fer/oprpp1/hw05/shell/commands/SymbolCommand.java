package hr.fer.oprpp1.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class SymbolCommand represents symbol command of MyShell program.
 * @author anace
 *
 */
public class SymbolCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public SymbolCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Writes symbol representation of the given symbol name, or if the new symbol is given changes current symbol representation to the given new value.");
		commandDescription.add("FORM: symbol [symbol-name] OPTIONAL[new-symbol]");
	}

	/**
	 * Writes symbol representation of the given symbol name, or if the new symbol is given changes current symbol representation to given new value and writes message to user. 
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String[] arr = arguments.split("\\s+");
		if(arr.length == 2) {
			if(arr[0].equals("PROMPT")) {
				char oldPromptSymbol = env.getPromptSymbol();
				env.setPromptSymbol(arr[1].charAt(0));
				env.writeln("Symbol for PROMPT changed from '" + oldPromptSymbol + "' to '" + env.getPromptSymbol() + "'");
			} else if(arr[0].equals("MORELINES")) {
				char oldMorelinesSymbol = env.getMorelinesSymbol();
				env.setMorelinesSymbol(arr[1].charAt(0));
				env.writeln("Symbol for MORELINES changed from '" + oldMorelinesSymbol + "' to '" + env.getMorelinesSymbol() + "'");
			} else if(arr[0].equals("MULTILINE")) {
				char oldMultilineSymbol = env.getMultilineSymbol();
				env.setMultilineSymbol(arr[1].charAt(0));
				env.writeln("Symbol for MULTILINE changed from '" + oldMultilineSymbol + "' to '" + env.getMultilineSymbol() + "'");
			} else {
				env.writeln("Invalid symbol command format");
			}
		} else if(arr.length == 1){
			if(arr[0].equals("PROMPT")) {
				env.writeln("Symbol for PROMPT is '" + env.getPromptSymbol() + "'");
			} else if(arr[0].equals("MORELINES")) {
				env.writeln("Symbol for MORELINES is '" + env.getMorelinesSymbol() + "'");
			} else if(arr[0].equals("MULTILINE")) {
				env.writeln("Symbol for MULTILINE is '" + env.getMultilineSymbol() + "'");
			} else {
				env.writeln("Invalid symbol command format");
			}	
		} else {
			env.writeln("Invalid symbol command format");
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "symbol";
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
