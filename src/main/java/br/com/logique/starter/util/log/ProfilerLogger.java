package br.com.logique.starter.util.log;

import org.slf4j.Logger;
import org.slf4j.profiler.Profiler;
import org.slf4j.profiler.ProfilerRegistry;

/**
 * Classe utilitária para log via profiller
 *
 * @author Rivaldo
 * Created on 03/08/2017.
 */
public class ProfilerLogger implements AutoCloseable {

    private Profiler profiler;
    private final ProfilerRegistry profilerRegistry;

    /**
     * Cria um novo profiller
     *
     * @param tituloProfile o titulo que sera logado no inicio do profile
     * @param loggerProfile o logger utilizado
     */
    public ProfilerLogger(String tituloProfile, Logger loggerProfile) {

        profilerRegistry = ProfilerRegistry.getThreadContextInstance();

        profiler = new Profiler(tituloProfile);
        profiler.registerWith(profilerRegistry);
        profiler.setLogger(loggerProfile);
    }

    /**
     * Cria um novo profiller
     *
     * @param tituloProfile o titulo que sera logado no inicio do profile
     * @param idProfile     o id para identificação do profiler
     * @param loggerProfile o logger utilizado
     */
    public ProfilerLogger(String tituloProfile, String idProfile, Logger loggerProfile) {

        profilerRegistry = ProfilerRegistry.getThreadContextInstance();

        profiler = new Profiler(tituloProfile);
        profiler.registerWith(profilerRegistry);
        profiler.setLogger(loggerProfile);
        profiler.startNested(idProfile);
    }

    /**
     * Inicia uma nova contagem do profile logando a descrição desejada
     *
     * @param descricao a descrição que será logada
     */
    public void registrar(String descricao) {
        if (profiler != null) {
            profiler.start(descricao);
        }
    }

    @Override
    public void close() {

        if (profiler != null) {
            profiler.stop().print();
            profiler.stop().log();
        }

        profiler = null;
    }
}
