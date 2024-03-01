package shell_commands;

import exceptions.ShellCommandException;

/**
 * An interface for a shell command that can be executed by the user.
 * @author Nimrod M.
 */
public interface ShellCommand {

    /**
     * Executes the command with the given arguments.
     * @param arguments The arguments for the command
     * @throws ShellCommandException If the command was not executed successfully
     */
    void execute(String[] arguments) throws ShellCommandException;
}
