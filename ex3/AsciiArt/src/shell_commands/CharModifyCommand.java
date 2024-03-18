package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.ShellCommandException;

/**
 * A command that adds or removes chars from the charset of the ASCII Art.
 * @author Nimrod M.
 */
public class CharModifyCommand implements ShellCommand {

    private static final int ALL_CHARS_START_ORD = 32;
    private static final int ALL_CHARS_END_ORD = 126;
    private static final String INVALID_ADD_COMMAND_FORMAT_MESSAGE =
            "Did not add due to incorrect format.";
    private static final String INVALID_REMOVE_COMMAND_FORMAT_MESSAGE =
            "Did not remove due to incorrect format.";
    private static final String TOKEN_ALL_CHARS = "all";
    private static final String TOKEN_SPACE = "space";

    private final AsciiArtAlgorithm asciiArtAlgorithm;
    private final boolean shouldAdd;


    /**
     * Constructs a new CharModifyCommand, allowing both removal or addition of chars to the charset
     * of the given ASCII Art algorithm.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     * @param shouldAdd True if the command should add chars, false if it should remove them
     */
    public CharModifyCommand(AsciiArtAlgorithm asciiArtAlgorithm, boolean shouldAdd) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
        this.shouldAdd = shouldAdd;
    }

    /**
     * Executes the command, adding or removing chars from the charset of the ASCII Art.
     * @param arguments The arguments for the command -
     *                  a single char, range of chars, "all" for all chars, or "space" for space
     * @throws ShellCommandException If the command was not executed successfully
     */
    @Override
    public void execute(String[] arguments) throws ShellCommandException {
        if (1 != arguments.length) {
            throw new ShellCommandException(getErrorMessage());
        }

        String token = arguments[0];
        switch (token) {
            case TOKEN_SPACE:
                modifyChar(' ');
                break;
            case TOKEN_ALL_CHARS:
                addCharRange(ALL_CHARS_START_ORD, ALL_CHARS_END_ORD);
                break;
            default:
                addGeneralChars(token);
                break;
        }
    }

    /**
     * Adds a single char or a range of chars to the charset of the ASCII Art.
     * @param args The arguments for the command - a single char or a range of chars
     * @throws ShellCommandException If the command was not executed successfully
     */
    private void addGeneralChars(String args) throws ShellCommandException {
        // Adding a single char
        if (1 == args.length()) {
            modifyChar(args.charAt(0));

        // Adding a range of chars
        } else if ((3 == args.length()) && ('-' == args.charAt(1))) {
            char start = args.charAt(0);
            char end = args.charAt(2);

            if (end < start) {
                char temp = end;
                end = start;
                start = temp;
            }

            addCharRange(start, end);

        // Received invalid format
        } else {
            throw new ShellCommandException(getErrorMessage());
        }
    }

    /**
     * Adds a range of chars to the charset of the ASCII Art.
     * @param start The start of the range
     * @param end The end of the range
     */
    private void addCharRange(int start, int end) {
        assert start <= end;
        assert start >= 0;
        assert start <= 0xFF && end <= 0xFF;

        for (int i = start; i <= end; i++) {
            modifyChar((char) i);
        }
    }

    /**
     * Retrieves the error message for the command.
     * Depends on whether instance is adding or removing chars.
     * @return The error message for the command
     */
    private String getErrorMessage() {
        if (shouldAdd) {
            return INVALID_ADD_COMMAND_FORMAT_MESSAGE;
        }
        return INVALID_REMOVE_COMMAND_FORMAT_MESSAGE;
    }

    /**
     * Adds or removes the given character from the charset of the ASCII Art, depending on the instance.
     * @param c The character to be added or removed from the charset
     */
    private void modifyChar(char c) {
        if (shouldAdd) {
            asciiArtAlgorithm.addChar(c);
        } else {
            asciiArtAlgorithm.removeChar(c);
        }
    }
}
