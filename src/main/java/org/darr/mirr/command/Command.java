package org.darr.mirr.command;

/**
 * Interface for util commands.
 * Each command have to parse specific for it cmd args.
 * If {@link Command} implementation object is executed
 * it means {@link CommandFactory} choose it by command name passed by cmd arguments.
 */
public interface Command {

    /**
     * Run command execution flow.
     *
     * @param args cmd arguments.
     * @throws Exception exception is risen during command execution.
     */
    void run(String[] args) throws Exception;
}
