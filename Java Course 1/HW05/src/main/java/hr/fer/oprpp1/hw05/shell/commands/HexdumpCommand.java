package hr.fer.oprpp1.hw05.shell.commands;

import java.awt.datatransfer.SystemFlavorMap;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class HexdumpCommand represents hexdump command of MyShell program.
 * @author anace
 *
 */
public class HexdumpCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public HexdumpCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Produces hex-output of the given file.");
		commandDescription.add("FORM: [file-name]");
	}

	/**
	 * Produces hex-output of the given file.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		// TODO Auto-generated method stub
		//offset od početka %7 
		//sadržaj linije, svaki bajt odvojen s |
		//stvarni sadržaj
		String fileName;
		List<String> elements = env.split(arguments);
		if(elements.size() != 1) {
			env.writeln("Invalid hex dump command format: hex dump command takes only one argument");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			fileName = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			fileName = elements.get(0).trim();
		}
		
		if(Files.isDirectory(Path.of(fileName))) {
			env.writeln("Invalid hexdump command format: given argument must be file, not directory");
			return ShellStatus.CONTINUE;
		}
		
		int offset = 0;
		
		try(InputStream is = Files.newInputStream(Path.of(fileName))) {
			while(true) {
				byte[] buff = new byte[8];
				List<Byte> list = new ArrayList<>();
				int r = is.read(buff);
				if(r < 1) break;
				env.write(String.format("%08X", offset) + ": ");
				for(int i = 0; i < buff.length; ++i) {
						if(i != buff.length - 1) {
							if(buff[i] == 0) {
								env.write("   ");
							}else {
								env.write(String.format("%02X", buff[i]) + " ");
								list.add(buff[i]);
							}
						}
						else {
							if(buff[i] == 0) {
								env.write("  ");
							}else {
								env.write(String.format("%02X", buff[i]));
								list.add(buff[i]);
							}
						}
				}
				
				env.write("|");
				
				buff = new byte[8];
				r = is.read(buff);
				for(int i = 0; i < buff.length; ++i) {
					if(buff[i] == 0) {
						env.write("   ");
					}else {
						env.write(String.format("%02x", buff[i]) + " ");
						list.add(buff[i]);
					}
				}
				
				env.write("| ");
				
				for(int i = 0; i < list.size(); ++i) {
					if(list.get(i) < 32 || list.get(i) > 127) {
						env.write(".");
					} else {
						env.write(String.format("%c", list.get(i)));
					}
				}
				env.writeln("");
				offset += list.size();
			}
		} catch (IOException e) {
			env.writeln("Cannot read from file: " + Path.of(fileName).getFileName());
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "hexdump";
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
