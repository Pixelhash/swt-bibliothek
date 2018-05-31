package de.fhl.swtlibrary.util;

public class Tuple<T1, T2> {

  private T1 firstValue;
  private T2 secondValue;

  Tuple(T1 firstValue, T2 secondValue) {
    this.firstValue = firstValue;
    this.secondValue = secondValue;
  }

  public T1 getFirstValue() {
    return firstValue;
  }

  public T2 getSecondValue() {
    return secondValue;
  }
}
