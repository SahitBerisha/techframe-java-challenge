package techframe.javachallenge.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.io.Serializable;

//@JsonFormat(shape = Shape.OBJECT)
public enum ProductType implements Serializable {
  KG, LITER, BOTTLE, PACKAGE, PACK, PIECE
}
