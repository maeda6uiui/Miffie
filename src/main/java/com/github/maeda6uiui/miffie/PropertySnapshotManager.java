package com.github.maeda6uiui.miffie;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.SingleSelectionModel;
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

    public static abstract class PropertySnapshotBase<T> {
        private Instant instant;
        private T value;

        public PropertySnapshotBase(T value) {
            instant = Instant.now();
            this.value = value;
        }

        public Instant getInstant() {
            return instant;
        }

        public T getValue() {
            return value;
        }

        public abstract void apply();
    }

    public static class SingleSelectionModelSnapshot<T> extends PropertySnapshotBase<T> {
        private SingleSelectionModel<T> model;

        public SingleSelectionModelSnapshot(SingleSelectionModel<T> model, T value) {
            super(value);
            this.model = model;
        }

        @Override
        public void apply() {
            model.select(this.getValue());
        }
    }

    public static class StringPropertySnapshot extends PropertySnapshotBase<String> {
        private StringProperty property;

        public StringPropertySnapshot(StringProperty property, String value) {
            super(value);
            this.property = property;
        }

        @Override
        public void apply() {
            property.set(this.getValue());
        }
    }

    public static class IntegerPropertySnapshot extends PropertySnapshotBase<Integer> {
        private IntegerProperty property;

        public IntegerPropertySnapshot(IntegerProperty property, Integer value) {
            super(value);
            this.property = property;
        }

        @Override
        public void apply() {
            property.set(this.getValue());
        }
    }

    public static class BooleanPropertySnapshot extends PropertySnapshotBase<Boolean> {
        private BooleanProperty property;

        public BooleanPropertySnapshot(BooleanProperty property, Boolean value) {
            super(value);
            this.property = property;
        }

        @Override
        public void apply() {
            property.set(this.getValue());
        }
    }

    private List<PropertySnapshotBase> snapshots;
    private List<PropertySnapshotBase> rebaseSnapshots;
    private PropertySnapshotBase currentSnapshot;
    private boolean ignoreNextAdd;

    public PropertySnapshotManager() {
        snapshots = Collections.synchronizedList(new LinkedList<>());
        rebaseSnapshots = Collections.synchronizedList(new LinkedList<>());

        ignoreNextAdd = false;
    }

    public PropertySnapshotManager add(PropertySnapshotBase snapshot) {
        if (ignoreNextAdd) {
            ignoreNextAdd = false;
        } else {
            snapshots.add(snapshot);
            currentSnapshot = snapshot;

            rebaseSnapshots.clear();
        }

        return this;
    }

    public <T> PropertySnapshotManager add(SingleSelectionModel<T> model, T value) {
        var snapshot = new SingleSelectionModelSnapshot<>(model, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(StringProperty property, String value) {
        var snapshot = new StringPropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(IntegerProperty property, Integer value) {
        var snapshot = new IntegerPropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertySnapshotManager add(BooleanProperty property, Boolean value) {
        var snapshot = new BooleanPropertySnapshot(property, value);
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
    public Optional<PropertySnapshotBase> getCurrent() {
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

        PropertySnapshotBase previousCurrent = rebaseSnapshots.remove(0);
        snapshots.add(previousCurrent);
        currentSnapshot = previousCurrent;

        this.ignoreNextAdd = true;

        return this;
    }
}
