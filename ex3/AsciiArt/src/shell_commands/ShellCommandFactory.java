package shell_commands;

import ascii_art.AsciiArtAlgorithm;

import java.util.HashMap;

public class ShellCommandFactory {

    public static HashMap<String, ShellCommand> createCommands(AsciiArtAlgorithm asciiArtAlgorithm) {
        HashMap<String, ShellCommand> commands = new HashMap<>();
        commands.put("chars", new CharDisplayCommand(asciiArtAlgorithm));
        /*commands.put("add", new CharAddCommand(asciiArtAlgorithm));
        commands.put("remove", new CharRemoveCommand(asciiArtAlgorithm));
        commands.put("res", new ResolutionModifierCommand(asciiArtAlgorithm));
        commands.put("image", new ImageSelectorCommand(asciiArtAlgorithm));
        commands.put("output", new OutputCommand(asciiArtAlgorithm));
        commands.put("asciiArt", new AsciiArtCommand(asciiArtAlgorithm));*/
        return commands;
    }
}
