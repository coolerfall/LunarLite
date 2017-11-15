package com.anbillon.lunarlite.ui.widget.picker;

/**
 * Range for visible items.
 *
 * @author Vincent Cheung (coolingfall@gmail.com)
 */
public class ItemsRange {
  /* first item number */
  private int mFirst;
  /* items count */
  private int mCount;

  /**
   * Default constructor. Creates an empty range
   */
  public ItemsRange() {
    this(0, 0);
  }

  /**
   * Constructor.
   *
   * @param first the number of first item
   * @param count the count of items
   */
  public ItemsRange(int first, int count) {
    mFirst = first;
    mCount = count;
  }

  /**
   * Gets number of  first item.
   *
   * @return the number of the first item
   */
  public int getFirst() {
    return mFirst;
  }

  /**
   * Gets number of last item.
   *
   * @return the number of last item
   */
  public int getLast() {
    return getFirst() + getCount() - 1;
  }

  /**
   * Get items count.
   *
   * @return the count of items
   */
  public int getCount() {
    return mCount;
  }

  /**
   * Tests whether item is contained by range.
   *
   * @param index the item number
   * @return true if item is contained
   */
  public boolean contains(int index) {
    return index >= getFirst() && index <= getLast();
  }
}