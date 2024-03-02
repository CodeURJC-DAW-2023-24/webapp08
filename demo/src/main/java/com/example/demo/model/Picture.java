package com.example.demo.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Picture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String tipoContenido;

    @Lob
    @ElementCollection(fetch = FetchType.EAGER)
    private List<byte[]> fragmentos;

	public Picture() {
	}

	public Picture(String name) {
		super();
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

    public void setName(String name){
        this.name = name;
    }

   public byte[] getDatos() throws IOException{
    byte[] datosAux = reconstruirDatos(fragmentos);
    return datosAux;
   } 

    
	public String getContenido() {
		return tipoContenido;
	}

    public void setContenido(String tipoContenido){
        this.tipoContenido = tipoContenido;
    }


	@Override
	public String toString() {
		return String.format("Imagen[id=%d, name='%s']",
				id, name);
	}

    
    public void setDatos(byte[] datosAux) throws IOException {
        List<byte[]>  datos = fragmentarDatos(datosAux, 128);
        this.fragmentos= datos;
    }
    public static List<byte[]> fragmentarDatos(byte[] datosOriginales, int tamañoFragmento) {
        List<byte[]> fragmentos = new ArrayList<>();
        int offset = 0;

        while (offset < datosOriginales.length) {
            int longitudFragmento = Math.min(tamañoFragmento, datosOriginales.length - offset);
            byte[] fragmento = new byte[longitudFragmento];
            System.arraycopy(datosOriginales, offset, fragmento, 0, longitudFragmento);
            fragmentos.add(fragmento);
            offset += longitudFragmento;
        }

        return fragmentos;
    }

    public static byte[] reconstruirDatos(List<byte[]> fragmentos) {
        int tamañoTotal = fragmentos.stream().mapToInt(bytes -> bytes.length).sum();
        byte[] datosReconstruidos = new byte[tamañoTotal];
        int offset = 0;

        for (byte[] fragmento : fragmentos) {
            System.arraycopy(fragmento, 0, datosReconstruidos, offset, fragmento.length);
            offset += fragmento.length;
        }

        return datosReconstruidos;
    }

   
}
