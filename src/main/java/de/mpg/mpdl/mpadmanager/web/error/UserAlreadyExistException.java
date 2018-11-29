package de.mpg.mpdl.mpadmanager.web.error;

public class UserAlreadyExistException extends RuntimeException{
	
    private static final long serialVersionUID = 6666666666666666666L;

    public UserAlreadyExistException() {
        super();
    }

    public UserAlreadyExistException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistException(final String message) {
        super(message);
    }

    public UserAlreadyExistException(final Throwable cause) {
        super(cause);
    }

}
