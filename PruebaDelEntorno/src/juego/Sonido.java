package juego;

import javax.sound.sampled.*;
import java.io.InputStream;
public class Sonido {
	private Clip clip;
	private AudioInputStream audioInputStream;

	public Sonido(String ruta) {
		try {
			// Carga el archivo de sonido
			InputStream inputStream = getClass().getResourceAsStream(ruta);
			audioInputStream = AudioSystem.getAudioInputStream(inputStream);
			clip = AudioSystem.getClip();
			clip.open(audioInputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reproducirFX() {
		try {
			if (clip != null) {
				// Reinicia el sonido desde el principio
				clip.setFramePosition(0);
				clip.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void reproducirMusica() {
		try {
			// Reproduce la música de forma continua
			clip.loop(Clip.LOOP_CONTINUOUSLY);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void pausarMusica() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
		}
	}

	public void pararMusica() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
			clip.setFramePosition(0);
		}
	}

}
