package com.benzoft.gravitytubes.commands;

import com.benzoft.gravitytubes.GravityTubes;
import com.benzoft.gravitytubes.commands.gravitytubes.GTCommand;
import com.benzoft.gravitytubes.commands.gravitytubesadmin.GTACommand;

import java.util.stream.Stream;

public final class CommandRegistry {

    public static void registerCommands(final GravityTubes gravityTubes) {
        Stream.of(new GTCommand(gravityTubes, "gravitytubes"), new GTACommand(gravityTubes, "gravitytubesadmin"))
                .forEach(command -> gravityTubes.getCommand(command.getCommandName()).setExecutor(command));
    }
}
