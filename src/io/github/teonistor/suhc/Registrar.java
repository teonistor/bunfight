package io.github.teonistor.suhc;

import static java.lang.String.format;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Utility to perform user input sanitization and record records to file in CSV format
 * @author Copyright (C) 2017 Teodor Gherasim Nistor
 * <p>
 * This sotware is distributed under the terms of the GNU General Public License, version 3 or later; see www.gnu.org/licenses
 */
public class Registrar {
	static final String HEADER = "Name,Email\n";
	
	private File file;
	private boolean append;
	
	/**
	 * Construct a new Registrar using the given file path
	 * @param file Path to file where registrations are to be recorded
	 */
	public Registrar (String file) {
		this.file = new File(file);
		append = this.file.exists();
	}
	
	/**
	 * Register given user details to CSV
	 * @param name User name
	 * @param email User email
	 */
	public void register (String name, String email) {
		try (FileWriter fw = new FileWriter(file, append)) {
			if (!append) {
				append = true;
				fw.write(HEADER);
			}

			fw.write(format("\"%s\",\"%s%s\"\n",
					name.replace('\"', '”'),
					email.replace('\"', '”'),
					email.indexOf('@') > -1 ? "" : "@soton.ac.uk"));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
