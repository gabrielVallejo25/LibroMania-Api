package es.gvallejo.libromaniaapi.dto.exception;

public class LibroManiaException extends Exception {

	/**
	* 
	*/
	private static final long serialVersionUID = -5497494553617739802L;
	private String msg;

	/**
	 * 
	 */

	public LibroManiaException(String msg) {
		super(msg);
	}

	public String getMsg() {
		return msg;
	}

}
