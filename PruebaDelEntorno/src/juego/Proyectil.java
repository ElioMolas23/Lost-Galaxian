package juego;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;



public class Proyectil {
	private double x,y,ancho, velocidad; 
	private Image imgBala;
	private boolean direccion; // true = bala de nave, false = bala de iones 


	public Proyectil(double x, double y, boolean direccion) {
		this.x = x;
		this.y = y;
		this.direccion = direccion; 
		this.velocidad = 8;
		this.ancho = 30;
		this.imgBala = Herramientas.cargarImagen("proyectil.png") ;
	}

	public Proyectil(double x, double y, boolean direccion, double velocidad) {
		this.x = x;
		this.y = y;
		this.direccion = direccion; 

		if(this.direccion) {
			this.velocidad = velocidad;
			this.ancho = 30;
			this.imgBala = Herramientas.cargarImagen("proyectil.png") ;
		}else{
			this.velocidad = velocidad;
			this.ancho = 20;
			this.imgBala = Herramientas.cargarImagen("proyectilIones.png") ;
		}
	}

	public void dibujar(Entorno e) {
		//e.dibujarCirculo(x, y, ancho, null); //HITBOX
		e.dibujarImagen(imgBala, x, y, 0, 0.5);
	}

	public void mover(Entorno e) {
		if(direccion) {
			y += velocidad * (-1);
		}else{
			y += velocidad * (1);
		}
	}
	//El 120 es para que la bala tenga un "Cooldown"
	public boolean estaDentro(Entorno e) {
		return y > 0 && y < e.alto() + 120;
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
