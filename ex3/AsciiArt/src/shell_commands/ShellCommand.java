package shell_commands;

import exceptions.ShellCommandException;

public interface ShellCommand {

    void execute(String[] arguments) throws ShellCommandException;
}
