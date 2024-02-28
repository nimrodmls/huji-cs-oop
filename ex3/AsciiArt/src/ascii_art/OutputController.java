package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

public class OutputController {

    private static final String DEFAULT_HTML_FONT = "Courier New";

    private final HtmlAsciiOutput htmlAsciiOutput;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private OutputMethod outputMethod;

    public OutputController(OutputMethod defaultOutputMethod, String htmlOutPath) {
        this.outputMethod = defaultOutputMethod;
        // Taking advantage of the fact that the Ctor of HtmlAsciiOutput does not write the actual file,
        // so we can construct it without the file existing before actually writing to it.
        htmlAsciiOutput = new HtmlAsciiOutput(htmlOutPath, DEFAULT_HTML_FONT);
        consoleAsciiOutput = new ConsoleAsciiOutput();
    }

    public void setOutputMethod(OutputMethod outputMethod) {
        this.outputMethod = outputMethod;
    }

    public void toOutput(char[][] asciiArt) {
        switch (outputMethod) {
            case CONSOLE:
                consoleAsciiOutput.out(asciiArt);
                break;
            case HTML:
                htmlAsciiOutput.out(asciiArt);
                break;
        }
    }
}
