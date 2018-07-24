package com.onwelo.practice.bts.ftp;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

public interface FtpBaseInterface {

    /**
     * Connects to ftp server and tries to log in the user.
     *
     * @return boolean true if successful, false otherwise.
     */
    boolean openConnection();

    /**
     * Logouts the active user and disconnects from the server.
     */
    void closeConnection();

    /**
     * Retrieve a file from the ftp server.
     *
     * @param outputStream stream the file is read into.
     * @param remotePath   remote path for the file.
     * @return boolean true if successful, false otherwise.
     */

    boolean getFileByRemoteFilePath(OutputStream outputStream, String remotePath);

    /**
     * Retrieve a file from bank dir on the ftp server.
     *
     * @param outputStream stream the file is read into.
     * @return boolean true if successful retrive all files, false otherwise.
     */
    boolean getFileFromBankRemoteDir(OutputStream outputStream);

    /**
     * Store a file on the ftp server.
     *
     * @param inputStream   Stream the new file is read from.
     * @param outboundPath  Remote path the file should be placed at.
     * @return boolean true if successful, false otherwise.
     */

    boolean addFile(InputStream inputStream, String outboundPath);

    /**
     * Store a file on the ftp server.
     *
     * @param sourcePath Local path the file is read from.
     * @param outboundPath   Remote path the file should be placed at.
     * @return boolean true if successful, false otherwise.
     */

    boolean addFileFromLocalDir(String sourcePath, String outboundPath);

    /**
     * Verifies that the connection is valid.
     *
     * @return boolean true if connected, false otherwise.
     */

    boolean isConnected();
}
