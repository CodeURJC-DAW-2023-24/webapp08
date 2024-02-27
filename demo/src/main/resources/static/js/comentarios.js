async function enviarComentario(){
    let comentario = document.getElementById('comentario').value;
    if (comentario != ""){ //Revisar si devuelve null o ""

    let id = document.getElementById('id').value;
    const response = await fetch( `/enviarComentario?comentario=${comentario}&id=${id}`,{
            method: 'POST'
          });
     
    let comentarios = await response.json();
    agregarComentarios(comentarios)
    }
    }

    function agregarComentarios(comentario) {
      // Obtener el contenedor de mensajes
      let containerMensajes = document.getElementById('container-mensajes');
  
      // Iterar sobre cada comentario en la lista
    
          // Crear un div para el comentario
          let coment = document.createElement('div');
          coment.classList.add('coment');
          coment.style.display = 'flex'; // Establecer display flex
          coment.style.flexDirection = 'column'; 
          // Crear la imagen del usuario
          let img = document.createElement('img');
         // img.src = comentario.imagen;
         // img.classList.add('rutine-image');
  
          // Crear el nombre del usuario (en negrita)
          let pNombre = document.createElement('p');
          let bNombre = document.createElement('b');
          bNombre.textContent = comentario.nombre;
          pNombre.appendChild(bNombre);
  
          // Crear el mensaje del usuario
          let pMensaje = document.createElement('p');
          pMensaje.textContent = comentario.contenido;
  
          // Agregar la imagen, el nombre y el mensaje al comentario
         

          coment.appendChild(img);
          coment.appendChild(pNombre);
          
          coment.appendChild(pMensaje);
  
          // Agregar el comentario al contenedor de mensajes
          containerMensajes.appendChild(coment);
      
  }
  
    
