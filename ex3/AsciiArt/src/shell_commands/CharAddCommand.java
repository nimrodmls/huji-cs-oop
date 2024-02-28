package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import main.AsciiArt;

public class CharAddCommand implements ShellCommand {

    private final AsciiArtAlgorithm asciiArtAlgorithm;

    public CharAddCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    @Override
    public void execute(String[] args) {
        if (1 != args.length) {
            System.out.println("Invalid number of arguments");
            return;
        }
        String token = args[0];
    }

    private void addToCharset(char[] chars) {
        for (char c : chars) {
            asciiArtAlgorithm.addChar(c);
        }
    }
}
