package ascii_art;

import exceptions.ShellCommandException;
import image.SimpleImage;
import shell_commands.OutputCommand;
import shell_commands.ShellCommand;
import shell_commands.ShellCommandFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Shell {

    private static final String COMMAND_PROMPT_PREFIX = ">>> ";
    private static final String DEFAULT_IMAGE_LOAD_ERROR_MESSAGE = "Error loading default image.";
    private static final String INVALID_COMMAND_MESSAGE = "Did not execute due to incorrect command.";
    private static final String EXIT_COMMAND = "exit";
    private static final String DEFAULT_IMAGE_PATH = "cat.jpeg";
    private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
    private static final int DEFAULT_RESOLUTION = 128;
    private static final String DEFAULT_OUTPUT_PATH = "out.html";
    private static final OutputMethod DEFAULT_OUTPUT_METHOD = OutputMethod.CONSOLE;

    private final HashMap<String, ShellCommand> commands;

    public Shell() {
        SimpleImage defaultImage = null;
        try {
            defaultImage = new SimpleImage(DEFAULT_IMAGE_PATH);
        } catch (Exception e) {
            System.out.println(DEFAULT_IMAGE_LOAD_ERROR_MESSAGE);
        }

        // Starting the algorithm with default values
        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(
                defaultImage, DEFAULT_RESOLUTION, DEFAULT_CHARSET);
        OutputController outputController = new OutputController(
                DEFAULT_OUTPUT_METHOD, DEFAULT_OUTPUT_PATH);
        commands = ShellCommandFactory.createCommands(asciiArtAlgorithm, outputController);
    }

    public void run() {
        String userInput = null;

        do {
            System.out.print(COMMAND_PROMPT_PREFIX);
            userInput = KeyboardInput.readLine();
            String[] commandTokens = userInput.split(" ");

            ShellCommand command = commands.get(commandTokens[0]);
            if (command == null) {
                System.out.println(INVALID_COMMAND_MESSAGE);
                continue;
            }

            String[] commandArgs = Arrays.copyOfRange(commandTokens, 1, commandTokens.length);
            try {
                 command.execute(commandArgs);
            } catch (ShellCommandException e) {
                 System.out.println(e.getMessage());
            }

        } while (!userInput.equals(EXIT_COMMAND));
    }

    public static void main(String[] args) {
        Shell shell = new Shell();
        shell.run();
    }
}
