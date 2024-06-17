package hr.fer.oprpp1.hw05.shell.commands;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import hr.fer.oprpp1.hw05.shell.Environment;
import hr.fer.oprpp1.hw05.shell.ShellCommand;
import hr.fer.oprpp1.hw05.shell.ShellStatus;

/**
 * Class LsCommand represents ls command of MyShell program.
 * @author anace
 */
public class LsCommand implements ShellCommand {
	/**
	 * List of elements of command descriptions.
	 */
	List<String> commandDescription;
	
	/**
	 * default constructor that writes command description in list
	 */
	public LsCommand() {
		commandDescription = new ArrayList<>();
		commandDescription.add("Lists all files of given directory.");
		commandDescription.add("FORM: ls [dir-name]");
	}

	/**
	 * Lists all files of given directory.
	 * @param env - shell environment object
	 * @param arguments - command arguments
	 * @return shell status CONTINUE
	 */
	@SuppressWarnings("resource")
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		String dirName;
		List<String> elements = env.split(arguments);
		if(elements.size() != 1) {
			env.writeln("Invalid ls command format: ls command takes only one argument");
			return ShellStatus.CONTINUE;
		}
		if(elements.get(0).startsWith("\"")) {
			dirName = elements.get(0).substring(1, elements.get(0).length()-1).trim();
		} else {
			dirName = elements.get(0).trim();
		}
		Path p = Path.of(dirName);
		if(!Files.isDirectory(p)) {
			env.writeln("Invalid ls command format: ls command argument must be directory, not file");
			return ShellStatus.CONTINUE;
		}
		
		Stream<Path> files;
		try {
			files = Files.list(p);
		} catch (IOException e) {
			env.writeln("Cannot list files of directory " + p.getFileName());
			return ShellStatus.CONTINUE;
		}
		Iterator<Path> i = files.iterator();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		while(i.hasNext()) {
			Path current = i.next();
			BasicFileAttributeView faView = Files.getFileAttributeView(current, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
			);
			BasicFileAttributes attributes = null;
			try {
				attributes = faView.readAttributes();
			} catch (IOException e) {
				env.writeln("Cannot reat file " + current.getFileName() + " attributes.");
				return ShellStatus.CONTINUE;
			}
			FileTime fileTime = attributes.creationTime();
			String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
			
			String labels = "";
			if(Files.isDirectory(current)) {
				labels += "d";
			}else {
				labels += "-";
			}
			if(Files.isReadable(current)) {
				labels += "r";
			}else {
				labels += "-";
			}
			if(Files.isWritable(current)) {
				labels += "w";
			}else {
				labels += "-";
			}
			if(Files.isExecutable(current)) {
				labels += "x";
			}else {
				labels += "-";
			}
			Long fileSize = calculateSize(current);
			
			String size = String.format("%10d", fileSize);
			
			env.writeln(labels + size + " " + formattedDateTime + " " + current.getFileName());
		}
		files.close();
		
		return ShellStatus.CONTINUE;
	}

	private Long calculateSize(Path p) {
		Long fileSize = 0L;
		try {
			if(Files.isDirectory(p)) {
				for(Path p2: Files.newDirectoryStream(p)) {
					if(!Files.isDirectory(p2)) {
						fileSize+=Files.size(p2);
					}else {
						fileSize+=calculateSize(p2);
					}
				}
			}else {
				fileSize += Files.size(p);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return fileSize;
	}

	/**
	 * Returns command name.
	 * @return symbol command name
	 */
	@Override
	public String getCommandName() {
		return "ls";
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
