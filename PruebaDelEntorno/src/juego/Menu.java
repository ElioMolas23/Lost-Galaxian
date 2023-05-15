package juego;

import java.awt.Color;

import entorno.Entorno;

public class Menu {

	public static boolean principal(Entorno e, int record) {
		e.cambiarFont("Arial Black", 80, Color.CYAN);
		e.escribirTexto("Lost Galaxian" , 100, e.alto() / 2 - 100);

		e.cambiarFont("Arial Black", 40, Color.WHITE);
		e.escribirTexto("Presiona 'Enter' para comenzar", 60, e.alto() / 2 + 50);

		e.cambiarFont("Arial Black", 20, Color.YELLOW);
		e.escribirTexto("High Record: " + record, 10, e.alto() - 20);

		if(e.estaPresionada(e.TECLA_ENTER)) {
			return false;
		}
		return true;
	}

	public static boolean superacionNivel(Entorno e, int nivel) {
		e.cambiarFont("Arial Black", 80, Color.GREEN);
		e.escribirTexto("Nivel " + (nivel - 1), 250, e.alto() / 2 - 100);
		e.escribirTexto("Superado", 190, e.alto() / 2 - 30);

		e.cambiarFont("Arial Black", 40, Color.WHITE);
		e.escribirTexto("Presiona 'Enter' para continuar", 60, e.alto() / 2 + 50);

		if(e.estaPresionada(e.TECLA_ENTER)) {
			return false;
		}
		return true;
	}

	public static boolean perdidaNivel(Entorno e, int nivel, int puntos) {
		e.cambiarFont("Arial Black", 80, Color.RED);
		e.escribirTexto("Perdiste" , 220, e.alto() / 2 - 100);

		e.cambiarFont("Arial Black", 50, Color.YELLOW);
		e.escribirTexto("Puntos: " + puntos, 220, e.alto() / 2 - 50);

		e.cambiarFont("Arial Black", 40, Color.WHITE);
		e.escribirTexto("Presiona 'Space'", 220, e.alto() / 2 + 50);
		e.escribirTexto("para volver al menu", 180, e.alto() / 2 + 120);

		if(e.estaPresionada(e.TECLA_ESPACIO)) {
			return true;
		}
		return false;
	}
}
