package ec.com.tecnointel.soem.serWebClientSri.general;

import java.io.IOException;
import java.io.Writer;

import org.glassfish.jaxb.core.marshaller.CharacterEscapeHandler;

public class NoEscapeHandler implements CharacterEscapeHandler {

	private NoEscapeHandler() {
		super();
	}

	public static final NoEscapeHandler theInstance = new NoEscapeHandler();

	/**
	 * Escape characters inside the buffer and send the output to the writer.
	 *
	 * @param buf        buffer of characters to be encoded
	 * @param start      the index position of the first character that should be
	 *                   encoded
	 * @param len        the number of characters that should be encoded
	 * @param isAttValue true, if the buffer represents an XML tag attribute
	 * @param out        the output stream
	 * @throws IOException if the writing process fails
	 */
	public void escape(char[] buf, int start, int len, boolean isAttValue, Writer out) throws IOException {
		for (int i = start; i < start + len; i++) {
			out.write(buf[i]);
		}
		return;
	}

//	Tambien Funciona asi
//	public void escape(char[] ac, int i, int j, boolean flag, Writer writer) throws IOException {
//		writer.write(ac, i, j);
//	}

}
