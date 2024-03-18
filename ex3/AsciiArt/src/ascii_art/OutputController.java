package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;

/**
 * Controls the output of the ASCII art, to either the console or an HTML file.
 * @author Nimrod M.
 */
public class OutputController {

    private static final String DEFAULT_HTML_FONT = "Courier New";

    private final HtmlAsciiOutput htmlAsciiOutput;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private OutputMethod outputMethod;

    /**
     * Constructs a new OutputController with the given default output method and HTML output file path.
     * @param defaultOutputMethod The default output method
     * @param htmlOutPath The path of the HTML output file, for when the output method is HTML (mandatory)
     */
    public OutputController(OutputMethod defaultOutputMethod, String htmlOutPath) {
        this.outputMethod = defaultOutputMethod;
        // Taking advantage of the fact that the Ctor of HtmlAsciiOutput does not write the actual file,
        // so we can construct it without the file existing before actually writing to it.
        htmlAsciiOutput = new HtmlAsciiOutput(htmlOutPath, DEFAULT_HTML_FONT);
        consoleAsciiOutput = new ConsoleAsciiOutput();
    }

    /**
     * Sets the output method to be used. The HTML output path will not be changed.
     * @param outputMethod The output method to be used
     */
    public void setOutputMethod(OutputMethod outputMethod) {
        this.outputMethod = outputMethod;
    }

    /**
     * Outputs the given ASCII art to the selected output method.
     * @param asciiArt The ASCII art to be output
     */
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
