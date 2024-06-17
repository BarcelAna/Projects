package hr.fer.oprpp1.hw05.shell;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

import hr.fer.oprpp1.hw05.shell.commands.CatCommand;
import hr.fer.oprpp1.hw05.shell.commands.CharsetsCommand;
import hr.fer.oprpp1.hw05.shell.commands.CopyCommand;
import hr.fer.oprpp1.hw05.shell.commands.ExitCommand;
import hr.fer.oprpp1.hw05.shell.commands.HelpCommand;
import hr.fer.oprpp1.hw05.shell.commands.HexdumpCommand;
import hr.fer.oprpp1.hw05.shell.commands.LsCommand;
import hr.fer.oprpp1.hw05.shell.commands.MkdirCommand;
import hr.fer.oprpp1.hw05.shell.commands.SymbolCommand;
import hr.fer.oprpp1.hw05.shell.commands.TreeCommand;

/**
 * Program works as a simple sheel and offers users communication with file system.
 * @author anace
 *
 */
public class MyShell {
	/**
	 * Map of shell commands.
	 * Each entry has a key which is command name, and value which is object representation of command.
	 */
	static TreeMap<String, ShellCommand> map = new TreeMap<>();
	static {
		map.put("cat", new CatCommand());
		map.put("charsets", new CharsetsCommand());
		map.put("copy", new CopyCommand());
		map.put("exit", new ExitCommand());
		map.put("help", new HelpCommand());
		map.put("hexdump", new HexdumpCommand());
		map.put("ls", new LsCommand());
		map.put("mkdir", new MkdirCommand());
		map.put("symbol", new SymbolCommand());
		map.put("tree", new TreeCommand());
	}
	static SortedMap<String, ShellCommand> commands = Collections.unmodifiableSortedMap(map);
	private static Scanner sc;
	
	public static void main(String[] args) {
		EnvironmentImpl environment = new EnvironmentImpl();
		environment.writeln("Welcome to MyShell v 1.0");
		
		ShellStatus status = ShellStatus.CONTINUE;
		do {
			try {
				environment.write(environment.getPromptSymbol() + " ");
				String l = environment.readLine();
				List<String> elements = environment.split(l);
				String commandName = elements.get(0);
				String arguments = "";
				if(elements.size() > 1) {
					for(int i = 1; i < elements.size(); ++i) {
						arguments += elements.get(i) + " ";
					}
					arguments = arguments.trim();
				}
				ShellCommand command = commands.get(commandName);
				if(command != null)status = command.executeCommand(environment, arguments);
				else {
					environment.writeln("Command does not exist.");
				}
			} catch(ShellIOException e) {
				status = ShellStatus.TERMINATE;
			}
			
		} while(status != ShellStatus.TERMINATE); 
		sc.close();
	}
	
	/**
	 * Implementation of shell environment that offers users methods to communicate with the system
	 * @author anace
	 *
	 */
	private static class EnvironmentImpl implements Environment {

		private char promptSymbol;
		private char multilineSymbol;
		private char moreLinesSymbol;
		
		public EnvironmentImpl() {
			this.promptSymbol = '>';
			this.multilineSymbol = '|';
			this.moreLinesSymbol = '\\';
		}


		/**
		 * Reads user's input and returns it as a string.
		 * @return input
		 */
		@Override
		public String readLine() throws ShellIOException {
			try {
				String result = "";
				sc = new Scanner(System.in);	
				String line = "";
				line = sc.nextLine();
				while(line.charAt(line.length()-1) == getMorelinesSymbol()) {
					write(getMultilineSymbol() + " ");
					line = line.substring(0, line.length() - 1).trim();
					result += line + " ";
					line = sc.nextLine();
				}
				result += line;
				result = result.trim();
				return result;
			} catch(Exception e) {
				throw new ShellIOException();
			}
			
		}

		/**
		 * Writes given text to console without starting a new line.
		 */
		@Override
		public void write(String text) throws ShellIOException {
			try {
				System.out.print(text);
			} catch(Exception e) {
				throw new ShellIOException();
			}
			
		}

		/**
		 * Writes given text to console and after that starts new line.
		 */
		@Override
		public void writeln(String text) throws ShellIOException {
			try {
				System.out.println(text);
			} catch(Exception e) {
				throw new ShellIOException();
			}
		}

		@Override
		public SortedMap<String, ShellCommand> commands() {
			return commands;
		}

		/**
		 * Returns multiline symbol used in this shell
		 * @return multiline symbol
		 */
		@Override
		public Character getMultilineSymbol() {
			return multilineSymbol;
		}

		/**
		 * Sets given symbol as the new multiline symbol.
		 */
		@Override
		public void setMultilineSymbol(Character symbol) {
			this.multilineSymbol = symbol;
		}

		/**
		 * Returns prompt symbol used in this shell
		 * @return prompt symbol
		 */
		@Override
		public Character getPromptSymbol() {
			return promptSymbol;
		}

		/**
		 * Sets given symbol as the new prompt symbol.
		 */
		@Override
		public void setPromptSymbol(Character symbol) {
			this.promptSymbol = symbol;
		}

		/**
		 * Returns morelines symbol used in this shell
		 * @return morelines symbol
		 */
		@Override
		public Character getMorelinesSymbol() {
			return moreLinesSymbol;
		}

		/**
		 * Sets given symbol as the new morelines symbol.
		 */
		@Override
		public void setMorelinesSymbol(Character symbol) {
			this.moreLinesSymbol = symbol;
			
		}
		
	}

}
