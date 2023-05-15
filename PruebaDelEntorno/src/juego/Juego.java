package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private AstroMegaShip nave;
	private DestructorEstelar[] enemigos;
	private Image fondo, fondoAux;
	private Image[] vidas;
	private double yFondo, yFondoAux;
	private Proyectil bala;
	private Proyectil[] iones;
	private int vidasRestantes, enemigosDerrotados, cooldownEnemigos, nivel, puntos, recordPuntos;
	private Asteroide[] asteroides;
	private boolean entrarMenus, inicioNivel;
	private Item vidaUP;
	private Sonido musicaJuego, musicaPerdiste, musicaMenu, musicaNivelSuperado;

	public Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Prueba del Entorno", 800, 600);
		//Inicia la musica
		musicaJuego = new Sonido("/musicaJuego.wav");
		musicaPerdiste = new Sonido("/musicaPerdiste.wav");
		musicaMenu = new Sonido("/musicaMenu.wav");
		musicaNivelSuperado = new Sonido("/musicaNivelSuperado.wav");


		// Inicializar lo que haga falta para el juego
		// ...
		// Objetos
		this.nave = new AstroMegaShip(entorno.ancho() / 2, entorno.alto() * 0.9);
		this.enemigos = new DestructorEstelar[6];
		this.iones = new Proyectil[6];
		this.asteroides = new Asteroide[6];

		// Imagenes
		this.fondo = Herramientas.cargarImagen("fondo.png");
		this.fondoAux = Herramientas.cargarImagen("fondo.png");
		this.vidas = new Image[3];
		this.vidas[2] = Herramientas.cargarImagen("vidaX3.png");
		this.vidas[1] = Herramientas.cargarImagen("vidaX2.png");
		this.vidas[0] = Herramientas.cargarImagen("vidaX1.png");

		// Variables
		this.yFondo = 600;
		this.yFondoAux = 0;
		this.vidasRestantes = 3;
		this.cooldownEnemigos = 400;
		this.nivel = 1;
		this.puntos = 0;
		this.recordPuntos = 0;
		this.entrarMenus = true;
		this.inicioNivel=true;
		// Inicia el juego!
		this.entorno.iniciar();

	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y por lo
	 * tanto es el método más importante de esta clase. Aquí se debe actualizar el
	 * estado interno del juego para simular el paso del tiempo (ver el enunciado
	 * del TP para mayor detalle).
	 */
	public void tick() {
		// Procesamiento de un instante de tiempo
		// ...

		if (entrarMenus) {
			mostrarFondo();
			gestionMenus();
		} else {

			// Fondo animado y musica
			mostrarFondo();
			musicaMenu.pararMusica();
			musicaNivelSuperado.pararMusica();
			this.musicaJuego.reproducirMusica();
			
			
			//creacion de enemigos/asteroides al inicio de los niveles
			if (this.inicioNivel) {
				double pos=0.1;
				for(int i=0; i<4; i++) {
					this.enemigos[i] = new DestructorEstelar(this.entorno.ancho()*pos, (this.entorno.alto() * 0.06), nivel);
					double j=Math.random();
					if(j>0.5) {
						this.asteroides[i]= new Asteroide(this.entorno.ancho()*pos, this.entorno.alto()/2 * j , true);
					}
					else {
						this.asteroides[i]= new Asteroide(this.entorno.ancho()*pos, this.entorno.alto()/2* j , false);
					}
					pos += 0.22;
				}
				inicioNivel=false;			
			}

			// CREACION ENEMIGOS
			// Crea los enemigos y los asigna a la array
			asignarEnemigos();

			// Permite activar a los enemigos cada una cierta cantidad de tiempo
			if (enemigosActivos() == 0 || cooldownEnemigos <= 0) {
				spawnEnemigos();
				cooldownEnemigos = 300 / nivel;
			}
			cooldownEnemigos--;
			
			// CREACION ASTEROIDES
			// Crea los asteroides y los asigna a la array
			spawnAsteroides();
			if(nivel>3) {
				asignarAsteroides();
			}
			// CREACION BALAS DE IONES(ENEMIGAS)
			// Asigna bala de iones a la array
			asignarBalasDeIones();

			// CREACION VIDA EXTRA
			asignarVidaUP();

			// MOVIMIENTOS, MUESTRA EN PANTALLA Y COLISIONES
			// Movimiento, dibujo y colisiones relacionadas con la VIDA EXTRA
			if (vidaUP != null) {
				vidaUP.mover(entorno);
				vidaUP.dibujar(entorno);

				if (vidaUP != null && nave != null && nave.chocasteCon(vidaUP)) {
					vidaUP.sonidoAgarrado();
					vidaUP = null;
					if (vidasRestantes < 3) {
						vidasRestantes += 1;
					} else {
						puntos += (nivel * 10);
					}

				}
				if (vidaUP != null && !vidaUP.estaDentro(entorno)) {
					vidaUP = null;
				}
			}


			// Movimiento, dibujo y colisiones relacionadas con la BALA DE LA NAVE
			if (bala != null) {
				bala.dibujar(entorno);
				bala.mover(entorno);

				if (!bala.estaDentro(entorno)) {
					bala = null;
				}

				for (int i = 0; i < enemigos.length; i++) {
					if (enemigos[i] != null && enemigos[i].estaActivado() && bala != null
							&& enemigos[i].chocasteCon(bala)) {
						enemigos[i].sonidoImpacto();
						enemigos[i] = null;
						bala = null;
						enemigosDerrotados++;
						puntos += (nivel * 10) * enemigosDerrotados;
					}
				}
			}

			// Creacion, movimiento, dibujo y colisiones relacionadas con la BALA DE IONES
			for (int i = 0; i < iones.length; i++) {
				if (iones[i] != null) {
					iones[i].mover(entorno);
					iones[i].dibujar(entorno);
				}
				if (iones[i] != null && !iones[i].estaDentro(entorno)) {
					iones[i] = null;
				}
				if (iones[i] != null && nave != null && nave.chocasteCon(iones[i])) {
					nave.sonidoImpacto();
					iones[i] = null;
					vidasRestantes -= 1;
				}
			}

			// Movimiento, dibujo y colisiones relacionadas con los ASTEROIDES
			for (int i = 0; i < asteroides.length; i++) {
				if (asteroides[i] != null && asteroides[i].estaActivado()) {
					asteroides[i].dibujar(entorno);
					asteroides[i].mover(entorno);
				}
				if (asteroides[i] != null && nave != null && nave.chocasteCon(asteroides[i])) {
					nave.sonidoImpacto();
					asteroides[i] = null;
					vidasRestantes -= 1;
				}
				if (asteroides[i] != null && asteroides[i].estaActivado() && bala != null && asteroides[i].chocasteCon(bala)) {
					asteroides[i].sonidoImpacto();
					bala = null;
				}
			}

			// Movimiento, dibujo y colision con nave de los ENEMIGOS
			for (int i = 0; i < enemigos.length; i++) {
				if (enemigos[i] != null) {
					if (enemigos[i].estaActivado()) {
						enemigos[i].dibujar(entorno);
						enemigos[i].mover(entorno);
					}

					if (nave != null && nave.chocasteCon(enemigos[i])) {
						nave.sonidoImpacto();
						enemigos[i] = null;
						vidasRestantes -= 1;
					}
				}
			}

			// ASIGNACION DE TECLAS PARA EL MOVIMIENTO DE LA NAVE Y SU DISPARO
			if (nave != null) {
				nave.dibujar(entorno);
				if (entorno.estaPresionada('a') || entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
					nave.moverHaciaLaIzquierda(entorno);
				}
				if (entorno.estaPresionada('d') || entorno.estaPresionada(entorno.TECLA_DERECHA)) {
					nave.moverHaciaLaDerecha(entorno);
				}
				// CREACION BALA
				if (bala == null && entorno.estaPresionada(entorno.TECLA_ESPACIO)) {
					bala = nave.dispararBala();
					nave.sonidoDisparo();
				}
			}

			// GUI
			// Sistema de vidas
			for (int i = 0; i < vidas.length; i++) {

				if (vidasRestantes == i + 1) {
					entorno.dibujarImagen(vidas[i], 80, 20, 0, 0.15);
				}
				if (vidasRestantes == 0) {
					entrarMenus = true;
					inicioNivel = true;
					reset();
				}
			}
			// Subir nivel
			if (enemigosDerrotados == 5 * nivel) {
				nivel++;
				entrarMenus = true;
				inicioNivel = true;
				reset();
			}

			// Nivel
			entorno.cambiarFont("Arial Black", 15, Color.CYAN);
			entorno.escribirTexto("Nivel " + nivel, 10, entorno.alto() - 70);

			// Enemigos restantes para pasar de nivel
			entorno.cambiarFont("Arial Black", 15, Color.RED);
			entorno.escribirTexto("Sig. Nivel  x" + (5 * nivel - enemigosDerrotados), 10, entorno.alto() - 50);

			// Puntos totales obtenidos
			entorno.cambiarFont("Arial Black", 20, Color.GREEN);
			entorno.escribirTexto("PUNTOS x" + puntos, 10, entorno.alto() - 20);

		}
	}
	// FINAL BRACKET TICK CLASS

	// METODOS DE SPAWN DE ASTEROIDES
	// Crea asteroides a posicion aleatoria
	public void asignarAsteroides() {
		int contador = 0;
		for (int i = 0; i < asteroides.length; i++) {
			if (asteroides[i] == null && contador < 2) {
				if (i % 2 == 0) {
					asteroides[i] = new Asteroide(Math.floor(Math.random() * this.entorno.ancho()), 0, true);
					contador++;
				}
				if (i % 2 != 0) {
					asteroides[i] = new Asteroide(Math.floor(Math.random() * this.entorno.ancho()), 0, false);
					contador++;
				}
			}
		}
	}

	// Activa asteroides si no hay ningun otro asteroide activo en su zona de spawn
	public void spawnAsteroides() {
		for (int i = 0; i < this.asteroides.length; i++) {
			if (this.asteroides[i] != null && (puedeSpawnearAsteroide(i)) && !asteroides[i].estaActivado()) {
				asteroides[i].switchActivar();
			}
		}
	}

	// Se fija si hay algun otro asteroide activo en la zona de un asteroide en
	// particular
	public boolean puedeSpawnearAsteroide(int i) {
		for (int n = 0; n < this.asteroides.length; n++) {
			if (this.asteroides[i] != null && (this.asteroides[n] != null && this.asteroides[n].estaActivado())
					&& asteroides[i].estaEnZona(asteroides[n])) {
				return false;
			}
		}
		return true;
	}

	// Cuenta cantidad de asteroides activos en pantalla
	public int asteroidesActivos() {
		int activos = 0;

		for (int i = 0; i < asteroides.length; i++) {
			if (asteroides[i] != null && asteroides[i].estaActivado()) {
				activos++;
			}
		}
		return activos;
	}

	// METODOS DE SPAWN ENEMIGOS
	// Asigna enemigos a una posicion aleatoria
	public void asignarEnemigos() {
		for (int i = 0; i < this.enemigos.length; i++) {
			if (enemigos[i] == null) {
				int posicion;
				posicion = (int) (this.entorno.ancho() * 0.10)
						+ (int) (Math.floor(Math.random() * (this.entorno.ancho() * 0.80)));
				this.enemigos[i] = new DestructorEstelar(posicion, (this.entorno.alto() * 0.06), nivel);
			}
		}
	}

	// Activa enemigos si no hay ningun otro enemigo activo en su zona de spawn
	public void spawnEnemigos() {
		for (int i = 0; i < this.enemigos.length; i++) {
			if (this.enemigos[i] != null && (puedeSpawnearEnemigo(i)) && !enemigos[i].estaActivado()) {
				enemigos[i].switchActivar();
			}
		}
	}

	// Se fija si hay algun otro enemigo activo en la zona de un enemigo en
	// particular
	public boolean puedeSpawnearEnemigo(int i) {
		for (int n = 0; n < this.enemigos.length; n++) {
			if (this.enemigos[i] != null && (this.enemigos[n] != null && this.enemigos[n].estaActivado())
					&& enemigos[i].estaEnZona(enemigos[n])) {
				return false;
			}
		}
		return true;
	}

	// Cuenta cantidad de enemigos activos en pantalla
	public int enemigosActivos() {
		int activos = 0;

		for (int i = 0; i < enemigos.length; i++) {
			if (enemigos[i] != null && enemigos[i].estaActivado()) {
				activos++;
			}
		}
		return activos;
	}

	// METODO ASIGNACION BALAS
	public void asignarBalasDeIones() {
		for (int i = 0; i < iones.length; i++) {
			if (iones[i] == null && enemigos[i] != null && enemigos[i].estaActivado() && nave != null
					&& enemigos[i].detectoObjetivo(nave)) {
				iones[i] = enemigos[i].dispararBala();
				enemigos[i].sonidoDisparo();
			}
		}
	}

	// METODO ASIGNACION VIDA EXTRA
	public void asignarVidaUP() {
		int random = (int) Math.floor(Math.random() * 1000);
		if (vidaUP == null && random == 1) {
			int posicion = (int) (this.entorno.ancho() * 0.10)
					+ (int) (Math.floor(Math.random() * (this.entorno.ancho() * 0.80)));
			this.vidaUP = new Item(posicion, vidasRestantes);
		}
	}

	// RESET PARA CAMBIO DE NIVEL
	public void reset() {
		for (int i = 0; i < 6; i++) {
			enemigos[i] = null;
			iones[i] = null;
			asteroides[i] = null;
			bala = null;
			nave = null;
			vidaUP = null;
			this.nave = new AstroMegaShip(entorno.ancho() / 2, entorno.alto() * 0.9);
			enemigosDerrotados = 0;
		}
	}

	// GESTION DE MENUS
	public void gestionMenus() {
		if (vidasRestantes == 3 && nivel == 1) {
			musicaPerdiste.pararMusica();
			musicaMenu.reproducirMusica();
			entrarMenus = Menu.principal(entorno, recordPuntos);
			return;
		}
		if (vidasRestantes > 0 && nivel > 1) {
			musicaJuego.pausarMusica();
			musicaNivelSuperado.reproducirMusica(); //CAMBIAR
			entrarMenus = Menu.superacionNivel(entorno, nivel);
			return;
		}
		if (vidasRestantes == 0) {
			musicaJuego.pararMusica();
			musicaPerdiste.reproducirMusica();
			boolean volver = false;
			volver = Menu.perdidaNivel(entorno, nivel, puntos);
			if (volver) {
				nivel = 1;
				vidasRestantes = 3;
				if (puntos > recordPuntos) {
					recordPuntos = puntos;
				}
				puntos = 0;
			}
			return;
		}
	}

	// ANIMACION FONDO
	public void mostrarFondo() {
		entorno.dibujarImagen(fondo, entorno.ancho() / 2, yFondo++, 0);
		entorno.dibujarImagen(fondoAux, entorno.ancho() / 2, yFondoAux++, 0);

		if (yFondo == 300) {
			yFondoAux = -300;
		}
		if (yFondoAux == 300) {
			yFondo = -300;
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}