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
    private PropertySnapshot currentSnapshot;
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
            currentSnapshot = snapshot;

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
        currentSnapshot = null;
    }

    public int getNumSnapshots() {
        return snapshots.size();
    }

    public int getNumRebaseSnapshots() {
        return rebaseSnapshots.size();
    }

    /**
     * Returns the current snapshot.
     *
     * @return Current snapshot
     */
    public Optional<PropertySnapshot> getCurrent() {
        return Optional.ofNullable(currentSnapshot);
    }

    /**
     * Sets the previous snapshot as current.
     * Call this method when implementing undo.
     * The next attempt to add a snapshot after this method has been called is ignored
     * so that applying the snapshot value to the corresponding property will not be recorded.
     *
     * @return this
     */
    public PropertySnapshotManager rebaseToPrevious() {
        if (snapshots.isEmpty()) {
            logger.warn("Attempted to rebase to previous snapshot, but there are no snapshots saved");
            return this;
        }

        rebaseSnapshots.add(0, currentSnapshot);
        snapshots.remove(currentSnapshot);
        currentSnapshot = snapshots.get(snapshots.size() - 1);

        this.ignoreNextAdd = true;

        return this;
    }

    /**
     * Sets the following snapshot as current.
     * Call this method when implementing redo.
     * The next attempt to add a snapshot after this method has been called is ignored
     * so that applying the snapshot value to the corresponding property will not be recorded.
     *
     * @return this
     */
    public PropertySnapshotManager rebaseToFollowing() {
        if (rebaseSnapshots.isEmpty()) {
            logger.warn("Attempted to rebase to following snapshot, but there are no snapshots saved for rebase");
            return this;
        }

        PropertySnapshot previousCurrent = rebaseSnapshots.remove(0);
        snapshots.add(previousCurrent);
        currentSnapshot = previousCurrent;

        this.ignoreNextAdd = true;

        return this;
    }
}
