package org.darr.mirr.command;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.darr.mirr.command.impl.PrintAreaZonesCommand;
import org.darr.mirr.command.impl.models.AccommodateModelFilesFromDirCommand;
import org.darr.mirr.command.impl.models.CollectModelFilesIntoDirCommand;
import org.darr.mirr.command.impl.profile.CharacterStatsToXlsCommand;
import org.darr.mirr.command.impl.profile.XlsToCharacterStatsCommand;
import org.darr.mirr.command.impl.scene.CopyUnitArenaLocationCommand;
import org.darr.mirr.command.impl.scene.FixPassPointNameSceneCommand;
import org.darr.mirr.command.impl.scene.InsertChildSceneBlockCommand;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Factory class to determine {@link Command} implementor by command name passed by cmd arguments.
 * I have try to follow dependency less principle and keep application code as simple as possible.
 * Therefore, all new {@link Command} implementors have to be registered manually.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandFactory {
    public static final CommandFactory INSTANCE = new CommandFactory();
    private final Map<String, Command> commands = new HashMap<>();

    {
        commands.put("copy_unit_arena_location", new CopyUnitArenaLocationCommand());
        commands.put("print_area_zones", new PrintAreaZonesCommand());
        commands.put("insert_child_scene_block", new InsertChildSceneBlockCommand());
        commands.put("collect_model_files_into_dir", new CollectModelFilesIntoDirCommand());
        commands.put("accommodate_model_files_from_dir", new AccommodateModelFilesFromDirCommand());
        commands.put("fix_pass_point_name_scene", new FixPassPointNameSceneCommand());
        commands.put("character_stats_to_xls", new CharacterStatsToXlsCommand());
        commands.put("xls_to_character_stats", new XlsToCharacterStatsCommand());
    }

    public Optional<Command> get(String commandName) {
        return Optional
                .ofNullable(commandName)
                .map(commands::get);
    }
}
