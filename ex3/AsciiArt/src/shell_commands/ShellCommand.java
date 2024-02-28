package shell_commands;

import ascii_art.Shell;
import exceptions.InvalidCommandFormatException;

public interface ShellCommand {

    void execute(String[] arguments) throws InvalidCommandFormatException;
}
