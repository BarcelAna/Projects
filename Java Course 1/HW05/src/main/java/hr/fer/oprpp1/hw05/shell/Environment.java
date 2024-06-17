package hr.fer.oprpp1.hw05.shell;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interface Envinonment represents list of methods available to shell commands for communication with users
 * @author anace
 *
 */
public interface Environment {
	/**
	 * Reads line from console.
	 * @return read line
	 * @throws ShellIOException - if reading fails
	 */
	String readLine() throws ShellIOException;
	
	/**
	 * Writes line to console.
	 * @throws ShellIOException - if writing fails
	 */
	void write(String text) throws ShellIOException;
	
	/**
	 * Writes line to console and starts the next line.
	 * @throws ShellIOException - if writing fails
	 */
	void writeln(String text) throws ShellIOException;
	
	/**
	 * Returs unmodifiable map of shell commands.
	 * @return SortedMap of commands
	 */
	SortedMap<String, ShellCommand> commands();
	
	/**
	 * Returns shell symbol for multiline.
	 * @return multiline symbol
	 */
	Character getMultilineSymbol();
	
	/**
	 * Sets shell symbol for multiline to given value.
	 */
	void setMultilineSymbol(Character symbol);
	
	/**
	 * Returns shell symbol for prompt.
	 * @return prompt symbol
	 */
	Character getPromptSymbol();
	
	/**
	 * Sets shell symbol for prompt to given value.
	 */
	void setPromptSymbol(Character symbol);
	
	/**
	 * Returns shell symbol for more lines.
	 * @return more lines symbol
	 */
	Character getMorelinesSymbol();
	
	/**
	 * Sets shell symbol for more lines to given value.
	 */
	void setMorelinesSymbol(Character symbol);
	
	/**
	 * Simplifies reading of arguments from console.
	 * If arguments are inside quotes than spaces are not skipped, otherwise this function uses blanks to split user's input.
	 * @param user's input
	 * @return List of entered command arguments including command name
	 */
	public default List<String> split(String l) {
		List<String> matchList = new ArrayList<String>();
		Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
		Matcher regexMatcher = regex.matcher(l);
		while (regexMatcher.find()) {
		    matchList.add(regexMatcher.group());
		}
		return matchList;
	}
}
