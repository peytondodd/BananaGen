package net.llamaslayers.minecraft.banana.gen;

import java.util.*;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

/**
 * @author Nightgunner5
 */
public abstract class BananaChunkGenerator extends ChunkGenerator {
	private final Map<String, Map<String, String>> worldArgs = new HashMap<String, Map<String, String>>();

	/**
	 * @param world
	 *            args are specified for
	 * @param args
	 *            that are specified for the world
	 */
	public final void setWorldArgs(String world, String[] args) {
		Map<String, String> parsedArgs = new HashMap<String, String>();
		worldArgs.put(world, parsedArgs);
		for (String arg : args) {
			if (arg.indexOf('=') > -1) {
				parsedArgs.put(arg.substring(0, arg.indexOf('=')), arg.substring(arg.indexOf('=')));
			} else {
				parsedArgs.put(arg, "");
			}
		}
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @param def
	 *            default value
	 * @return the value of the arg, or def if the arg is not defined
	 */
	public final String getArgString(World world, String arg, String def) {
		checkArg(arg);
		if (!worldArgs.containsKey(world.getName()))
			return def;
		if (!worldArgs.get(world.getName()).containsKey(arg))
			return def;
		return worldArgs.get(world.getName()).get(arg);
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @param def
	 *            default value
	 * @return the value of the arg, or def if the arg is not defined or is not
	 *         an integer
	 */
	public final int getArgInt(World world, String arg, int def) {
		checkArg(arg);
		if (!worldArgs.containsKey(world.getName()))
			return def;
		if (!worldArgs.get(world.getName()).containsKey(arg))
			return def;
		try {
			return Integer.parseInt(worldArgs.get(world.getName()).get(arg));
		} catch (NumberFormatException ex) {
			return def;
		}
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @param def
	 *            default value
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return the value of the arg, or def if the arg is not defined or is not
	 *         an integer
	 */
	public final int getArgInt(World world, String arg, int def, int min,
		int max) {
		return Math.min(Math.max(getArgInt(world, arg, def), min), max);
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @param def
	 *            default value
	 * @return the value of the arg, or def if the arg is not defined or is not
	 *         a double
	 */
	public final double getArgDouble(World world, String arg, double def) {
		checkArg(arg);
		if (!worldArgs.containsKey(world.getName()))
			return def;
		if (!worldArgs.get(world.getName()).containsKey(arg))
			return def;
		try {
			return Double.parseDouble(worldArgs.get(world.getName()).get(arg));
		} catch (NumberFormatException ex) {
			return def;
		}
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @param def
	 *            default value
	 * @param min
	 *            minimum value
	 * @param max
	 *            maximum value
	 * @return the value of the arg, or def if the arg is not defined or is not
	 *         a double
	 */
	public final double getArgDouble(World world, String arg, double def,
		double min,
		double max) {
		return Math.min(Math.max(getArgDouble(world, arg, def), min), max);
	}

	/**
	 * @param world
	 *            to look for arg in
	 * @param arg
	 *            identifier
	 * @return true if the arg was specified for the world, false if it was not
	 */
	public final boolean getArg(World world, String arg) {
		checkArg(arg);
		if (!worldArgs.containsKey(world.getName()))
			return false;
		return worldArgs.get(world.getName()).containsKey(arg);
	}

	private Set<String> args = null;

	private void checkArg(String arg) throws RuntimeException {
		if (args != null) {
			if (!args.contains(arg)) {
				RuntimeException ex = new RuntimeException("Argument " + arg
						+ " is not declared!");
				ex.fillInStackTrace();
				throw ex;
			}
			return;
		}
		Args allowed = getClass().getAnnotation(Args.class);
		if (allowed == null || allowed.value().length == 0) {
			args = Collections.emptySet();

			RuntimeException ex = new RuntimeException("Argument " + arg
					+ " is not declared!");
			ex.fillInStackTrace();
			throw ex;
		}
		args = new HashSet<String>();
		for (String allowedArg : allowed.value()) {
			args.add(allowedArg);
		}

		if (!args.contains(arg)) {
			RuntimeException ex = new RuntimeException("Argument " + arg
					+ " is not declared!");
			ex.fillInStackTrace();
			throw ex;
		}
	}

	@Override
	public boolean canSpawn(World world, int x, int z) {
		return !world.getHighestBlockAt(x, z).isLiquid();
	}
}
