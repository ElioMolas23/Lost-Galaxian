package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class AstroMegaShip {
	private double x, y, velocidad, ancho;
	private Image img;
	private Sonido sonidoDisparo, sonidoImpacto;

	public AstroMegaShip(double x, double y) {
		this.x = x;
		this.y = y;
		this.ancho = 100;
		this.velocidad = 6;
		this.img = Herramientas.cargarImagen("nave.png") ;
		this.sonidoDisparo = new Sonido("/disparoNave.wav");
		this.sonidoImpacto = new Sonido("/impactoNave.wav");
	}

	public void dibujar(Entorno e) {
		//e.dibujarCirculo(x, y, ancho, null); //HITBOX
		e.dibujarImagen(img, x , y , 0, 0.25);
	}

	public void sonidoDisparo() {
		sonidoDisparo.reproducirFX();
	}

	public void sonidoImpacto() {
		sonidoImpacto.reproducirFX();
	}

	//MOVIMIENTO Y DISPARO
	//La direccion puede ser (-1 = izquierda || 1 = derecha)
	private void mover(Entorno e, int direc) {
		x += direc * velocidad;

		if(this.x < e.ancho()*0.05) {
			this.x = e.ancho()*0.05;
		}
		if(this.x > e.ancho() *0.95) {
			this.x = e.ancho() *0.95;
		}
	}

	public void moverHaciaLaIzquierda(Entorno e) {
		mover(e, -1);
	}

	public void moverHaciaLaDerecha(Entorno e) {
		mover(e, 1);
	}

	//True = hacia arriba
	public Proyectil dispararBala() {
		return  new Proyectil(x,y,true);
	}

	//COLISIONES
	//Colision con destructor
	public boolean chocasteCon(DestructorEstelar e) {
		return Math.sqrt(Math.pow(this.x - e.getX(),2) + Math.pow(this.y - e.getY(),2)) - (this.ancho / 2 + e.getAncho() / 2) < 0;
	}

	//Colision con proyectil
	public boolean chocasteCon(Proyectil p) {
		return Math.sqrt(Math.pow(this.x - p.getX(),2) + Math.pow(this.y - p.getY(),2)) - (this.ancho / 2 + p.getAncho() / 2) < 0;
	} 
	
	//Colision con item
	public boolean chocasteCon(Item i) {
		return Math.sqrt(Math.pow(this.x - i.getX(),2) + Math.pow(this.y - i.getY(),2)) - (this.ancho / 2 + i.getAncho() / 2) < 0;
	} 

	//Colision con asteroide
	public boolean chocasteCon(Asteroide a) {
		return Math.sqrt(Math.pow(this.x - a.getX(),2) + Math.pow(this.y - a.getY(),2)) - (this.ancho / 2 + a.getAncho() / 2) < 0;
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
