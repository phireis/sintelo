

package sintelo.glc;



public class GlcException extends RuntimeException{

    public GlcException(Throwable arg0) {
        super(arg0);
    }

    public GlcException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public GlcException(String arg0) {
        super(arg0);
    }

    public GlcException() {
    }



}
