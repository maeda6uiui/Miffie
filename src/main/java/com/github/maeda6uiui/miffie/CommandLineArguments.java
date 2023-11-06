package com.github.maeda6uiui.miffie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * This class retains the command-line arguments passed to the main method.
 *
 * @author maeda6uiui
 */
public class CommandLineArguments {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineArguments.class);

    private List<String> args;

    private static CommandLineArguments instance;

    private CommandLineArguments(String[] args) {
        this.args = Arrays.asList(Objects.requireNonNullElse(args, new String[]{}));
    }

    public List<String> getArgs() {
        return new ArrayList<>(args);
    }

    public static void setArgs(String[] args) {
        if (instance == null) {
            instance = new CommandLineArguments(args);
        } else {
            logger.warn("Command-line arguments are already set. Refuse to update");
        }
    }

    public static Optional<CommandLineArguments> get() {
        return Optional.ofNullable(instance);
    }
}
