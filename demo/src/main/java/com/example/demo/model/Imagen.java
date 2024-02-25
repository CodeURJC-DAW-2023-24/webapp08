package com.example.demo.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.sql.rowset.serial.SerialBlob;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mysql.cj.jdbc.Blob;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OrderColumn;

@Entity
public class Imagen {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String name;
	private String tipoContenido;

    @Lob
    @ElementCollection(fetch = FetchType.EAGER)
    private List<byte[]> fragmentos;

	public Imagen() {
		// Used by JPA
	}

	public Imagen(String name) {
		super();
		this.name = name;
		
	}

	public String getName() {
		return name;
	}

    public void setName(String name){
        this.name = name;
    }

   /**  public byte[] getDatos() {
         try {
            // Obtener un InputStream del Blob
            InputStream inputStream = datos.getBinaryStream();

            // Crear un ByteArrayOutputStream para almacenar los bytes
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            // Leer los bytes desde el InputStream y escribirlos en el ByteArrayOutputStream
            byte[] buffer = new byte[4096]; // Puedes ajustar el tamaño del buffer según tus necesidades
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // Obtener el array de bytes resultante
            byte[] byteArray = byteArrayOutputStream.toByteArray();
			return byteArray;
            // Puedes hacer lo que necesites con el array de bytes
           

        } catch (Exception e) {
            e.printStackTrace();
        }
		byte[] buffer = new byte[4096];
		return  buffer ;
    }
 * @throws IOException 
**/
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
