package ascii_art;

import shell_commands.ShellCommand;
import shell_commands.ShellCommandFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Shell {

    private HashMap<String, ShellCommand> commands;

    public Shell() {
        commands = new ShellCommandFactory().createCommands();
    }

    public void run() {
        String userInput = null;

        do {
             userInput = KeyboardInput.readLine();
             String[] commandTokens = userInput.split(" ");
             // Executing the command - The first token is the command name, the rest are the arguments
             commands.get(commandTokens[0]).execute(Arrays.copyOfRange(commandTokens, 1, commandTokens.length));

        } while (!userInput.equals("exit"));


    }
}
