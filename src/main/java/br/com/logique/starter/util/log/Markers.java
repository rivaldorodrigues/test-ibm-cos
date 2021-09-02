package br.com.logique.starter.util.log;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public interface Markers {

    Marker SINCRONIZAR_DADOS = MarkerFactory.getMarker("SINCRONIZAR_DADOS");
    Marker COMUNICACAO_NORDESTAO = MarkerFactory.getMarker("COMUNICACAO_NORDESTAO");

}
