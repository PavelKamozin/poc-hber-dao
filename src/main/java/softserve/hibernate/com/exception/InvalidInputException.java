package softserve.hibernate.com.exception;

import java.io.IOException;

public class InvalidInputException extends RuntimeException {
    private IOException ex;

    public InvalidInputException(String error, IOException ex) {
        super(error);
        this.ex = ex;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
        this.ex.printStackTrace();
    }
}
