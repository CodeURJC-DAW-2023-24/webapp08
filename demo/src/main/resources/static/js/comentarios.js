async function enviarComentario(){
    let comentario = document.getElementById('comentario').value;
    if (comentario != ""){ 

    let id = document.getElementById('id').value;
    const response = await fetch( `/enviarComentario?comentario=${comentario}&id=${id}`,{
            method: 'POST'
          });
     
    let comentarios = await response.json();
    agregarComentarios(comentarios)
    }
    }

    function agregarComentarios(comentario) {
      let containerMensajes = document.getElementById('container-mensajes');
  
    
          let coment = document.createElement('div');
          coment.classList.add('coment');
          coment.style.display = 'flex'; 
          coment.style.flexDirection = 'column'; 
          // Create user imagen 
          let img = document.createElement('img');
         // img.src = comentario.imagen;
         // img.classList.add('rutine-image');
  
          // Crear el nombre del usuario (en negrita)
          let pNombre = document.createElement('p');
          let bNombre = document.createElement('b');
          bNombre.textContent = comentario.nombre;
          pNombre.appendChild(bNombre);
  
          // Create user mensage
          let pMensaje = document.createElement('p');
          pMensaje.textContent = comentario.contenido;
  
          // Add imagen,name and mesage
         

          coment.appendChild(img);
          coment.appendChild(pNombre);
          
          coment.appendChild(pMensaje);
  
          // Add comment to the mensage container
          containerMensajes.appendChild(coment);
      
  }
  
    
