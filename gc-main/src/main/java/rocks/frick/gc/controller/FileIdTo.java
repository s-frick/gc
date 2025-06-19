package rocks.frick.gc.controller;

import gc.model.FileID;
import scala.Option;

import static scala.jdk.javaapi.OptionConverters.*;

import java.util.Optional;

public record FileIdTo(String id, String name, FileID fileID) {

  public static Optional<FileIdTo> fromDomain(Option<FileID> fileId, String id, String name) {
    var file = toJava(fileId);
    return file.map(f -> new FileIdTo(
        id,
        name,
        f)
    );
  }
}
