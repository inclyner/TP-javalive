package pt.isec.pa.javalife.model.factory;

import pt.isec.pa.javalife.model.EcoSistemaFacade.EcossistemaFacade;
import pt.isec.pa.javalife.model.data.*;

public class ElementFactory {

    public static ElementoBase createElement(Elemento type, Object... args) {
        switch (type) {
            case INANIMADO:
                if (args.length == 1 && args[0] instanceof Area) {
                    return new Pedra((Area) args[0]);
                } else {
                    throw new IllegalArgumentException("Argumentos inválidos para Pedra");
                }
            case FLORA:
                if (args.length == 1 && args[0] instanceof Area) {
                    return new Erva((Area) args[0]);
                } else {
                    throw new IllegalArgumentException("Argumentos inválidos para Pedra");
                }
            case FAUNA:
                if (args.length == 1 && args[0] instanceof Area) {
                    return new Animal((Area) args[0]);
                } else {
                    throw new IllegalArgumentException("Argumentos inválidos para Pedra");
                }
            default:
                throw new IllegalArgumentException("Tipo desconhecido: " + type);
        }
    }
}
