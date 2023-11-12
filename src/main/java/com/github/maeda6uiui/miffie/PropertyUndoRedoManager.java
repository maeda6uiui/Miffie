package com.github.maeda6uiui.miffie;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.SingleSelectionModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Optional;

/**
 * This class manages undo and redo of registered properties.
 *
 * @author maeda6uiui
 */
public class PropertyUndoRedoManager {
    private static final Logger logger = LoggerFactory.getLogger(PropertyUndoRedoManager.class);

    public static abstract class PropertySnapshotBase {
        private Instant instant;
        private Object value;

        private PropertySnapshotBase previousSnapshot;
        private PropertySnapshotBase nextSnapshot;

        public PropertySnapshotBase(Object value) {
            instant = Instant.now();
            this.value = value;
        }

        public Instant getInstant() {
            return instant;
        }

        public Object getObjectValue() {
            return value;
        }

        public Optional<PropertySnapshotBase> getPreviousSnapshot() {
            return Optional.ofNullable(previousSnapshot);
        }

        public void setPreviousSnapshot(PropertySnapshotBase previousSnapshot) {
            this.previousSnapshot = previousSnapshot;
        }

        public Optional<PropertySnapshotBase> getNextSnapshot() {
            return Optional.ofNullable(nextSnapshot);
        }

        public void setNextSnapshot(PropertySnapshotBase nextSnapshot) {
            this.nextSnapshot = nextSnapshot;
        }

        public abstract void apply();
    }

    public static abstract class PropertySnapshot<T> extends PropertySnapshotBase {
        private T value;

        public PropertySnapshot(T value) {
            super(value);
            this.value = value;
        }

        public T getValue() {
            return value;
        }

        public abstract void apply();
    }

    public static class SingleSelectionModelSnapshot<T> extends PropertySnapshot<T> {
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

    public static class StringPropertySnapshot extends PropertySnapshot<String> {
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

    public static class IntegerPropertySnapshot extends PropertySnapshot<Integer> {
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

    public static class BooleanPropertySnapshot extends PropertySnapshot<Boolean> {
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

    private PropertySnapshotBase currentSnapshot;

    public PropertyUndoRedoManager() {

    }

    public synchronized PropertyUndoRedoManager add(PropertySnapshotBase snapshot) {
        if (currentSnapshot == null) {
            currentSnapshot = snapshot;
            return this;
        }

        currentSnapshot.setNextSnapshot(snapshot);
        snapshot.setPreviousSnapshot(currentSnapshot);
        currentSnapshot = snapshot;

        return this;
    }

    public <T> PropertyUndoRedoManager add(SingleSelectionModel<T> model, T value) {
        var snapshot = new SingleSelectionModelSnapshot<>(model, value);
        return this.add(snapshot);
    }

    public PropertyUndoRedoManager add(StringProperty property, String value) {
        var snapshot = new StringPropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertyUndoRedoManager add(IntegerProperty property, Integer value) {
        var snapshot = new IntegerPropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public PropertyUndoRedoManager add(BooleanProperty property, Boolean value) {
        var snapshot = new BooleanPropertySnapshot(property, value);
        return this.add(snapshot);
    }

    public boolean snapshotExists() {
        return currentSnapshot != null;
    }

    public void undo() {
        if (currentSnapshot == null) {
            return;
        }

        currentSnapshot
                .getPreviousSnapshot()
                .ifPresent(p -> {
                    p.apply();
                    currentSnapshot = p;
                });
    }

    public void redo() {
        if (currentSnapshot == null) {
            return;
        }

        currentSnapshot
                .getNextSnapshot()
                .ifPresent(p -> {
                    p.apply();
                    currentSnapshot = p;
                });
    }
}
