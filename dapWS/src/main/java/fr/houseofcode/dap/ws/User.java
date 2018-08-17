package fr.houseofcode.dap.ws;

import java.io.Serializable;

/**
 * @author djer
 *
 */
public class User implements Serializable{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6952674130801816604L;
    private String code;

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

}
