package io.github.teonistor.suhc;

import static java.lang.String.format;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Registrar {
	static final String HEADER = "Name,Email\n";
	
	private File file;
	private boolean append;
	
	public Registrar (String file) {
		this.file = new File(file);
		append = this.file.exists();
	}
	
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
