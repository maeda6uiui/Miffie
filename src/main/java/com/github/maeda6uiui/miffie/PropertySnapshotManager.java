package com.github.maeda6uiui.miffie;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class manages change history of registered properties.
 *
 * @author maeda6uiui
 */
public class PropertySnapshotManager {
    private static final Logger logger = LoggerFactory.getLogger(PropertySnapshotManager.class);

    public static class PropertySnapshot {
        private final Instant instant = Instant.now();
        private final Object property;
        private Object objectValue;
        private String stringValue;
        private Integer intValue;
        private Boolean booleanValue;

        public PropertySnapshot(Object property, Object objectValue) {
            this.property = property;
            this.objectValue = objectValue;
        }

        public PropertySnapshot(Object property, String stringValue) {
            this.property = property;
            this.stringValue = stringValue;
        }

        public PropertySnapshot(Object property, int intValue) {
            this.property = property;
            this.intValue = intValue;
        }

        public PropertySnapshot(Object property, boolean booleanValue) {
            this.property = property;
            this.booleanValue = booleanValue;
        }

        public Instant getInstant() {
            return instant;
        }

        public Object getProperty() {
            return property;
        }

        public Optional<Object> getObjectValue() {
            return Optional.ofNullable(objectValue);
        }

        public Optional<String> getStringValue() {
            return Optional.ofNullable(stringValue);
        }

        public Optional<Integer> getIntValue() {
            return Optional.ofNullable(intValue);
        }

        public Optional<Boolean> getBooleanValue() {
            return Optional.ofNullable(booleanValue);
        }
    }

    private List<PropertySnapshot> snapshots;
    private List<PropertySnapshot> rebaseSnapshots;
    private PropertySnapshot current;
    private boolean ignoreNextAdd;

    public PropertySnapshotManager() {
        snapshots = Collections.synchronizedList(new LinkedList<>());
        rebaseSnapshots = Collections.synchronizedList(new LinkedList<>());

        ignoreNextAdd = false;
    }

    public PropertySnapshotManager add(PropertySnapshot snapshot) {
        if (ignoreNextAdd) {
            ignoreNextAdd = false;
        } else {
            snapshots.add(snapshot);
            current = snapshot;

            rebaseSnapshots.clear();
        }

        return this;
    }

    public PropertySnapshotManager add(Object property, Object value) {
        var snapshot = new PropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(Object property, String value) {
        var snapshot = new PropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(Object property, int value) {
        var snapshot = new PropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(Object property, boolean value) {
        var snapshot = new PropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public void clear() {
        snapshots.clear();
        rebaseSnapshots.clear();
        current = null;
    }

    public int getNumSnapshots() {
        return snapshots.size();
    }

    public int getNumRebaseSnapshots() {
        return rebaseSnapshots.size();
    }

    public Optional<PropertySnapshot> getCurrent() {
        return Optional.ofNullable(current);
    }

    public PropertySnapshotManager rebaseToPrevious() {
        if (snapshots.isEmpty()) {
            logger.warn("Attempted to rebase to previous, but there are no snapshots saved");
            return this;
        }

        rebaseSnapshots.addFirst(current);
        snapshots.remove(current);
        current = snapshots.getLast();

        this.ignoreNextAdd = true;

        return this;
    }

    public PropertySnapshotManager rebaseToPreviousCurrent() {
        if (rebaseSnapshots.isEmpty()) {
            logger.warn("Attempted to rebase to previous current, but there are no snapshots saved for rebase");
            return this;
        }

        PropertySnapshot previousCurrent = rebaseSnapshots.removeFirst();
        snapshots.add(previousCurrent);
        current = previousCurrent;

        this.ignoreNextAdd = true;

        return this;
    }
}
