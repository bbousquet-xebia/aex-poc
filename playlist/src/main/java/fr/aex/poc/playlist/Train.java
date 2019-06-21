package fr.aex.poc.playlist;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public final class Train {

    String idTrain;
    String typeTrain;
}