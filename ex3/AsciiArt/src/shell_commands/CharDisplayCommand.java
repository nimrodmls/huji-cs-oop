package shell_commands;

import ascii_art.AsciiArtAlgorithm;

public class CharDisplayCommand implements ShellCommand {

    private final AsciiArtAlgorithm asciiArtAlgorithm;

    public CharDisplayCommand(AsciiArtAlgorithm asciiArtAlgorithm) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
    }

    @Override
    public void execute(String[] args) {
        char[] charset = asciiArtAlgorithm.getCharset();
        for (char c : charset) {
            System.out.print(c + " ");
        }
    }
}
