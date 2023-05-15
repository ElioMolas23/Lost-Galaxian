package juego;

import java.awt.*;
import entorno.*;

public class Asteroide {
	private double x, y, velocidad, angulo,ancho,alto;
	private Image img;
	private boolean direccion;
	private boolean activado;
	private Sonido sonidoImpacto;

	public Asteroide(double x, double y,boolean direccion) {
		this.x = x;
		this.y = y;
		this.img = Herramientas.cargarImagen("asteroide.png");
		this.sonidoImpacto = new Sonido("/impactoAsteroide.wav");
		this.ancho = 30;
		this.alto = 30;
		this.direccion = direccion;
		this.activado = false;

		if(this.direccion) {
			this.angulo = Math.PI * 7 / 12;
			this.velocidad = 1.8;
		}else {
			this.angulo = Math.PI * 5 / 12;
			this.velocidad = 2;
		}
	}

	public void dibujar(Entorno e) {
		e.dibujarImagen(img, x, y, angulo- Math.PI/1.5, 0.05);
		//e.dibujarCirculo(x, y, ancho, null); //HITBOX
	}

	public void sonidoImpacto() {
		sonidoImpacto.reproducirFX();
	}

	//MOVIMIENTO
	public void mover(Entorno e) {
		x += velocidad * Math.cos(angulo);
		y += velocidad * Math.sin(angulo);

		if(this.x < e.ancho() * 0.05) {
			this.x = e.ancho() * 0.05;
			this.angulo = Math.PI - this.angulo;
		}
		if(this.x > e.ancho() * 0.95) {
			this.x = e.ancho() * 0.95;
			this.angulo = Math.PI - this.angulo;
		}
		if (this.y > e.alto() + alto) {
			this.y = -alto * 2;
		}
	}

	//COLISIONES
	//Devuelve true si hay otro asteroide en su zona
	public boolean estaEnZona(Asteroide a) {
		return Math.sqrt(Math.pow(this.x - a.getX(),2) + Math.pow(this.y - a.getY(),2)) - (this.ancho / 2 + a.getAncho() / 2) < 100 || a.getY() < 50;	
	}

	//Colision con proyectil
	public boolean chocasteCon(Proyectil p) {
		return Math.sqrt(Math.pow(this.x - p.getX(),2) + Math.pow(this.y - p.getY(),2)) - (this.ancho / 2 + p.getAncho() / 2) < 0;
	}

	//METODOS DE ACTIVACION 
	//Si esta activado se muestra en pantalla
	public boolean estaActivado() {
		return activado;
	}

	public void switchActivar() {
		activado = !activado;
	}

	//GETTERS
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getAncho() {
		return ancho;
	}
}
