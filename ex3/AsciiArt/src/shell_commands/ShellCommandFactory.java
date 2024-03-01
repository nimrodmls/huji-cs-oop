package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputController;

import java.util.HashMap;

/**
 * A factory for creating shell commands for the ASCII Art program.
 * @author Nimrod M.
 */
public class ShellCommandFactory {

    /**
     * Constructs a new ShellCommandFactory object.
     * Per this implementation, the constructor is not used
     * and no object of this class differs from another.
     */
    public ShellCommandFactory() {}

    /**
     * Creates a map of the commands available for the ASCII Art program,
     * mapping between the literal command string and the proper command object.
     * @param asciiArtAlgorithm The algorithm to be used for the ASCII Art
     * @param outputController The output controller to be used for the ASCII Art
     * @return A map of the commands available for the ASCII Art program
     */
    public static HashMap<String, ShellCommand> createCommands(
            AsciiArtAlgorithm asciiArtAlgorithm, OutputController outputController) {
        HashMap<String, ShellCommand> commands = new HashMap<>();
        commands.put("chars", new CharDisplayCommand(asciiArtAlgorithm));
        commands.put("add", new CharModifyCommand(asciiArtAlgorithm, true));
        commands.put("remove", new CharModifyCommand(asciiArtAlgorithm, false));
        commands.put("res", new ResolutionModifyCommand(asciiArtAlgorithm));
        commands.put("image", new ImageSelectorCommand(asciiArtAlgorithm));
        commands.put("output", new OutputCommand(outputController));
        commands.put("asciiArt", new AsciiArtCommand(asciiArtAlgorithm, outputController));
        return commands;
    }
}
