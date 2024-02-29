package shell_commands;

import ascii_art.AsciiArtAlgorithm;
import ascii_art.OutputController;

import java.util.HashMap;

public class ShellCommandFactory extends OutputCommand {

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
