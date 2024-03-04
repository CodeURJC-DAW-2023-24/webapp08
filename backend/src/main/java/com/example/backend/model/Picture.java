package com.example.backend.model;

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
	private String typeContent;

    @Lob
    @ElementCollection(fetch = FetchType.EAGER)
    private List<byte[]> fragments;

	protected Picture() {
        
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

   public byte[] getData() throws IOException{
    byte[] dataAux = rebuildData(fragments);
    return dataAux;
   } 

    
	public String getContent() {
		return typeContent;
	}

    public void setContent(String typeContent){
        this.typeContent = typeContent;
    }


	@Override
	public String toString() {
		return String.format("Imagen[id=%d, name='%s']",
				id, name);
	}

    
    public void setData(byte[] dataAux) throws IOException {
        List<byte[]>  data = fragmentData(dataAux, 128);
        this.fragments= data;
    }
    public static List<byte[]> fragmentData(byte[] originalData, int tamañoFragmento) {
        List<byte[]> fragments = new ArrayList<>();
        int offset = 0;

        while (offset < originalData.length) {
            int fragmentLength = Math.min(tamañoFragmento, originalData.length - offset);
            byte[] fragment = new byte[fragmentLength];
            System.arraycopy(originalData, offset, fragment, 0, fragmentLength);
            fragments.add(fragment);
            offset += fragmentLength;
        }

        return fragments;
    }

    public static byte[] rebuildData(List<byte[]> fragments) {
        int totalSize = fragments.stream().mapToInt(bytes -> bytes.length).sum();
        byte[] rebuildData = new byte[totalSize];
        int offset = 0;

        for (byte[] fragment : fragments) {
            System.arraycopy(fragment, 0, rebuildData, offset, fragment.length);
            offset += fragment.length;
        }

        return rebuildData;
    }

   
}
