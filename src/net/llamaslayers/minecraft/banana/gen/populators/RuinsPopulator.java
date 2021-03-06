package net.llamaslayers.minecraft.banana.gen.populators;

import java.util.Random;

import net.llamaslayers.minecraft.banana.gen.BananaBlockPopulator;

import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;

/**
 * Populator from BananaMaze to make crumbling walls.
 *
 * @author Nightgunner5
 */
public class RuinsPopulator extends BananaBlockPopulator {
	private static final int MAX_RUINS = 3;
	private static final int RUINS_CHANCE = 20;
	private static final Material RUINS_MATERIAL = Material.COBBLESTONE;
	private static final Material NETHER_RUINS_MATERIAL = Material.GLOWSTONE;
	private static final BlockFace[] directions = new BlockFace[] {
			BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST };

	/**
	 * @see org.bukkit.generator.BlockPopulator#populate(org.bukkit.World,
	 *      java.util.Random, org.bukkit.Chunk)
	 */
	@Override
	public void populate(World world, Random random, Chunk source) {
		if (getArg(world, "noruins"))
			return;
		ChunkSnapshot snapshot = source.getChunkSnapshot();
		int ruins = 0;
		while (random.nextInt(100) < RUINS_CHANCE
				&& ruins < MAX_RUINS) {
			int startX = random.nextInt(14) + 1;
			int startZ = random.nextInt(14) + 1;
			int startY = snapshot.getHighestBlockYAt(startX, startZ);
			int startHeight = random.nextInt(5) + 1;

			BlockFace direction1 = directions[random.nextInt(directions.length)];
			BlockFace direction2 = directions[random.nextInt(directions.length)];

			if (source.getBlock(startX, startY - 1, startZ).isLiquid()) {
				ruins++;
				continue;
			}

			int height = startHeight;
			int x = startX;
			int z = startZ;
			while (height > 0 && 0 <= x && x < 16 && 0 <= z && z < 16) {
				int y = snapshot.getHighestBlockYAt(x, z);
				while (source.getBlock(x, y - 1, z).isLiquid() && y > 0) {
					y--;
				}
				for (; y < startY + height; y++) {
					source.getBlock(x, y, z).setType(getArg(world, "nether") ? NETHER_RUINS_MATERIAL
							: RUINS_MATERIAL);
				}

				height -= random.nextInt(3);

				x += direction1.getModX();
				z += direction1.getModZ();
			}

			if (direction1 != direction2) {
				height = startHeight;
				x = startX;
				z = startZ;
				while (height > 0 && 0 <= x && x < 16 && 0 <= z && z < 16) {
					int y = snapshot.getHighestBlockYAt(x, z);
					while (source.getBlock(x, y - 1, z).isLiquid() && y > 0) {
						y--;
					}
					for (; y < startY + height; y++) {
						source.getBlock(x, y, z).setType(getArg(world, "nether") ? NETHER_RUINS_MATERIAL
								: RUINS_MATERIAL);
					}

					height -= random.nextInt(3);

					x += direction2.getModX();
					z += direction2.getModZ();
				}
			}

			ruins++;
		}
	}
}
