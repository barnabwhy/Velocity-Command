package cc.barnab;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.PosArgument;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;

import java.util.List;
import java.util.Optional;

public class ChangeVelocityCommand {
    public static int executeCommand(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerCommandSource source = context.getSource();

        EntitySelector target = context.getArgument("target", EntitySelector.class);
        List<? extends Entity> entities = target.getEntities(source);

        PosArgument posArg = context.getArgument("velocity", PosArgument.class);

        for (Entity entity : entities) {
            Vec3d oldVec = entity.getVelocity();
            Vec3d newVec = posArg.getPos(source.withPosition(oldVec).withRotation(entity.getRotationClient()));

            if (entity instanceof ServerPlayerEntity player) {
                player.networkHandler.sendPacket(new ExplosionS2CPacket(new Vec3d(0, -10000000, 0), Optional.ofNullable(newVec.subtract(oldVec)), ParticleTypes.EFFECT, SoundEvents.BLOCK_NOTE_BLOCK_BANJO));
            } else {
                entity.velocityModified = true;
            }

            entity.velocityDirty = true;
            entity.setVelocity(newVec);
        }

        source.sendFeedback(() -> {
            if (entities.size() == 1)
                return Text.literal("Modified velocity of ").append(entities.getFirst().getName());
            else
                return Text.literal(String.format("Modified velocity of %d entities", entities.size()));
        }, false);

        return 1;
    }
}