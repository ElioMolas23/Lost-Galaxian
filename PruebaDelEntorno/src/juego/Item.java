package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Item {
	double x, y, ancho, velocidad;
	Image img;
	Sonido sonido;

	public Item(double x,int vidas) {
		this.x = x;
		this.y = 1;
		this.ancho = 50;
		this.velocidad = 1.8;
		if(vidas < 3) {
			this.img = Herramientas.cargarImagen("vidaUP.png") ;
		}else {
			this.img = Herramientas.cargarImagen("puntosUP.png") ;
		}
		this.sonido = new Sonido("/vidaUP.wav");
	}

	public void dibujar(Entorno e) {
		//e.dibujarCirculo(x, y, ancho, null); //HITBOX
		e.dibujarImagen(img, x, y, 0, 0.2);
	}

	public void sonidoAgarrado() {
		sonido.reproducirFX();
	}

	public boolean estaDentro(Entorno e) {
		return y > 0 && y < e.alto() + ancho;
	}

	public void mover(Entorno e) {
		y += velocidad;
	}

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
