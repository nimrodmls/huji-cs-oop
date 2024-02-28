package ascii_art;

import image.SimpleImage;
import shell_commands.ShellCommand;
import shell_commands.ShellCommandFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Shell {

    private HashMap<String, ShellCommand> commands;

    public Shell() {
        SimpleImage image = null;
        try {
            image = new SimpleImage("C:\\temp\\ex3_examples\\cat.jpeg");
        } catch (Exception e) {
            System.out.println("error");
        }

        char[] ascii = new char[10];
        for (int i = 0; i < 10; i++) {
            ascii[i] = (char)((int)'0' + i);
        }

        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, 256, ascii);
        commands = ShellCommandFactory.createCommands(asciiArtAlgorithm);
    }

    public void run() {
        String userInput = null;

        do {
            System.out.print(">>> ");
            userInput = KeyboardInput.readLine();
            String[] commandTokens = userInput.split(" ");

            ShellCommand command = commands.get(commandTokens[0]);
            if (command == null) {
                System.out.println("Did not execute due to incorrect command.");
                continue;
            }

            String[] commandArgs = Arrays.copyOfRange(commandTokens, 1, commandTokens.length);
            try {
                 command.execute(commandArgs);
            } catch (Exception e) {
                 System.out.println(e.getMessage());
            }

        } while (!userInput.equals("exit"));


    }
}
