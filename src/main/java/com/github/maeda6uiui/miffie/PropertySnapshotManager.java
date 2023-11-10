package com.github.maeda6uiui.miffie;

import java.time.Instant;
import java.util.*;

/**
 * This class manages change history of registered properties.
 *
 * @author maeda6uiui
 */
public class PropertySnapshotManager {
    public static class PropertySnapshot {
        private final Instant instant = Instant.now();
        private final Object property;
        private String stringValue;
        private Integer intValue;
        private Boolean booleanValue;

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

    private List<String> snapshotUUIDs; //UUIDs of snapshots in chronological order
    private Map<String, PropertySnapshot> snapshots;    //(Snapshot UUID, Snapshot)

    public PropertySnapshotManager() {
        snapshotUUIDs = Collections.synchronizedList(new ArrayList<>());
        snapshots = Collections.synchronizedSortedMap(new TreeMap<>());
    }

    /**
     * Registers a snapshot.
     *
     * @param snapshot Snapshot
     * @return UUID of the snapshot
     */
    public String put(PropertySnapshot snapshot) {
        String uuid = UUID.randomUUID().toString();
        snapshotUUIDs.add(uuid);
        snapshots.put(uuid, snapshot);

        return uuid;
    }

    public void clear() {
        snapshotUUIDs.clear();
        snapshots.clear();
    }

    /**
     * Returns the last snapshot and removes it from the list.
     * This method returns empty value if there is no snapshot saved.
     *
     * @return Last snapshot
     */
    public Optional<PropertySnapshot> getLast() {
        if (snapshotUUIDs.isEmpty()) {
            return Optional.empty();
        }

        String lastUUID = snapshotUUIDs.get(snapshotUUIDs.size() - 1);
        PropertySnapshot lastSnapshot = snapshots.get(lastUUID);

        snapshotUUIDs.remove(snapshotUUIDs.size() - 1);
        snapshots.remove(lastUUID);

        return Optional.ofNullable(lastSnapshot);
    }

    public Optional<PropertySnapshot> findByUUID(String uuid) {
        return Optional.ofNullable(snapshots.get(uuid));
    }
}
