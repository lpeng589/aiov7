package com.menyi.aio.web.bol88;

/**
 * <p>Title: </p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ÷‹–¬”Ó</p>
 *
 * @author not attributable
 * @version 1.0
 */
public class AIOBOL88Bean {
    private String userName;
    private String password;
    private int flag;
    public AIOBOL88Bean() {
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public int getFlag() {
        return flag;
    }
}
