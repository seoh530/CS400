import java.util.NoSuchElementException;

public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {
  
  private static final int DEFAULT_CAPACITY = 8;
  private static final double LOAD_FACTOR_THRESHOLD = 0.7;

  private Entry<KeyType, ValueType>[] table;
  private int size;

  @SuppressWarnings("unchecked")
  public HashtableMap(int capacity) {
    table = (Entry<KeyType, ValueType>[]) new Entry[capacity];
    size = 0;
  }

  public HashtableMap() {
    this(DEFAULT_CAPACITY);
  }

  @Override
  public void put(KeyType key, ValueType value) throws IllegalArgumentException {
    if (key == null || containsKey(key)) {
      throw new IllegalArgumentException();
    }

    int index = getIndex(key);
    while (table[index] != null && !table[index].isRemoved()) {
      index = (index + 1) % table.length;
    }

    table[index] = new Entry<>(key, value);
    size++;

    if ((double)size >= table.length * LOAD_FACTOR_THRESHOLD) {
      resizeTable();
    }
  }
  

  @Override
  public boolean containsKey(KeyType key) {
    int index = getIndex(key);
    int startIndex = index;

    while (table[index] != null) {
      if (!table[index].isRemoved() && table[index].getKey().equals(key)) {
        return true;
      }
      index = (index + 1) % table.length;
      if (index == startIndex) {
        break;
      }
    }

    return false;
  }

  @Override
  public ValueType get(KeyType key) throws NoSuchElementException {
    int index = getIndex(key);
    int startIndex = index;

    while (table[index] != null) {
      if (!table[index].isRemoved() && table[index].getKey().equals(key)) {
        return table[index].getValue();
      }
      index = (index + 1) % table.length;
      if (index == startIndex) {
        break;
      }
    }

    throw new NoSuchElementException();
  }

  @Override
  public ValueType remove(KeyType key) throws NoSuchElementException {
    int index = getIndex(key);
    int startIndex = index;

    while (table[index] != null) {
      if (!table[index].isRemoved() && table[index].getKey().equals(key)) {
        ValueType value = table[index].getValue();
        table[index].setRemoved(true);
        size--;
        return value;
      }
      index = (index + 1) % table.length;
      if (index == startIndex) {
        break;
      }
    }

    throw new NoSuchElementException();
  }

  @Override
  public void clear() {
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
    size = 0;
  }

  @Override
  public int getSize() {
    return size;
  }

  @Override
  public int getCapacity() {
    return table.length;
  }

  private int getIndex(KeyType key) {
    return Math.abs(key.hashCode()) % table.length;
  }

  @SuppressWarnings("unchecked")
  private void resizeTable() {
    Entry<KeyType, ValueType>[] oldTable = table;
    table = (Entry<KeyType, ValueType>[]) new Entry[table.length * 2];
    size = 0;

    for (Entry<KeyType, ValueType> entry : oldTable) {
      if (entry != null && !entry.isRemoved()) {
        put(entry.getKey(), entry.getValue());
      }
    }
  }


  private static class Entry<KeyType, ValueType> {
    private KeyType key;
    private ValueType value;
    private boolean removed;

    public Entry(KeyType key, ValueType value) {
      this.key = key;
      this.value = value;
      this.removed = false;
    }

    public KeyType getKey() {
      return key;
    }

    public ValueType getValue() {
      return value;
    }

    public boolean isRemoved() {
      return removed;
    }

    public void setRemoved(boolean removed) {
      this.removed = removed;
    }
  }
}
