package org.darr.mirr;

import lombok.extern.slf4j.Slf4j;
import org.darr.mirr.command.CommandFactory;
import org.darr.mirr.util.CmdArgExtractor;

/**
 * Entrypoint of application execution.
 */
@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        log.info("Start.");

        var commandArg = CmdArgExtractor.getArgMandatory(args, "command");
        var commandName = CmdArgExtractor.getArgValue(commandArg);
        var commandOptional = CommandFactory.INSTANCE.get(commandName);

        if (commandOptional.isPresent()) {
            log.info("Run command : command_name={}", commandName);
            commandOptional.get().run(args);
        } else {
            log.error("Command has not been found : command_name={}", commandName);
        }

        log.info("Finished.");
    }
}