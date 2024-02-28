package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import exceptions.InvalidCommandFormatException;

public class CharModifyCommand implements ShellCommand {

    private static final int ALL_CHARS_START_ORD = 32;
    private static final int ALL_CHARS_END_ORD = 127;
    private static final String INVALID_ADD_COMMAND_FORMAT_MESSAGE = "Did not add due to incorrect format.";
    private static final String INVALID_REMOVE_COMMAND_FORMAT_MESSAGE = "Did not remove due to incorrect format.";
    private static final String TOKEN_ALL_CHARS = "all";
    private static final String TOKEN_SPACE = "space";

    private final AsciiArtAlgorithm asciiArtAlgorithm;
    private final boolean shouldAdd;


    public CharModifyCommand(AsciiArtAlgorithm asciiArtAlgorithm, boolean shouldAdd) {
        this.asciiArtAlgorithm = asciiArtAlgorithm;
        this.shouldAdd = shouldAdd;
    }

    @Override
    public void execute(String[] arguments) throws InvalidCommandFormatException {
        if (1 != arguments.length) {
            throw new InvalidCommandFormatException(getErrorMessage());
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

    private void addGeneralChars(String args) throws InvalidCommandFormatException {
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
            throw new InvalidCommandFormatException(getErrorMessage());
        }
    }

    private void addCharRange(int start, int end) {
        assert start <= end;
        assert start >= 0;
        assert start <= 0xFF && end <= 0xFF;

        for (int i = start; i <= end; i++) {
            modifyChar((char) i);
        }
    }

    private String getErrorMessage() {
        if (shouldAdd) {
            return INVALID_ADD_COMMAND_FORMAT_MESSAGE;
        }
        return INVALID_REMOVE_COMMAND_FORMAT_MESSAGE;
    }

    private void modifyChar(char c) {
        if (shouldAdd) {
            asciiArtAlgorithm.addChar(c);
        } else {
            asciiArtAlgorithm.removeChar(c);
        }
    }
}
