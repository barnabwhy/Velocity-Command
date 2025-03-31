package cc.barnab;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.command.argument.EntityArgumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class VelocityCommand implements ModInitializer {
	public static final String MOD_ID = "velocity-command";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(literal("velocity")
					.requires(source -> source.hasPermissionLevel(2))
					.then(argument("target", EntityArgumentType.entities())
						.then(argument("velocity", BlockPosArgumentType.blockPos())
							.executes(ChangeVelocityCommand::executeCommand)
						)
					)
			);
		});
	}
}